package ru.nexign.cdr.generator;

import java.util.Random;

public class PhoneNumberGenerator {
    public static String generateRussianPhoneNumber() {
        String formatPattern = "7xxxxxxxxxx"; // Шаблон формата номера телефона

        StringBuilder phoneNumber = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < formatPattern.length(); i++) {
            char formatChar = formatPattern.charAt(i);

            if (formatChar == 'x') {
                phoneNumber.append(random.nextInt(10)); // подставляет рандомные цифры вместо х
            } else {
                phoneNumber.append(formatChar);
            }
        }

        return phoneNumber.toString();
    }
}
