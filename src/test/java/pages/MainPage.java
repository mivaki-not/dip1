package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage {

    private final SelenideElement buyButton = $$("button").findBy(text("Купить"));
    private final SelenideElement creditButton = $$("button").findBy(text("Купить в кредит"));

    public PaymentPage openPaymentPage() {
        buyButton.click();
        return new PaymentPage();
    }

    public CreditPage openCreditPage() {
        creditButton.click();
        return new CreditPage();
    }
}