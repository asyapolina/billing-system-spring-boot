package ru.nexign.cdr;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.nexign.cdr.generator.cdr.CallTimeGenerator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class CallTimeGeneratorTests {
    @ParameterizedTest
    @Order(1)
    @DisplayName("Call time generator tests on valid values")
    @CsvSource({"1,2001", "10,2010", "5,2015", "1,2023", "2,2023", "3,2023", "4,2023"})
    void testCTGDontThrowOnValidValues(int month, int year) {
        assertDoesNotThrow(() -> CallTimeGenerator.generateCallTime(month, year));
    }

    @ParameterizedTest
    @Order(2)
    @DisplayName("Call time generator test for throwing on invalid values")
    @CsvSource({"1,1999", "-1,2001", "10,2045", "5,-1", "13,2021", "0,2011", "0,0", "12,2024"})
    void testCTGThrowsOnInvalidValues(int month, int year) {
        assertThrowsExactly(IllegalArgumentException.class, () -> CallTimeGenerator.generateCallTime(month, year),
                "Incorrect month and year values.");
    }

    @ParameterizedTest
    @Order(3)
    @DisplayName("Call time generator check for correct return format")
    @CsvSource({"1,2001", "10,2010", "5,2015", "1,2023", "2,2023", "3,2023", "4,2023"})
    void testCTGFormat(int month, int year) {
        String[] dateTimes = CallTimeGenerator.generateCallTime(month, year).split(",");
        assertEquals(2, dateTimes.length);
        String startTime = dateTimes[0];
        String endTime = dateTimes[1];
        String formatPattern = "yyyyMMddHHmmss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
        assertDoesNotThrow(() -> LocalDateTime.parse(startTime, formatter));
        assertDoesNotThrow(() -> LocalDateTime.parse(endTime, formatter));
        LocalDateTime startLDT = LocalDateTime.parse(startTime, formatter);
        assertEquals(month, startLDT.getMonth().getValue());
        assertEquals(year, startLDT.getYear());
        LocalDateTime endLDT = LocalDateTime.parse(startTime, formatter);
        assertEquals(month, endLDT.getMonth().getValue());
        assertEquals(year, endLDT.getYear());
    }
}
