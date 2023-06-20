import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoField;

public class ToolPricing {
    ToolType toolType;
    BigDecimal dailyCharge;
    boolean isWeekdayCharged;
    boolean isWeekendCharged;
    boolean isHolidayCharged;

    public ToolPricing(ToolType toolType, BigDecimal dailyCharge, boolean isWeekdayCharged, boolean isWeekendCharged, boolean isHolidayCharged) {
        this.toolType = toolType;
        this.dailyCharge = dailyCharge;
        this.isWeekdayCharged = isWeekdayCharged;
        this.isWeekendCharged = isWeekendCharged;
        this.isHolidayCharged = isHolidayCharged;
    }

    public int GetChargeDaysCount(Store store, int rentalDaysCount, LocalDate checkoutDate, LocalDate dueDate) {
        int chargeDaysCount = 0;  // From day after checkoutDate through and including dueDate

        // Linear solution, too slow as rentalDaysCount approaches max limit:
        // checkoutDate.plusDays(1).datesUntil(dueDate.plusDays(1)).forEach(date -> {});

        // Add any valid weekdays or weekends
        if (isWeekdayCharged != isWeekendCharged) {
            int weekendDaysCount = GetWeekendDaysCount(rentalDaysCount, checkoutDate);

            if (isWeekendCharged) {
                chargeDaysCount += weekendDaysCount;
            }
            if (isWeekdayCharged) {
                int weekDaysCount = rentalDaysCount - weekendDaysCount;
                chargeDaysCount += weekDaysCount;
            }
        } else if (isWeekdayCharged && isWeekendCharged) {
            chargeDaysCount += rentalDaysCount;
        }

        // Exclude any valid holidays
        if (!isHolidayCharged) {
            int holidaysCount = 0;  // Holidays that would otherwise be charged (not already weekday/weekend excluded)
            // TODO
            chargeDaysCount -= holidaysCount;
        }

        return chargeDaysCount;
    }

    static int GetWeekendDaysCount(int rentalDaysCount, LocalDate checkoutDate) {
        int startingDay = checkoutDate.plusDays(1).get(ChronoField.DAY_OF_WEEK);  // 6 == Saturday, 7 == Sunday
        int repeatedWeeks = rentalDaysCount / 7;
        int weekendDaysCount = 2 * repeatedWeeks;
        int weekRemainingDays = rentalDaysCount % 7;
        if (weekRemainingDays > 0) {
            // Monday to Friday: 1 + 5 - 6 = 0
            // Monday to Saturday: 1 + 6 - 6 = 1
            // Saturday to Saturday: 6 + 1 - 6 = 1
            // Sunday to Sunday: 7 + 1 - 6 = 2
            // Saturday to Sunday: 6 + 2 - 6 = 2
            // Thursday to Tuesday: 4 + 6 - 6 = 4
            int weekendDeterminingCount = startingDay + weekRemainingDays - 6;
            if (weekendDeterminingCount > 0) {
                weekendDaysCount += 1;
                if (weekendDeterminingCount > 1 && startingDay != 7) {
                    weekendDaysCount += 1;
                }
            }
        }
        return weekendDaysCount;
    }
}
