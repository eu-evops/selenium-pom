package uk.sponte.automation.seleniumpom.testobjects.ba;

import com.google.inject.Inject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.seleniumpom.PageElement;

/**
 * Created by n450777 on 04/03/2016.
 */
public class BaMobileHomePage {

    @Inject
    WebDriver driver;


    @FindBy(name = "depDate")
    public PageElement departureDate;

    @FindBy(name = "retDate")
    public PageElement returnDate;

    @Inject
    MobileDateSelector dateSelector;

    public void load() {
        driver.navigate().to("http://www.britishairways.com/travel/mfx/public/en_gb");
    }

}
