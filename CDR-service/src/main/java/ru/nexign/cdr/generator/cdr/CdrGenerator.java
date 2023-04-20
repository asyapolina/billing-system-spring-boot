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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@Slf4j
public class CdrGenerator {
    @Value("${const.cdr.amount}")
    private int amount;

    @Value("${const.filepath}")
    private String filePath;

    public void generateCdrFile(int month, int year) throws IOException {
        List<String> cdrList = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < amount; i++) {
            // Генерация типа вызова (01 - исходящий, 02 - входящий)
            String callType = random.nextInt(2) == 0 ? "01" : "02";

            // Генерация номера абонента
            String phoneNumber = PhoneNumberGenerator.generateRussianPhoneNumber();

            // Генерация времени начала и конца звонка
            String callTime = CallTimeGenerator.generateCallTime(month, year);

            String cdr = callType + "," + phoneNumber + "," + callTime;
            cdrList.add(cdr);
        }

        String[] fields = filePath.split("/");
        Path path = Paths.get(fields[0], fields[1]);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()))) {
            for (String cdr : cdrList) {
                writer.write(cdr);
                writer.newLine();
            }

            log.info("Записи CDR сохранены в файл");
        } catch (IOException e) {
            throw new IOException("Ошибка при сохранении записей CDR в файл: " + e.getMessage());
        }
    }
}
