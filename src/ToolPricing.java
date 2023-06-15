import java.math.BigDecimal;

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
}
