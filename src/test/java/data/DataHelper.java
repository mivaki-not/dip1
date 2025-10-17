package data;

import com.github.javafaker.Faker;
import lombok.Value;
import java.time.LocalDate;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class DataHelper {
    private static final Faker faker = new Faker(new Locale("en"));

    public static CardInfo getApprovedCard() {
        return new CardInfo(
                "4444 4444 4444 4441",
                getValidMonth(),
                getValidYear(),
                getValidName(),
                getValidCvc()
        );
    }

    public static CardInfo getDeclinedCard() {
        return new CardInfo(
                "4444 4444 4444 4442",
                getValidMonth(),
                getValidYear(),
                getValidName(),
                getValidCvc()
        );
    }

    public static String getValidMonth() {
        return String.format("%02d", LocalDate.now().getMonthValue());
    }

    public static String getValidYear() {
        int currentYear = LocalDate.now().getYear() % 100;
        int year = currentYear + ThreadLocalRandom.current().nextInt(1, 6);
        return String.format("%02d", year);
    }

    public static String getValidName() {
        return faker.name().lastName().toUpperCase() + " " + faker.name().firstName().toUpperCase();
    }

    public static String getValidCvc() {
        return String.format("%03d", ThreadLocalRandom.current().nextInt(1000));
    }

    public static String getInvalidCardNumber() {
        return "1111 2222 3333 4445";
    }

    public static String getInvalidMonth() {
        return "13";
    }

    public static String getZeroMonth() {
        return "00";
    }

    public static String getExpiredYear() {
        return "22";
    }

    public static String getFutureYear() {
        return "31";
    }

    public static String getInvalidName() {
        return "1vaн 1van0v@";
    }

    public static String getEmptyField() {
        return "";
    }

    @Value
    public static class CardInfo {
        String number;
        String month;
        String year;
        String holder;
        String cvc;
    }
}
