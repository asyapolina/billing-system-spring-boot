package ru.nexign.cdr.parser;

import org.springframework.stereotype.Component;
import ru.nexign.jpa.enums.CallType;
import ru.nexign.jpa.model.CallDataRecord;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class CdrParser {

    public List<CallDataRecord> parse(String filePath) throws IOException {
        List<CallDataRecord> cdrList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String line;

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        while ((line = reader.readLine()) != null) {
            String[] fields = line.split(",");

            String callType = fields[0].trim();
            String phoneNumber = fields[1].trim();
            LocalDateTime startTime = LocalDateTime.parse(fields[2].trim(), formatter);
            LocalDateTime endTime = LocalDateTime.parse(fields[3].trim(), formatter);

            CallType callTypeEnum = null;
            if (callType.equals(CallType.INCOMING.name())) {
                callTypeEnum = CallType.INCOMING;
            } else if (callType.equals(CallType.OUTGOING.name())) {
                callTypeEnum = CallType.OUTGOING;
            }
            cdrList.add(new CallDataRecord(phoneNumber, callTypeEnum, startTime, endTime, null));
        }
        reader.close();

        return cdrList;
    }
}
