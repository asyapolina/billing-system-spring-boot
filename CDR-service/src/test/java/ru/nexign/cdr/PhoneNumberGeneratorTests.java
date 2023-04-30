package ru.nexign.cdr;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.nexign.cdr.generator.PhoneNumberGenerator;
import ru.nexign.cdr.generator.client.TariffGenerator;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PhoneNumberGeneratorTests {
    @Test
    @DisplayName("Ensure that PhoneNumberGenerator return valid values")
    void tesReturnValidNumbers() {
        Pattern pattern = Pattern.compile("\\+?[1-9][0-9]{10}");
        for (int i = 0; i < 10; ++i) {
            assertTrue(pattern.matcher(PhoneNumberGenerator.generateRussianPhoneNumber()).find());
        }
    }
}
