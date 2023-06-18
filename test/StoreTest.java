import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

class StoreTest {
    private final Store store = new Store();

    /** Arg bounds tests */
    @Test
    @DisplayName("Checkout throws exception if arg rentalDaysCount isn't between 1 and 365")
    void testCheckoutRentalDaysCountBounds() {
        ToolCode toolCode = ToolCode.LADW;
        assertThrows(IllegalArgumentException.class, () -> {
            store.Checkout(toolCode, "2/1/23", -1, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            store.Checkout(toolCode, "2/1/23", 0, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            store.Checkout(toolCode, "2/1/23", 366, 0);
        });
    }

    @Test
    @DisplayName("Checkout throws exception if arg discountPercentInt isn't between 0 and 100")
    void testCheckoutDiscountPercentIntBounds() {
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
    void testCheckoutDateStringParsing() {
        int rentalDaysCount = 7;
        LocalDate dueDate = LocalDate.of(2023, 2, 8);

        RentalAgreement rentAgreement1 = store.Checkout(ToolCode.LADW, "02/01/23", rentalDaysCount, 0);
        assertEquals(dueDate, rentAgreement1.dueDate);

        RentalAgreement rentAgreement2 = store.Checkout(ToolCode.LADW, "2/1/23", rentalDaysCount, 0);
        assertEquals(dueDate, rentAgreement2.dueDate);
    }

    @Test
    @DisplayName("Checkout parses arg discountPercentInt to BigDecimal")
    void testCheckoutDiscountPercentIntParsing() {
        RentalAgreement rentAgreement = store.Checkout(ToolCode.LADW, "02/01/23", 7, 49);
        assertEquals(new BigDecimal(".49"), rentAgreement.discountPercent);
    }

    /** Baseline calculation tests */
    @Test
    @DisplayName("Checkout charging all rental days with no discount")
    void testCheckoutAllChargeDaysNoDiscount() {
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
    void testCheckoutAllChargeDaysWithDiscount() {
        RentalAgreement rentAgreement = store.Checkout(ToolCode.LADW, "2/1/23", 7, 50);
        assertEquals(new BigDecimal("13.93"), rentAgreement.preDiscountCharge);
        assertEquals(new BigDecimal("6.97"), rentAgreement.discountAmount);
        assertEquals(new BigDecimal("6.96"), rentAgreement.finalCharge);
    }

    /** Required calculation tests */
    // TODO
}
