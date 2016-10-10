package pages.google;

import org.openqa.selenium.support.FindBy;
import pages.google.sections.searchresults.SearchResults;
import uk.sponte.automation.seleniumpom.PageElement;

/**
 * Created by n450777 on 01/05/15.
 */
public class SearchResultsPage {
    public PageElement resultStats;

    @FindBy(id = "center_col")
    public SearchResults searchResults;
}
