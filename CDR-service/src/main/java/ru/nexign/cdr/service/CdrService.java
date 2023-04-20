package ru.nexign.cdr.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nexign.cdr.generator.cdr.CdrGenerator;
import ru.nexign.cdr.parser.CdrParser;
import ru.nexign.jpa.model.CallDataRecord;
import ru.nexign.jpa.model.CdrList;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class CdrService {
    private final CdrGenerator generator;
    private final CdrParser parser;

    @Autowired
    public CdrService(CdrGenerator generator, CdrParser parser) {
        this.generator = generator;
        this.parser = parser;
    }

    public void generateCdrFile(int month, int year) {
        try {
            generator.generateCdrFile(month, year);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CdrList sendCdrData(String filePath, int month, int year) throws IOException {
        generateCdrFile(month, year);
        List<CallDataRecord> cdrList;

        try {
            cdrList = parser.parse(filePath);
            log.info("Данные из файла получены");
        } catch (IOException e) {
            throw new IOException("Ошибка при чтении файла: " + e.getMessage());
        }

        return new CdrList(cdrList);
    }
}
