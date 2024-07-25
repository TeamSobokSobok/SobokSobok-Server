package io.sobok.SobokSobok.utils;

import io.sobok.SobokSobok.exception.ErrorCode;
import io.sobok.SobokSobok.exception.model.BadRequestException;
import io.sobok.SobokSobok.pill.domain.Pill;
import io.sobok.SobokSobok.pill.ui.dto.PillRequest;
import io.sobok.SobokSobok.pill.ui.dto.PillUpdateRequest;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PillUtil {

    public static int getRandomColorNumber() {

        Random random = new Random();
        return random.nextInt(5) + 1;
    }

    public static LocalDate[] getScheduleDateList(LocalDate startDate, LocalDate endDate, String[] days) {

        List<LocalDate> matchingDates = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            DayOfWeek currentDayOfWeek = currentDate.getDayOfWeek();
            String currentDayOfWeekString = currentDayOfWeek.toString();

            for (String day : days) {
                if (currentDayOfWeekString.equalsIgnoreCase(convertToEnglishDayOfWeek(day))) {
                    matchingDates.add(currentDate);
                    break;
                }
            }

            currentDate = currentDate.plusDays(1);
        }

        return matchingDates.toArray(new LocalDate[0]);
    }

    public static Boolean checkChangePillSchedule(Pill pill, PillUpdateRequest pillRequest) {
        return !pill.getScheduleDay().equals(pillRequest.day()) ||
                !pill.getStartDate().equals(pillRequest.startDate()) ||
                !pill.getEndDate().equals(pillRequest.endDate());
    }

    private static String convertToEnglishDayOfWeek(String koreanDayOfWeek) {
        return switch (koreanDayOfWeek) {
            case "월" -> "MONDAY";
            case "화" -> "TUESDAY";
            case "수" -> "WEDNESDAY";
            case "목" -> "THURSDAY";
            case "금" -> "FRIDAY";
            case "토" -> "SATURDAY";
            case "일" -> "SUNDAY";
            default -> throw new BadRequestException(ErrorCode.BAD_REQUEST_EXCEPTION);
        };
    }
}
