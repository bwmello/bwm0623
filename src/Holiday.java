import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;

public abstract class Holiday {
    String name;
    int monthOfYear;

    abstract public LocalDate DateOfYear(int year);
}

class HolidayOfDate extends Holiday {
    int dayOfMonth;

    // "Independence Day", 7, 4
    public HolidayOfDate(String name, int monthOfYear, int dayOfMonth) {
        this.name = name;
        this.monthOfYear = monthOfYear;
        this.dayOfMonth = dayOfMonth;
    }

    // If falls on weekend, observed on closest weekday
    public LocalDate DateOfYear(int year) {
        LocalDate holidayDate = LocalDate.of(year, monthOfYear, dayOfMonth);
        int dayOfWeek = holidayDate.get(ChronoField.DAY_OF_WEEK);
        if (dayOfWeek == 6) {
            return holidayDate.plusDays(-1);  // previous Friday
        } else if (dayOfWeek > 6) {
            return holidayDate.plusDays(1); // next Monday
        } else {
            return holidayDate;
        }
    }
}

class HolidayOfOrdinal extends Holiday {
    int ordinal;  // ___ occurrence for dayOfWeek in monthOfYear
    DayOfWeek dayOfWeek;

    // "Labor Day", 9, 1, MONDAY
    public HolidayOfOrdinal(String name, int monthOfYear, int ordinal, DayOfWeek dayOfWeek) {
        this.name = name;
        this.monthOfYear = monthOfYear;
        this.ordinal = ordinal;
        this.dayOfWeek = dayOfWeek;
    }

    public LocalDate DateOfYear(int year) {
        LocalDate holidayDate = LocalDate.of(year, monthOfYear, 1);
        return  holidayDate.with(TemporalAdjusters.dayOfWeekInMonth(ordinal, dayOfWeek));
    }
}
