import org.junit.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import pages.google.Homepage;
import pages.google.SearchResultsPage;
import pages.google.sections.searchresults.SearchResult;
import pages.wikipedia.Article;
import uk.sponte.automation.seleniumpom.PageFactory;

import java.util.Optional;
import java.util.concurrent.TimeoutException;

/**
 * Created by n450777 on 30/11/2015.
 */
public class PageFactoryExamplesTests {

    private WebDriver driver;

    private PageFactory pageFactory;

    @Before
    public void setup() {
        System.setProperty("selenium.webdriver", "chrome");

        pageFactory = new PageFactory();
        driver = pageFactory.getDriver();
        driver.navigate().to("http://www.google.com/ncr");
    }

    @After
    public void teardown() {
        driver.quit();
    }


    @Test
    public void runExampleTest() throws TimeoutException {
        Homepage homepage = pageFactory.get(Homepage.class);
        homepage.searchForm.searchFor("selenium");

        SearchResultsPage searchResultsPage = pageFactory.get(SearchResultsPage.class);
        searchResultsPage.resultStats.waitFor();

        System.out.printf("Found %d results%n", searchResultsPage.searchResults.results.size());

        Optional<SearchResult> searchResult = searchResultsPage
                .searchResults
                .results
                .stream()
                .filter(result -> result.getUrl().getHost().equals("en.wikipedia.org"))
                .findAny();

        if(!searchResult.isPresent())
            throw new RuntimeException("Could not find a link with wikipedia in url");

        String expectedPageTitle = searchResult.get().title.getText();
        searchResult.get().select();

        Article article = pageFactory.get(Article.class);
        article.firstHeading.waitFor();

        Assert.assertEquals(expectedPageTitle, pageFactory.getDriver().getTitle());
    }
}
