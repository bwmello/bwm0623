import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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
        Tool tool = toolsMap.get(toolCode);
        ToolPricing toolPricing = toolsPricingMap.get(tool.brand);

        LocalDate checkoutDate = LocalDate.parse(checkoutDateString, DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
        LocalDate dueDate = checkoutDate.plusDays(rentalDaysCount);
        int chargeDaysCount = rentalDaysCount;
        // TODO exclude no charge days from chargeDaysCount

        BigDecimal discountPercent = BigDecimal.valueOf(discountPercentInt).divide(new BigDecimal("100"));
        return new RentalAgreement(tool, rentalDaysCount, checkoutDate, dueDate, toolPricing.dailyCharge, chargeDaysCount, discountPercent);
    }
}
