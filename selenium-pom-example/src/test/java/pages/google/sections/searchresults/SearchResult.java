package pages.google.sections.searchresults;

import org.openqa.selenium.support.FindBy;
import uk.sponte.automation.seleniumpom.PageElement;
import uk.sponte.automation.seleniumpom.PageSection;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by n450777 on 01/05/15.
 */
public class SearchResult extends PageSection {
    @FindBy(tagName = "h3")
    public PageElement title;

    @FindBy(css = ".r a")
    private PageElement link;

    @FindBy(css = ".st")
    public PageElement description;

    public void select() {
        this.link.click();
    }

    public URL getUrl() {
        try {
            return new URL(this.link.getAttribute("href"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
