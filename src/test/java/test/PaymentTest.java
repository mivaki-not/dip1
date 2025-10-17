package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import pages.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:8080");
        SQLHelper.cleanDatabase();
    }

    @Test
    @DisplayName("1. Успешная оплата APPROVED картой")
    void shouldSuccessWithApprovedCard() {
        var paymentPage = new MainPage().openPaymentPage();
        var card = DataHelper.getApprovedCard();
        paymentPage.fillForm(card.getNumber(), card.getMonth(), card.getYear(), card.getHolder(), card.getCvc());
        paymentPage.checkSuccessNotification();
        assertEquals("APPROVED", SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("2. Отказ DECLINED картой")
    void shouldFailWithDeclinedCard() {
        var paymentPage = new MainPage().openPaymentPage();
        var card = DataHelper.getDeclinedCard();
        paymentPage.fillForm(card.getNumber(), card.getMonth(), card.getYear(), card.getHolder(), card.getCvc());
        paymentPage.checkErrorNotification();
        assertEquals("DECLINED", SQLHelper.getPaymentStatus());
    }

    @Test
    @DisplayName("3. Несуществующая карта")
    void shouldFailWithInvalidCard() {
        var paymentPage = new MainPage().openPaymentPage();
        paymentPage.fillForm(
                DataHelper.getInvalidCardNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidName(),
                DataHelper.getValidCvc()
        );
        paymentPage.checkErrorNotification();
    }

    @Test
    @DisplayName("4. Буквы в номере карты")
    void shouldShowErrorForLettersInCardNumber() {
        var paymentPage = new MainPage().openPaymentPage();
        paymentPage.fillForm(
                "QQQQ WWWW EEEE RRRR",
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidName(),
                DataHelper.getValidCvc()
        );
        paymentPage.checkCardNumberValue("");
        paymentPage.checkValidationError("Неверный формат");
    }

    @Test
    @DisplayName("5. Спецсимволы в номере карты")
    void shouldShowErrorForSpecialCharsInCardNumber() {
        var paymentPage = new MainPage().openPaymentPage();
        paymentPage.fillForm(
                "@#(% @#$% &!<} &!<}",
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidName(),
                DataHelper.getValidCvc()
        );
        paymentPage.checkCardNumberValue("");
        paymentPage.checkValidationError("Неверный формат");
    }

    @Test
    @DisplayName("6. Неверный формат имени")
    void shouldShowErrorForInvalidName() {
        var paymentPage = new MainPage().openPaymentPage();
        paymentPage.fillForm(
                DataHelper.getApprovedCard().getNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getInvalidName(),
                DataHelper.getValidCvc()
        );
        paymentPage.checkValidationError("Неверный формат");
    }

    @Test
    @DisplayName("7. Пустое поле имени")
    void shouldShowErrorForEmptyName() {
        var paymentPage = new MainPage().openPaymentPage();
        paymentPage.fillForm(
                DataHelper.getApprovedCard().getNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getEmptyField(),
                DataHelper.getValidCvc()
        );
        paymentPage.checkValidationError("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("8. Пустое поле месяца")
    void shouldShowErrorForEmptyMonth() {
        var paymentPage = new MainPage().openPaymentPage();
        paymentPage.fillForm(
                DataHelper.getApprovedCard().getNumber(),
                DataHelper.getEmptyField(),
                DataHelper.getValidYear(),
                DataHelper.getValidName(),
                DataHelper.getValidCvc()
        );
        paymentPage.checkValidationError("Неверный формат");
    }

    @Test
    @DisplayName("9. Несуществующий месяц (13)")
    void shouldShowErrorForInvalidMonth() {
        var paymentPage = new MainPage().openPaymentPage();
        paymentPage.fillForm(
                DataHelper.getApprovedCard().getNumber(),
                DataHelper.getInvalidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidName(),
                DataHelper.getValidCvc()
        );
        paymentPage.checkValidationError("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("10. Неверный формат месяца")
    void shouldShowErrorForWrongMonthFormat() {
        var paymentPage = new MainPage().openPaymentPage();
        paymentPage.fillForm(
                DataHelper.getApprovedCard().getNumber(),
                "&D",
                DataHelper.getValidYear(),
                DataHelper.getValidName(),
                DataHelper.getValidCvc()
        );
        paymentPage.checkMonthValue("");
        paymentPage.checkValidationError("Неверный формат");
    }

    @Test
    @DisplayName("11. Пустое поле года")
    void shouldShowErrorForEmptyYear() {
        var paymentPage = new MainPage().openPaymentPage();
        paymentPage.fillForm(
                DataHelper.getApprovedCard().getNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getEmptyField(),
                DataHelper.getValidName(),
                DataHelper.getValidCvc()
        );
        paymentPage.checkValidationError("Неверный формат");
    }

    @Test
    @DisplayName("12. Неверный формат года")
    void shouldShowErrorForWrongYearFormat() {
        var paymentPage = new MainPage().openPaymentPage();
        paymentPage.fillForm(
                DataHelper.getApprovedCard().getNumber(),
                DataHelper.getValidMonth(),
                "(D",
                DataHelper.getValidName(),
                DataHelper.getValidCvc()
        );
        paymentPage.checkYearValue("");
        paymentPage.checkValidationError("Неверный формат");
    }

    @Test
    @DisplayName("13. Истекший срок карты")
    void shouldShowErrorForExpiredYear() {
        var paymentPage = new MainPage().openPaymentPage();
        paymentPage.fillForm(
                DataHelper.getApprovedCard().getNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getExpiredYear(),
                DataHelper.getValidName(),
                DataHelper.getValidCvc()
        );
        paymentPage.checkValidationError("Истёк срок действия карты");
    }

    @Test
    @DisplayName("14. Слишком далекий год")
    void shouldShowErrorForFutureYear() {
        var paymentPage = new MainPage().openPaymentPage();
        paymentPage.fillForm(
                DataHelper.getApprovedCard().getNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getFutureYear(),
                DataHelper.getValidName(),
                DataHelper.getValidCvc()
        );
        paymentPage.checkValidationError("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("15. Месяц '00'")
    void shouldShowErrorForZeroMonth() {
        var paymentPage = new MainPage().openPaymentPage();
        paymentPage.fillForm(
                DataHelper.getApprovedCard().getNumber(),
                DataHelper.getZeroMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidName(),
                DataHelper.getValidCvc()
        );
        paymentPage.checkValidationError("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("16. Неверный формат CVC")
    void shouldShowErrorForInvalidCvc() {
        var paymentPage = new MainPage().openPaymentPage();
        paymentPage.fillForm(
                DataHelper.getApprovedCard().getNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidName(),
                "ff*"
        );
        paymentPage.checkCvcValue("");
        paymentPage.checkValidationError("Неверный формат");
    }

    @Test
    @DisplayName("17. Пустое поле номера карты")
    void shouldShowErrorForEmptyCardNumber() {
        var paymentPage = new MainPage().openPaymentPage();
        paymentPage.fillForm(
                DataHelper.getEmptyField(),
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidName(),
                DataHelper.getValidCvc()
        );
        paymentPage.checkValidationError("Неверный формат");
    }

    @Test
    @DisplayName("18. Пустое поле CVC")
    void shouldShowErrorForEmptyCvc() {
        var paymentPage = new MainPage().openPaymentPage();
        paymentPage.fillForm(
                DataHelper.getApprovedCard().getNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidName(),
                ""
        );
        paymentPage.checkValidationError("Неверный формат");
    }
}
