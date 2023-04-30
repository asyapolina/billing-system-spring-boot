package ru.nexign.cdr.generator.cdr;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.nexign.cdr.generator.PhoneNumberGenerator;
import ru.nexign.cdr.generator.cdr.CallTimeGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Component
public class CdrGenerator {
    @Value("${const.cdr.count}")
    private int count;
    @Value("${const.unique.phone.numbers}")
    private int uniqueNumbersCount;

    @Value("${const.filepath}")
    private String filePath;

    public void generateCdrFile(int month, int year) throws IOException {
        List<String> cdrList = new ArrayList<>();
        Random random = new Random();
        List<String> uniqueNumbers = new ArrayList<>(); // Для хранения уже добавленных уникальных номеров


        for (int i = 0; i < count; i++) {

            // Генерация типа вызова (01 - исходящий, 02 - входящий)
            String callType = random.nextInt(2) == 0 ? "01" : "02";

            // Генерация номера абонента
            String phoneNumber;
            if (uniqueNumbers.size() < uniqueNumbersCount) { // уникальные номера ограничены, чтобы генерировалось больше записей для одного номера
                phoneNumber = PhoneNumberGenerator.generateRussianPhoneNumber();
            }   else {
                phoneNumber = uniqueNumbers.get(random.nextInt(uniqueNumbers.size()));
            }

            // Генерация времени начала и конца звонка
            String callTime = CallTimeGenerator.generateCallTime(month, year);

            String cdr = callType + "," + phoneNumber + "," + callTime;
            cdrList.add(cdr);

            if (!uniqueNumbers.contains(phoneNumber)) {
                uniqueNumbers.add(phoneNumber);
            }
        }
        filePath.replace("\\", "/");
        Path path = Paths.get(filePath);
        if (path.getParent() != null && !path.getParent().toFile().exists()) {
            path.getParent().toFile().mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()))) {
            for (String cdr : cdrList) {
                writer.write(cdr);
                writer.newLine();
            }

        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }
}
