package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PaymentPage {

    private SelenideElement heading = $$(".heading").find(Condition.exactText("Оплата по карте"));
    private ElementsCollection formField = $$(".input__inner");
    private final SelenideElement cardNumber = formField.findBy(Condition.text("Номер карты")).$(".input__control");
    private final SelenideElement month = formField.findBy(Condition.text("Месяц")).$(".input__control");
    private final SelenideElement year = formField.findBy(Condition.text("Год")).$(".input__control");
    private final SelenideElement owner = formField.findBy(Condition.text("Владелец")).$(".input__control");
    private final SelenideElement cvc = formField.findBy(Condition.text("CVC/CVV")).$(".input__control");
    private final SelenideElement continueButton = $$("button").find(Condition.exactText("Продолжить"));
    private final SelenideElement successNotification = $(".notification_status_ok");
    private final SelenideElement closeNotification = $(".notification__closer");
    private final SelenideElement errorNotification = $(".notification_status_error");

    private final SelenideElement validationError = $(".input__sub");

    public void fillForm(String number, String month, String year, String owner, String cvc) {
        this.cardNumber.setValue(number);
        this.month.setValue(month);
        this.year.setValue(year);
        this.owner.setValue(owner);
        this.cvc.setValue(cvc);
        continueButton.click();
    }

    public void checkCvcValue(String expectedValue) {
        cvc.shouldHave(attribute("value", expectedValue));
    }

    public void checkCardNumberValue(String expectedValue) {
        cardNumber.shouldHave(attribute("value", expectedValue));
    }

    public void checkMonthValue(String expectedValue) {
        month.shouldHave(attribute("value", expectedValue));
    }

    public void checkYearValue(String expectedValue) {
        year.shouldHave(attribute("value", expectedValue));
    }

    public void checkSuccessNotification() {
        successNotification.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void checkErrorNotification() {
        errorNotification.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void checkValidationError(String expectedText) {
        validationError.shouldBe(visible)
                .shouldHave(exactText(expectedText));
    }

    public void closeNotification() {
        closeNotification.click();
    }
}