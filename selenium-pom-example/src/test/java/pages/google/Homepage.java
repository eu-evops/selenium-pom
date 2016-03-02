package pages.google;

import org.openqa.selenium.support.FindBy;
import pages.google.sections.homepage.SearchForm;

/**
 * Created by n450777 on 22/05/15.
 */
public class Homepage {
    @FindBy(css = ".sbibod")
    public SearchForm searchForm;
}
