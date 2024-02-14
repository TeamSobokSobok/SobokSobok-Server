package io.sobok.SobokSobok.utils;

import java.time.LocalDate;

public class DateUtil {

    public static LocalDate getStartDateOfMonth(LocalDate date) {
        return date.withDayOfMonth(1);
    }

    public static LocalDate getEndDateOfMonth(LocalDate date) {
        return date.withDayOfMonth(date.lengthOfMonth());
    }
}
