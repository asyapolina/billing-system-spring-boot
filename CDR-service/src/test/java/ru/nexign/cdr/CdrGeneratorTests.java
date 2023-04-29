package ru.nexign.cdr;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import ru.nexign.cdr.generator.cdr.CdrGenerator;
import ru.nexign.cdr.generator.client.BalanceGenerator;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
public class CdrGeneratorTests {
    final static String filePath = "cdr/cdr.txt";
    static CdrGenerator getInitializedCdrGenerator(int count, int  uniqueNumbersCount, String filePath) {
        CdrGenerator cdr = new CdrGenerator();
        ReflectionTestUtils.setField(cdr, "count", count);
        ReflectionTestUtils.setField(cdr, "uniqueNumbersCount", uniqueNumbersCount);
        ReflectionTestUtils.setField(cdr, "filePath", filePath);
        return cdr;
    }

    @AfterEach
    void cleanAfterTest() {
        try {
            Path p = Paths.get(filePath);
            if (p.toFile().exists()) {
                Files.delete(p);
                while (p.getParent() != null) {
                    Files.delete(p.getParent());
                    p = p.getParent();
                }
            }
        } catch (IOException x) {
            // File permission problems are caught here.
            System.err.println(x);
        }
    }

    @Test
    @Order(1)
    @DisplayName("Ensure that class can be default constructed")
    void testCanConstructDefault() {
        assertDoesNotThrow(() -> new BalanceGenerator());
    }

    @Test
    @Order(2)
    @DisplayName("Ensure that class can be constructed with ReflectionTestUtils.setField")
    void testCanConstructAndSetField() {
        assertDoesNotThrow(() -> getInitializedCdrGenerator(100,50, "cdr.txt"));
    }

    @Test
    @Order(3)
    @DisplayName("Ensure that the generateCdrFile method can be called with valid numbers")
    void testCanCallGenerateCdrFile() {
        assertDoesNotThrow(() -> {
            CdrGenerator cdr = getInitializedCdrGenerator(100,50, filePath);
            cdr.generateCdrFile(10, 2022);
        });
    }

    @Test
    @Order(4)
    @DisplayName("Ensure that the generated file exists")
    void testGeneratedFileExists() {
        File f = new File(filePath);
        CdrGenerator cdr = getInitializedCdrGenerator(100,50, filePath);
        try {
            cdr.generateCdrFile(10, 2022);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertTrue(new File(filePath).exists());
    }

    void checkStartEndTime(String startTime, String endTime, int expectedYear, int expectedMonth) {
        String formatPattern = "yyyyMMddHHmmss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
        assertDoesNotThrow(() -> LocalDateTime.parse(startTime, formatter));
        assertDoesNotThrow(() -> LocalDateTime.parse(endTime, formatter));
        LocalDateTime startLDT = LocalDateTime.parse(startTime, formatter);
        assertEquals(expectedMonth, startLDT.getMonth().getValue());
        assertEquals(expectedYear, startLDT.getYear());
        LocalDateTime endLDT = LocalDateTime.parse(startTime, formatter);
        assertEquals(expectedMonth, endLDT.getMonth().getValue());
        assertEquals(expectedYear, endLDT.getYear());

    }
    @Test
    @Order(5)
    @DisplayName("Ensure that the generated file has correct structure")
    void testGeneratedFileHasCorrectStructure() {
        String filePath = "cdr1.txt";
        int year = 2022, month = 10;
        File f = new File(filePath);
        CdrGenerator cdr = getInitializedCdrGenerator(100,50, filePath);

        assertDoesNotThrow(() -> {
            cdr.generateCdrFile(10, 2022);
        });

        assertDoesNotThrow(() -> {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            while (line != null) {
                assertTrue(line.contains(","));
                String[] splitString = line.split(",");
                assertEquals(4, splitString.length);
                String callType = splitString[0];
                String phoneNumber = splitString[1];
                String startTime = splitString[2];
                String endTime = splitString[3];
                checkStartEndTime(startTime, endTime, year, month);
                line = reader.readLine();
            }
            reader.close();
        });
    }
}
