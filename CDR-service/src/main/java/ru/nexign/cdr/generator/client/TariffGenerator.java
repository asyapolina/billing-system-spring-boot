package ru.nexign.cdr.generator.client;

import com.github.javafaker.Faker;
import org.springframework.stereotype.Component;

@Component
public class TariffGenerator {
    public static String generateRandomTariffId() {
        String[] tariffIds = {"03", "06", "11"};
        return tariffIds[new Faker().number().numberBetween(0, tariffIds.length)];
    }
}
