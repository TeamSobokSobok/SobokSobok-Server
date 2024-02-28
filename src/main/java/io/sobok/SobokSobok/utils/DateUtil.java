package io.sobok.SobokSobok.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static LocalDate getStartDateOfMonth(LocalDate date) {
        return date.withDayOfMonth(1);
    }

    public static LocalDate getEndDateOfMonth(LocalDate date) {
        return date.withDayOfMonth(date.lengthOfMonth());
    }

    // ex) 07:00:00 -> 오전 7시
    public static String getKoreanTime(String timeString) {
        LocalTime time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm:ss"));
        String period = time.getHour() < 12 ? "오전" : "오후";
        String formattedTime = time.format(DateTimeFormatter.ofPattern("hh:mm"));

        return String.format("%s %s", period, formattedTime);
    }
}
