import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ToolPricingTest {
    private final Store store = new Store();

    @Test
    @DisplayName("GetWeekendDaysCount with less than 7 rentalDaysCount")
    void getWeekendDaysCountWithinWeek() {
        // checkoutDate to dueDate, return number of weekendDays from day AFTER checkoutDate through and including dueDate
        // Sunday to Friday
        assertEquals(0, ToolPricing.GetWeekendDaysCount(5, LocalDate.of(2023, 1, 1)));
        // Sunday to Saturday
        assertEquals(1, ToolPricing.GetWeekendDaysCount(6, LocalDate.of(2023, 1, 1)));
        // Friday to Saturday
        assertEquals(1, ToolPricing.GetWeekendDaysCount(1, LocalDate.of(2022, 12, 30)));
        // Friday to Sunday
        assertEquals(2, ToolPricing.GetWeekendDaysCount(2, LocalDate.of(2022, 12, 30)));
        // Saturday to Sunday
        assertEquals(1, ToolPricing.GetWeekendDaysCount(6, LocalDate.of(2022, 12, 31)));
        // Saturday to Sunday
        assertEquals(1, ToolPricing.GetWeekendDaysCount(6, LocalDate.of(2022, 12, 31)));
    }

    @Test
    @DisplayName("GetWeekendDaysCount with at least 7 rentalDaysCount")
    void getWeekendDaysCountForMultipleWeeks() {
        // checkoutDate and rentalDaysCount, return number of weekendDays from day AFTER checkoutDate through and including dueDate
        // Monday 7 rental days
        assertEquals(2, ToolPricing.GetWeekendDaysCount(7, LocalDate.of(2023, 1, 2)));
        // Saturday 7 rental days
        assertEquals(2, ToolPricing.GetWeekendDaysCount(7, LocalDate.of(2022, 12, 31)));
        // Monday 35 rental days
        assertEquals(10, ToolPricing.GetWeekendDaysCount(35, LocalDate.of(2023, 1, 2)));
        // Monday 34 rental days
        assertEquals(10, ToolPricing.GetWeekendDaysCount(34, LocalDate.of(2023, 1, 2)));
        // Monday 33 rental days
        assertEquals(9, ToolPricing.GetWeekendDaysCount(33, LocalDate.of(2023, 1, 2)));
        // Monday 33 rental days
        assertEquals(8, ToolPricing.GetWeekendDaysCount(32, LocalDate.of(2023, 1, 2)));
    }

    @Test
    void getChargeDaysCount() {
        // TODO
    }
}
