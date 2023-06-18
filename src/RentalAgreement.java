import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class RentalAgreement {
    Tool tool;
    int rentalDaysCount;
    LocalDate checkoutDate;
    LocalDate dueDate;
    BigDecimal dailyCharge;
    int chargeDaysCount;  // From day after checkoutDate through and including dueDate, excluding no charge days
    BigDecimal preDiscountCharge;  // chargeDaysCount * dailyCharge. Round half up to cents
    BigDecimal discountPercent;
    BigDecimal discountAmount;  // discountPercent * preDiscountCharge. Round half up to cents
    BigDecimal finalCharge;  // preDiscountCharge - discountAmount

    public RentalAgreement(Tool tool, int rentalDaysCount, LocalDate checkoutDate, LocalDate dueDate, BigDecimal dailyCharge, int chargeDaysCount, BigDecimal discountPercent) {
        this.tool = tool;
        this.rentalDaysCount = rentalDaysCount;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
        this.dailyCharge = dailyCharge;
        this.chargeDaysCount = chargeDaysCount;
        this.preDiscountCharge = dailyCharge.multiply(BigDecimal.valueOf(chargeDaysCount)).setScale(2, RoundingMode.HALF_UP);
        this.discountPercent = discountPercent;
        this.discountAmount = preDiscountCharge.multiply(discountPercent).setScale(2, RoundingMode.HALF_UP);
        this.finalCharge = preDiscountCharge.subtract(discountAmount).setScale(2);
    }

    public void Print() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();  // $9,999.99
        NumberFormat percentFormat = NumberFormat.getPercentInstance();  // 99%
        DateTimeFormatter dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);  // mm/dd/yy

        // Below String arg compiles down to StringBuilder
        System.out.println(
                "Tool code: " + tool.toolCode +
                "\nTool type: " + tool.toolType +
                "\nTool brand: " + tool.brand +
                "\nRental days: " + rentalDaysCount +
                "\nCheck out date: " + dateFormat.format(checkoutDate) +
                "\nDue date: " + dateFormat.format(dueDate) +
                "\nDaily rental charge: " + currencyFormat.format(dailyCharge) +
                "\nCharge days: " + chargeDaysCount +
                "\nPre-discount charge: " + currencyFormat.format(preDiscountCharge) +
                "\nDiscount percent: " + percentFormat.format(discountPercent) +
                "\nDiscount amount: " + currencyFormat.format(discountAmount) +
                "\nFinal charge: " + currencyFormat.format(finalCharge)
        );
    }
}
