import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

class StoreTest {
    private final Store store = new Store();

    @Test
    @DisplayName("GetHolidaysForYear assigns returned holiday lists to holidaysByYearMap ")
    void getHolidaysForYear() {
        assertEquals(0, store.holidaysByYearMap.size());
        store.GetHolidaysForYear(2023);
        assertEquals(1, store.holidaysByYearMap.size());
        assertEquals(2, store.holidaysByYearMap.get(2023).size());

        store.GetHolidaysForYear(2023);
        assertEquals(1, store.holidaysByYearMap.size());
        assertEquals(2, store.holidaysByYearMap.get(2023).size());

        store.GetHolidaysForYear(2015);
        assertEquals(2, store.holidaysByYearMap.size());
        assertEquals(2, store.holidaysByYearMap.get(2015).size());
    }

    /** Arg bounds tests */
    @Test
    @DisplayName("Checkout throws exception if arg rentalDaysCount isn't between 1 and 3650")
    void checkoutRentalDaysCountBounds() {
        ToolCode toolCode = ToolCode.LADW;
        assertThrows(IllegalArgumentException.class, () -> {
            store.Checkout(toolCode, "2/1/23", -1, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            store.Checkout(toolCode, "2/1/23", 0, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            store.Checkout(toolCode, "2/1/23", 3660, 0);
        });
    }

    @Test
    @DisplayName("Checkout throws exception if arg discountPercentInt isn't between 0 and 100")
    void checkoutDiscountPercentIntBounds() {
        ToolCode toolCode = ToolCode.LADW;
        assertThrows(IllegalArgumentException.class, () -> {
            store.Checkout(toolCode, "2/1/23", 7, -1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            store.Checkout(toolCode, "2/1/23", 7, 101);
        });
    }

    /** Arg parsing tests */
    @Test
    @DisplayName("Checkout parses arg checkoutDateString of format mm/dd/yy to LocalDate")
    void checkoutDateStringParsing() {
        int rentalDaysCount = 7;
        LocalDate dueDate = LocalDate.of(2023, 2, 8);

        RentalAgreement rentAgreement1 = store.Checkout(ToolCode.LADW, "02/01/23", rentalDaysCount, 0);
        assertEquals(dueDate, rentAgreement1.dueDate);

        RentalAgreement rentAgreement2 = store.Checkout(ToolCode.LADW, "2/1/23", rentalDaysCount, 0);
        assertEquals(dueDate, rentAgreement2.dueDate);
    }

    @Test
    @DisplayName("Checkout parses arg discountPercentInt to BigDecimal")
    void checkoutDiscountPercentIntParsing() {
        RentalAgreement rentAgreement = store.Checkout(ToolCode.LADW, "02/01/23", 7, 49);
        assertEquals(new BigDecimal(".49"), rentAgreement.discountPercent);
    }

    /** Baseline calculation tests */
    @Test
    @DisplayName("Checkout charging all rental days with no discount")
    void checkoutAllChargeDaysNoDiscount() {
        BigDecimal dailyCharge = store.toolsPricingMap.get(ToolType.LADDER).dailyCharge;
        assertEquals(new BigDecimal("1.99"), dailyCharge);
        assertTrue(store.toolsPricingMap.get(ToolType.LADDER).isWeekdayCharged);
        assertTrue(store.toolsPricingMap.get(ToolType.LADDER).isWeekendCharged);

        int chargeDaysCount = 7;
        RentalAgreement rentAgreement = store.Checkout(ToolCode.LADW, "2/1/23", chargeDaysCount, 0);
        assertEquals(chargeDaysCount, rentAgreement.chargeDaysCount);

        BigDecimal finalCharge = new BigDecimal("13.93");
        assertEquals(finalCharge, rentAgreement.preDiscountCharge);
        assertEquals(new BigDecimal("0.00"), rentAgreement.discountAmount);
        assertEquals(finalCharge, rentAgreement.finalCharge);
    }

    @Test
    @DisplayName("Checkout charging all rental days with discount")
    void checkoutAllChargeDaysWithDiscount() {
        RentalAgreement rentAgreement = store.Checkout(ToolCode.LADW, "2/1/23", 7, 50);
        assertEquals(new BigDecimal("13.93"), rentAgreement.preDiscountCharge);
        assertEquals(new BigDecimal("6.97"), rentAgreement.discountAmount);
        assertEquals(new BigDecimal("6.96"), rentAgreement.finalCharge);
    }

    /** Required tests */
    @Test
    @DisplayName("Checkout Test 1")
    void checkoutTest1() {
        assertThrows(IllegalArgumentException.class, () -> {
            store.Checkout(ToolCode.JAKR, "9/3/15", 5, 101);
        });
    }

    @Test
    @DisplayName("Checkout Test 2")
    void checkoutTest2() {
        RentalAgreement rentAgreement = store.Checkout(ToolCode.LADW, "7/2/20", 3, 10);
        assertEquals(new BigDecimal("3.98"), rentAgreement.preDiscountCharge);  // 2 * 1.99
        assertEquals(new BigDecimal("0.40"), rentAgreement.discountAmount);
        assertEquals(new BigDecimal("3.58"), rentAgreement.finalCharge);
    }

    @Test
    @DisplayName("Checkout Test 3")
    void checkoutTest3() {
        RentalAgreement rentAgreement = store.Checkout(ToolCode.CHNS, "7/2/15", 5, 25);
        assertEquals(new BigDecimal("4.47"), rentAgreement.preDiscountCharge);  // 3 * 1.49
        assertEquals(new BigDecimal("1.12"), rentAgreement.discountAmount);
        assertEquals(new BigDecimal("3.35"), rentAgreement.finalCharge);
    }

    @Test
    @DisplayName("Checkout Test 4")
    void checkoutTest4() {
        RentalAgreement rentAgreement = store.Checkout(ToolCode.JAKD, "9/3/15", 6, 0);
        assertEquals(new BigDecimal("8.97"), rentAgreement.preDiscountCharge);  // 3 * 2.99
        assertEquals(new BigDecimal("0.00"), rentAgreement.discountAmount);
        assertEquals(new BigDecimal("8.97"), rentAgreement.finalCharge);
    }

    @Test
    @DisplayName("Checkout Test 5")
    void checkoutTest5() {
        RentalAgreement rentAgreement = store.Checkout(ToolCode.JAKR, "7/2/15", 9, 0);
        assertEquals(new BigDecimal("14.95"), rentAgreement.preDiscountCharge);  // 5 * 2.99
        assertEquals(new BigDecimal("0.00"), rentAgreement.discountAmount);
        assertEquals(new BigDecimal("14.95"), rentAgreement.finalCharge);
    }

    @Test
    @DisplayName("Checkout Test 6")
    void checkoutTest6() {
        RentalAgreement rentAgreement = store.Checkout(ToolCode.JAKR, "7/2/20", 4, 50);
        assertEquals(new BigDecimal("2.99"), rentAgreement.preDiscountCharge);  // 1 * 2.99
        assertEquals(new BigDecimal("1.50"), rentAgreement.discountAmount);
        assertEquals(new BigDecimal("1.49"), rentAgreement.finalCharge);
    }
}
