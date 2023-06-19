import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.util.HashMap;

public class Store {
    HashMap<ToolCode, Tool> toolsMap = new HashMap<>();
    HashMap<ToolType, ToolPricing> toolsPricingMap = new HashMap<>();

    public Store() {
        toolsMap.put(ToolCode.CHNS, new Tool(ToolCode.CHNS, ToolType.CHAINSAW, ToolBrand.STIHL));
        toolsMap.put(ToolCode.JAKD, new Tool(ToolCode.CHNS, ToolType.JACKHAMMER, ToolBrand.DEWALT));
        toolsMap.put(ToolCode.JAKR, new Tool(ToolCode.CHNS, ToolType.JACKHAMMER, ToolBrand.RIDGID));
        toolsMap.put(ToolCode.LADW, new Tool(ToolCode.LADW, ToolType.LADDER, ToolBrand.WERNER));

        toolsPricingMap.put(ToolType.CHAINSAW, new ToolPricing(ToolType.CHAINSAW, new BigDecimal("1.49"), true, false, true));
        toolsPricingMap.put(ToolType.JACKHAMMER, new ToolPricing(ToolType.JACKHAMMER, new BigDecimal("2.99"), true, false, false));
        toolsPricingMap.put(ToolType.LADDER, new ToolPricing(ToolType.LADDER, new BigDecimal("1.99"), true, true, false));
    }

    public RentalAgreement Checkout(ToolCode toolCode, String checkoutDateString, int rentalDaysCount, int discountPercentInt) {
        if (rentalDaysCount < 1 || rentalDaysCount > 365) {
            throw new IllegalArgumentException("Rental days count must be between 1 and 365");
        }
        if (discountPercentInt < 0 || discountPercentInt > 100) {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100");
        }

        Tool tool = toolsMap.get(toolCode);
        ToolPricing toolPricing = toolsPricingMap.get(tool.toolType);

        LocalDate checkoutDate = LocalDate.parse(checkoutDateString, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
        LocalDate dueDate = checkoutDate.plusDays(rentalDaysCount);
        int chargeDaysCount = GetChargeDaysCount(toolPricing, rentalDaysCount, checkoutDate, dueDate);

        BigDecimal discountPercent = BigDecimal.valueOf(discountPercentInt).divide(new BigDecimal("100"));
        return new RentalAgreement(tool, rentalDaysCount, checkoutDate, dueDate, toolPricing.dailyCharge, chargeDaysCount, discountPercent);
    }

    public int GetChargeDaysCount(ToolPricing toolPricing, int rentalDaysCount, LocalDate checkoutDate, LocalDate dueDate) {
        int chargeDaysCount = 0;  // From day after checkoutDate through and including dueDate

        // Linear solution, too slow as rentalDaysCount approaches max limit:
        // checkoutDate.plusDays(1).datesUntil(dueDate.plusDays(1)).forEach(date -> {});

        // Add any valid weekdays or weekends
        if (toolPricing.isWeekdayCharged != toolPricing.isWeekendCharged) {
            int startingDay = checkoutDate.plusDays(1).get(ChronoField.DAY_OF_WEEK);  // 6 == Saturday, 7 == Sunday
            int endingDay = dueDate.get(ChronoField.DAY_OF_WEEK);
            int repeatedWeeks = rentalDaysCount / 7;
            int weekendDaysCount = 2 * repeatedWeeks;
            if (startingDay != endingDay) {
                // TODO
            }
            int weekDaysCount = rentalDaysCount - weekendDaysCount;
            if (toolPricing.isWeekdayCharged) {
                chargeDaysCount += weekDaysCount;
            }
            if (toolPricing.isWeekendCharged) {
                chargeDaysCount += weekendDaysCount;
            }
        } else if (toolPricing.isWeekdayCharged && toolPricing.isWeekendCharged) {
            chargeDaysCount += rentalDaysCount;
        }

        // Exclude any valid holidays
        if (!toolPricing.isHolidayCharged) {
            int holidaysCount = 0;
            // TODO
            chargeDaysCount -= holidaysCount;
        }

        return chargeDaysCount;
    }
}
