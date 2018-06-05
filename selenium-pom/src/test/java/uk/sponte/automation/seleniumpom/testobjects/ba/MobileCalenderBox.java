package uk.sponte.automation.seleniumpom.testobjects.ba;

import com.google.inject.Inject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import uk.sponte.automation.seleniumpom.PageElement;
import uk.sponte.automation.seleniumpom.PageElementImpl;
import uk.sponte.automation.seleniumpom.PageSection;

import java.util.List;

/**
 * Created by n448846 on 09/10/2015.
 */
public class MobileCalenderBox extends PageSection {

    @Inject
    WebDriver driver;

    @FindBy(css = "div.picker__month")
    public PageElement monthName;

    @FindBy(css = "div.picker__nav--next")
    public PageElement calendarNext;

    @FindBy(css = "button.picker__buttonConfirm")
    public PageElement confirmDateButton;

    @FindBys({@FindBy(css = "table.picker__table"),@FindBy(css="td"),@FindBy(css="div")})
    public List<PageElement> calendarBox;

    PageElement dateCell(int date) {
        return new PageElementImpl(rootElement.findElement(
                        By.xpath(
                                String.format("//*[contains(@class,'picker--opened')]//*[contains(@class,'picker__day--infocus')][text()='%s']", String.valueOf(date))
                        )
        ));
    }
}
