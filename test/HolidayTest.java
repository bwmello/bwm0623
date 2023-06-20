import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class HolidayTest {
    private final Holiday independenceDay = new HolidayOfDate("Independence Day", 7, 4);
    private final Holiday laborDay = new HolidayOfOrdinal("Labor Day", 9, 1,DayOfWeek.MONDAY);

    @Test
    @DisplayName("DateOfYear getting closest weekday for HolidayOfDate")
    void dateOfYearForHolidayOfDate() {
        // Falls on Tuesday, so observed on that Tuesday
        assertEquals(LocalDate.of(2023, 7, 4), independenceDay.DateOfYear(2023));
        // Falls on Sunday, so observed on next Monday
        assertEquals(LocalDate.of(2021, 7, 5), independenceDay.DateOfYear(2021));
        // Falls on Saturday, so observed on previous Friday
        assertEquals(LocalDate.of(2015, 7, 3), independenceDay.DateOfYear(2015));
    }

    @Test
    @DisplayName("DateOfYear getting date of weekday ordinal within month")
    void dateOfYearForHolidayOfOrdinal() {
        assertEquals(LocalDate.of(2023, 9, 4), laborDay.DateOfYear(2023));
        assertEquals(LocalDate.of(2022, 9, 5), laborDay.DateOfYear(2022));
        assertEquals(LocalDate.of(2021, 9, 6), laborDay.DateOfYear(2021));
        assertEquals(LocalDate.of(2015, 9, 7), laborDay.DateOfYear(2015));
    }
}
