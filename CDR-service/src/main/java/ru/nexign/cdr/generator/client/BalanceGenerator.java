package ru.nexign.cdr.generator.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class BalanceGenerator {
    @Value("${const.balance.lower}")
    private int lower;

    @Value("${const.balance.upper}")
    private int upper;

    public long generateBalance() {
        Random random = new Random();
        return random.nextInt(lower, upper);
    }
}
