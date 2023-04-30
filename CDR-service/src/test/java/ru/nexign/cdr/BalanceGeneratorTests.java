package ru.nexign.cdr;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import ru.nexign.cdr.generator.client.BalanceGenerator;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class BalanceGeneratorTests {

    static BalanceGenerator getInitializedBalanceGenerator(int lower, int upper) {
        BalanceGenerator b = new BalanceGenerator();
        ReflectionTestUtils.setField(b, "lower", lower);
        ReflectionTestUtils.setField(b, "upper", upper);
        return b;
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
        assertDoesNotThrow(() -> getInitializedBalanceGenerator(0, 100));
    }

    @Test
    @Order(3)
    @DisplayName("Ensure that the generateBalance method can be called")
    void testCanGenerateBalance() {
        assertDoesNotThrow(() -> {
            BalanceGenerator b = getInitializedBalanceGenerator(-10, 1000);
            b.generateBalance();
        });
    }

    @Test
    @Order(4)
    @DisplayName("Ensure that generated value is within predefined boundaries")
    void testGeneratedBalanceWithinProperBoundaries() {
        int lower = -10;
        int upper = 1000;
        BalanceGenerator b = getInitializedBalanceGenerator(lower, upper);
        for(int i = 0; i < (upper - lower) * 2; ++i) {
            double val = b.generateBalance();
            assertTrue(val >= lower && val <= upper);
        }
    }

    @Test
    @Order(5)
    @DisplayName("Ensure that it can generate different values")
    void testGeneratedValuesAreDifferent() {
        BalanceGenerator b = getInitializedBalanceGenerator(-10, 1000);
        HashSet<Long> values = new HashSet<Long>();

        for(int i = 0; i < 1000; ++i) {
            values.add(b.generateBalance());
        }
        // expected that at least 2 different values will be generated
        assertNotEquals(1, values.size());
    }
}
