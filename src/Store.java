import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Store {
    HashMap<ToolCode, Tool> toolsMap = new HashMap<>();
    HashMap<ToolType, ToolPricing> toolsPricingMap = new HashMap<>();
    List<Holiday> holidaysList = new ArrayList<>();
    HashMap<Integer, List<LocalDate>> holidaysByYearMap = new HashMap<>();

    public Store() {
        toolsMap.put(ToolCode.CHNS, new Tool(ToolCode.CHNS, ToolType.CHAINSAW, ToolBrand.STIHL));
        toolsMap.put(ToolCode.JAKD, new Tool(ToolCode.CHNS, ToolType.JACKHAMMER, ToolBrand.DEWALT));
        toolsMap.put(ToolCode.JAKR, new Tool(ToolCode.CHNS, ToolType.JACKHAMMER, ToolBrand.RIDGID));
        toolsMap.put(ToolCode.LADW, new Tool(ToolCode.LADW, ToolType.LADDER, ToolBrand.WERNER));

        toolsPricingMap.put(ToolType.CHAINSAW, new ToolPricing(ToolType.CHAINSAW, new BigDecimal("1.49"), true, false, true));
        toolsPricingMap.put(ToolType.JACKHAMMER, new ToolPricing(ToolType.JACKHAMMER, new BigDecimal("2.99"), true, false, false));
        toolsPricingMap.put(ToolType.LADDER, new ToolPricing(ToolType.LADDER, new BigDecimal("1.99"), true, true, false));

        holidaysList.add(new HolidayOfDate("Independence Day", 7, 4));
        holidaysList.add(new HolidayOfOrdinal("Labor Day", 9, 1, DayOfWeek.MONDAY));
    }

    public RentalAgreement Checkout(ToolCode toolCode, String checkoutDateString, int rentalDaysCount, int discountPercentInt) {
        if (rentalDaysCount < 1 || rentalDaysCount > 3650) {  // rentalDays between 1 day and 10 years
            throw new IllegalArgumentException("Rental days count must be between 1 and 3650");
        }
        if (discountPercentInt < 0 || discountPercentInt > 100) {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100");
        }

        Tool tool = toolsMap.get(toolCode);
        ToolPricing toolPricing = toolsPricingMap.get(tool.toolType);

        LocalDate checkoutDate = LocalDate.parse(checkoutDateString, DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
        LocalDate dueDate = checkoutDate.plusDays(rentalDaysCount);
        int chargeDaysCount = toolPricing.GetChargeDaysCount(this, rentalDaysCount, checkoutDate, dueDate);

        BigDecimal discountPercent = BigDecimal.valueOf(discountPercentInt).divide(new BigDecimal("100"));
        return new RentalAgreement(tool, rentalDaysCount, checkoutDate, dueDate, toolPricing.dailyCharge, chargeDaysCount, discountPercent);
    }

    public List<LocalDate> GetHolidaysForYear(int year) {
        if (holidaysByYearMap.containsKey(year)) {
            return holidaysByYearMap.get(year);
        }
        else {
            List<LocalDate> holidaysForYear = new ArrayList<>();
            for(Holiday holiday : holidaysList) {
                holidaysForYear.add(holiday.DateOfYear(year));
            }

            holidaysByYearMap.put(year, holidaysForYear);
            return holidaysForYear;
        }
    }
}
