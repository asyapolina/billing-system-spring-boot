package ru.nexign.cdr.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import ru.nexign.cdr.generator.CdrGenerator;
import ru.nexign.cdr.parser.CdrParser;
import ru.nexign.jpa.model.CallDataRecord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CdrService {
    private final CdrGenerator generator;
    private final CdrParser parser;
    private final JmsTemplate jmsTemplate;
    private final String cdrMq;

    @Autowired
    public CdrService(CdrGenerator generator, CdrParser parser,
                      JmsTemplate jmsTemplate,
                      @Value("${cdr.mq}") String cdrMq) {
        this.generator = generator;
        this.parser = parser;
        this.jmsTemplate = jmsTemplate;
        this.cdrMq = cdrMq;
    }

    public void generateCdrFile(int month, int year) {
        try {
            generator.generateCdrFile(month, year);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendCdrData(String filePath, int month, int year) throws IOException {
        generateCdrFile(month, year);
        List<CallDataRecord> cdrList;

        try {
            cdrList = parser.parse(filePath);
            log.info("Данные из файла получены");
        } catch (IOException e) {
            throw new IOException("Ошибка при чтении файла: " + e.getMessage());
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        jmsTemplate.convertAndSend(cdrMq, mapper.writeValueAsString(cdrList));
    }
}
