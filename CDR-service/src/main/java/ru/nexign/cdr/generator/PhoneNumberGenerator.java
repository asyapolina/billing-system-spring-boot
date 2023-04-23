package ru.nexign.cdr.generator;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PhoneNumberGenerator {
    public static String generateRussianPhoneNumber() {
        String formatPattern = "79xxxxxxxxx"; // Шаблон формата номера телефона

        StringBuilder phoneNumber = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < formatPattern.length(); i++) {
            char formatChar = formatPattern.charAt(i);

            if (formatChar == 'x') { // подставляет рандомные цифры вместо х
                phoneNumber.append(random.nextInt(5)); // Рандом от 0 до 5, чтобы увеличить шанс совпадения номера клиента и номера в cdr
            } else {
                phoneNumber.append(formatChar);
            }
        }

        return phoneNumber.toString();
    }
}
