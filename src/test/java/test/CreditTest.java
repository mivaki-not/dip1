package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.SQLHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import pages.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditTest {
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
    @DisplayName("19. Оплата тура в кредит со статусом APPROVED и валидными данными")
    void shouldSuccessWithApprovedCreditCard() {
        var creditPage = new MainPage().openCreditPage();
        var card = DataHelper.getApprovedCard();
        creditPage.fillForm(card.getNumber(), card.getMonth(), card.getYear(), card.getHolder(), card.getCvc());
        creditPage.checkSuccessNotification();
        assertEquals("APPROVED", SQLHelper.getCreditPaymentStatus());
    }

    @Test
    @DisplayName("20. Оплата тура в кредит со статусом DECLINED и валидными данными")
    void shouldFailWithDeclinedCreditCard() {
        var creditPage = new MainPage().openCreditPage();
        var card = DataHelper.getDeclinedCard();
        creditPage.fillForm(card.getNumber(), card.getMonth(), card.getYear(), card.getHolder(), card.getCvc());
        creditPage.checkErrorNotification();
        assertEquals("DECLINED", SQLHelper.getCreditPaymentStatus());
    }

    @Test
    @DisplayName("21. Оплата тура в кредит по несуществующей карте")
    void shouldFailWithInvalidCreditCard() {
        var creditPage = new MainPage().openCreditPage();
        creditPage.fillForm(
                DataHelper.getInvalidCardNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidName(),
                DataHelper.getValidCvc()
        );
        creditPage.checkErrorNotification();
    }

    @Test
    @DisplayName("22. Буквы в номере кредитной карты")
    void shouldShowErrorForLettersInCreditCardNumber() {
        var creditPage = new MainPage().openCreditPage();
        creditPage.fillForm(
                "QQQQ wwww rrrr tttt",
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidName(),
                DataHelper.getValidCvc()
        );
        creditPage.checkCardNumberValue("");
        creditPage.checkValidationError("Неверный формат");
    }

    @Test
    @DisplayName("23. Спецсимволы в номере кредитной карты")
    void shouldShowErrorForSpecialCharsInCreditCardNumber() {
        var creditPage = new MainPage().openCreditPage();
        creditPage.fillForm(
                "@#(% @#$% &!<} &!<}",
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidName(),
                DataHelper.getValidCvc()
        );
        creditPage.checkCardNumberValue("");
        creditPage.checkValidationError("Неверный формат");
    }

    @Test
    @DisplayName("24. Неверный формат имени при покупке тура в кредит")
    void shouldShowErrorForInvalidNameWhenBuyingOnCredit() {
        var creditPage = new MainPage().openCreditPage();
        creditPage.fillForm(
                DataHelper.getApprovedCard().getNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getInvalidName(),
                DataHelper.getValidCvc()
        );
        creditPage.checkValidationError("Неверный формат");
    }

    @Test
    @DisplayName("25. Оплата тура в кредит с незаполненным полем имени")
    void shouldShowErrorForEmptyNameWhenBuyingOnCredit() {
        var creditPage = new MainPage().openCreditPage();
        creditPage.fillForm(
                DataHelper.getApprovedCard().getNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getEmptyField(),
                DataHelper.getValidCvc()
        );
        creditPage.checkValidationError("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("26. Оплата тура в кредит с незаполненным полем месяца")
    void shouldShowErrorForEmptyMonthWhenBuyingOnCredit() {
        var creditPage = new MainPage().openCreditPage();
        creditPage.fillForm(
                DataHelper.getApprovedCard().getNumber(),
                DataHelper.getEmptyField(),
                DataHelper.getValidYear(),
                DataHelper.getValidName(),
                DataHelper.getValidCvc()
        );
        creditPage.checkValidationError("Неверный формат");
    }

    @Test
    @DisplayName("27. Оплата тура в кредит с несуществующим месяцем (13)")
    void shouldShowErrorForInvalidMonthWhenBuyingOnCredit() {
        var creditPage = new MainPage().openCreditPage();
        creditPage.fillForm(
                DataHelper.getApprovedCard().getNumber(),
                DataHelper.getInvalidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidName(),
                DataHelper.getValidCvc()
        );
        creditPage.checkValidationError("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("28. Оплата тура в кредит с неверным форматом месяца")
    void shouldShowErrorForWrongMonthFormatWhenBuyingOnCredit() {
        var creditPage = new MainPage().openCreditPage();
        creditPage.fillForm(
                DataHelper.getApprovedCard().getNumber(),
                "&D",
                DataHelper.getValidYear(),
                DataHelper.getValidName(),
                DataHelper.getValidCvc()
        );
        creditPage.checkMonthValue("");
        creditPage.checkValidationError("Неверный формат");
    }

    @Test
    @DisplayName("29. Оплата тура в кредит с пустым полем года")
    void shouldShowErrorForEmptyYearWhenBuyingOnCredit() {
        var creditPage = new MainPage().openCreditPage();
        creditPage.fillForm(
                DataHelper.getApprovedCard().getNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getEmptyField(),
                DataHelper.getValidName(),
                DataHelper.getValidCvc()
        );
        creditPage.checkValidationError("Неверный формат");
    }

    @Test
    @DisplayName("30. Оплата тура в кредит с неверным форматом года")
    void shouldShowErrorForWrongYearFormatWhenBuyingOnCredit() {
        var creditPage = new MainPage().openCreditPage();
        creditPage.fillForm(
                DataHelper.getApprovedCard().getNumber(),
                DataHelper.getValidMonth(),
                "(D",
                DataHelper.getValidName(),
                DataHelper.getValidCvc()
        );
        creditPage.checkYearValue("");
        creditPage.checkValidationError("Неверный формат");
    }

    @Test
    @DisplayName("31. Оплата тура в кредит с истекшим сроком карты")
    void shouldShowErrorForExpiredYearWhenBuyingOnCredit() {
        var creditPage = new MainPage().openCreditPage();
        creditPage.fillForm(
                DataHelper.getApprovedCard().getNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getExpiredYear(),
                DataHelper.getValidName(),
                DataHelper.getValidCvc()
        );
        creditPage.checkValidationError("Истёк срок действия карты");
    }

    @Test
    @DisplayName("32. Оплата тура в кредит со слишком далеким годом")
    void shouldShowErrorForFutureYearWhenBuyingOnCredit() {
        var creditPage = new MainPage().openCreditPage();
        creditPage.fillForm(
                DataHelper.getApprovedCard().getNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getFutureYear(),
                DataHelper.getValidName(),
                DataHelper.getValidCvc()
        );
        creditPage.checkValidationError("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("33. Оплата тура в кредит с '00' в месяце")
    void shouldShowErrorForZeroMonthWhenBuyingOnCredit() {
        var creditPage = new MainPage().openCreditPage();
        creditPage.fillForm(
                DataHelper.getApprovedCard().getNumber(),
                DataHelper.getZeroMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidName(),
                DataHelper.getValidCvc()
        );
        creditPage.checkValidationError("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("34. Оплата тура в кредит с неверным форматом CVC")
    void shouldShowErrorForInvalidCvcWhenBuyingOnCredit() {
        var creditPage = new MainPage().openCreditPage();
        creditPage.fillForm(
                DataHelper.getApprovedCard().getNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidName(),
                "ff*"
        );
        creditPage.checkCvcValue("");
        creditPage.checkValidationError("Неверный формат");
    }

    @Test
    @DisplayName("35. Оплата тура в кредит с пустым полем номера карты")
    void shouldShowErrorForEmptyCardNumberWhenBuyingOnCredit() {
        var creditPage = new MainPage().openCreditPage();
        creditPage.fillForm(
                DataHelper.getEmptyField(),
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidName(),
                DataHelper.getValidCvc()
        );
        creditPage.checkValidationError("Неверный формат");
    }

    @Test
    @DisplayName("36. Оплата тура в кредит с пустым полем CVC")
    void shouldShowErrorForEmptyCvcWhenBuyingOnCredit() {
        var creditPage = new MainPage().openCreditPage();
        creditPage.fillForm(
                DataHelper.getApprovedCard().getNumber(),
                DataHelper.getValidMonth(),
                DataHelper.getValidYear(),
                DataHelper.getValidName(),
                ""
        );
        creditPage.checkValidationError("Неверный формат");
    }
}