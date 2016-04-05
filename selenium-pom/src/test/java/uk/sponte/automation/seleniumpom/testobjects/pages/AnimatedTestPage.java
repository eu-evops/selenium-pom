package uk.sponte.automation.seleniumpom.testobjects.pages;

import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.seleniumpom.PageElement;

import java.util.List;

/**
 * Created by n450777 on 10/03/2016.
 */
public class AnimatedTestPage {
    public PageElement moveButton;

    @FindBy(css = ".listItem")
    public List<PageElement> listElements;
}
