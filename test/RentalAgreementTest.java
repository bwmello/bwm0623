import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;

class RentalAgreementTest {
    private final Store store = new Store();
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    @DisplayName("Print formats numbers and dates as expected")
    void print() {
        Tool tool = store.toolsMap.get(ToolCode.LADW);
        int rentalDaysCount = 3;
        int chargeDaysCount = 3;
        LocalDate checkoutDate = LocalDate.of(2023, 2, 1);
        LocalDate dueDate = LocalDate.of(2023, 2, 4);
        BigDecimal dailyCharge = new BigDecimal("2");
        BigDecimal discountPercent = new BigDecimal(".2");

        RentalAgreement rentAgreement = new RentalAgreement(tool, rentalDaysCount, checkoutDate, dueDate, dailyCharge, chargeDaysCount, discountPercent);
        String expectedPrint = new StringBuilder()
                .append("Tool code: " + tool.toolCode.toString())
                .append("\nTool type: " + tool.toolType)
                .append("\nTool brand: " + tool.brand)
                .append("\nRental days: " + rentalDaysCount)
                .append("\nCheck out date: 2/1/23")
                .append("\nDue date: 2/4/23")
                .append("\nDaily rental charge: $2.00")
                .append("\nCharge days: " + chargeDaysCount)
                .append("\nPre-discount charge: $6.00")
                .append("\nDiscount percent: 20%")
                .append("\nDiscount amount: $1.20")
                .append("\nFinal charge: $4.80")
                .toString();

        rentAgreement.Print();
        assertEquals(expectedPrint, outputStreamCaptor.toString().trim());
    }
}
