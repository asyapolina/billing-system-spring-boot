package ru.nexign.cdr;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import ru.nexign.cdr.generator.client.TariffGenerator;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TariffGeneratorTests {
    @Test
    @DisplayName("Ensure that TariffGenerators return valid values")
    void testCanConstructDefault() {
        ArrayList<String> validValues = Lists.newArrayList("03", "06", "11");
        for (int i = 0; i < 10; ++i) {
            assertTrue(validValues.contains(TariffGenerator.generateRandomTariffId()));
        }
    }
}
