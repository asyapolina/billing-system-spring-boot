package ru.nexign.cdr;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import ru.nexign.cdr.generator.cdr.CdrGenerator;
import ru.nexign.cdr.generator.client.BalanceGenerator;
import static org.junit.jupiter.api.Assertions.*;
public class CdrGeneratorTests {
    @Test
    @DisplayName("Ensure that class can be default constructed")
    void testCanConstruct() {
        assertDoesNotThrow(() -> new CdrGenerator());
    }

    static CdrGenerator getInitializedCdrGenerator(int amount, int  uniqueNumbersAmount, String filePath) {
        CdrGenerator cdr = new CdrGenerator();
        ReflectionTestUtils.setField(cdr, "amount", amount);
        ReflectionTestUtils.setField(cdr, "uniqueNumbersAmount", uniqueNumbersAmount);
        ReflectionTestUtils.setField(cdr, "filePath", filePath);
        return b;
    }

    @Test
    @DisplayName("Ensure that the generateBalance method can be called")
    void testCanGenerateBalance() {
        assertDoesNotThrow(() -> {
            BalanceGenerator b = getInitializedBalanceGenerator(-10, 1000);
            b.generateBalance();
        });
    }

    @Test
    @DisplayName("Ensure that generated value is within predefined boundaries")
    void testGeneratedBalanceWithinProperBoundaries() {
        int lower = -10;
        int upper = 1000;
        BalanceGenerator b = getInitializedBalanceGenerator(lower, upper);
        for(int i = 0; i < (upper - lower); ++i) {
            double val = b.generateBalance();
            assertTrue(val >= lower && val <= upper);
        }
    }

    @Test
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
