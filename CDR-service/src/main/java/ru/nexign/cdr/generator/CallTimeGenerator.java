package ru.nexign.cdr.generator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class CallTimeGenerator {
    public static String generateCallTime(int month, int year) {
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonthValue = currentDate.getMonthValue();

        // Проверка на валидность месяца и года
        if (month < 1 || month > 12 || year > currentYear || (year == currentYear && month > currentMonthValue)) {
            throw new IllegalArgumentException("Неверные значения месяца и года");
        }

        // Проверка на високосный год
        Month targetMonth = Month.of(month);
        int daysInMonth = targetMonth.maxLength();
        if (targetMonth == Month.FEBRUARY && LocalDate.of(year, month, 1).isLeapYear()) {
            daysInMonth = 29;
        }

        Random random = new Random();
        int randomDay = random.nextInt(daysInMonth) + 1; // Случайный день в месяце
        LocalTime randomTime = LocalTime.of(random.nextInt(23), random.nextInt(60)); // Случайное время
        LocalDateTime callStartDateTime = LocalDateTime.of(year, targetMonth, randomDay, randomTime.getHour(), randomTime.getMinute());

        // Ограничение продолжительности звонка до 2 часов (ближе к реальности)
        int callDurationHours = random.nextInt(2);
        int callDurationMinutes = random.nextInt(60);
        LocalDateTime callEndDateTime = callStartDateTime.plusHours(callDurationHours).plusMinutes(callDurationMinutes);

        // Форматирование даты и времени в соответствии с шаблоном
        String formatPattern = "yyyyMMddHHmmss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
        String callStartDateTimeString = callStartDateTime.format(formatter);
        String callEndDateTimeString = callEndDateTime.format(formatter);

        return callStartDateTimeString + "," + callEndDateTimeString;
    }
}