package pages.google.sections.searchresults;

import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.seleniumpom.PageSection;

import java.util.List;

/**
 * Created by n450777 on 22/05/15.
 */
public class SearchResults extends PageSection {
    @FindAll({
            @FindBy(css = "div.rc"),
            @FindBy(css = "._NId > .srg > .g div.rc")
    })
    public List<SearchResult> results;
}
