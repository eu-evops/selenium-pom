import com.google.inject.Inject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import pages.google.Homepage;
import pages.google.SearchResultsPage;
import pages.google.sections.searchresults.SearchResult;
import pages.wikipedia.Article;
import uk.sponte.automation.seleniumpom.guice.DependencyInjectionConfiguration;
import uk.sponte.automation.seleniumpom.guice.SeleniumPomGuiceModule;

import java.util.Optional;
import java.util.concurrent.TimeoutException;

/**
 * Created by n450777 on 30/11/2015.
 */
public class GoogleGuiceDependencyInjectionTest {
    @Inject private Homepage homepage;
    @Inject private SearchResultsPage searchResultsPage;
    @Inject private Article article;

    @Inject private WebDriver driver;

    @Before
    public void setupDependencyInjection() {
        SeleniumPomGuiceModule seleniumPomGuiceModule = new SeleniumPomGuiceModule(new DependencyInjectionConfiguration());
        seleniumPomGuiceModule.injectMembers(this);

        driver.navigate().to("https://www.google.com/ncr");
    }

    @After
    public void teardown() {
        driver.quit();
    }

    @Test
    public void runExampleTest() throws TimeoutException {
        homepage.searchForm.searchFor("selenium");
        searchResultsPage.resultStats.waitFor();

        System.out.printf("Found %d results%n", searchResultsPage.searchResults.results.size());

        Optional<SearchResult> potentialSearchResult = searchResultsPage
                .searchResults
                .results
                .parallelStream()
                .filter(result -> result.getUrl().getHost().equals("en.wikipedia.org"))
                .findFirst();

        if(!potentialSearchResult.isPresent())
            throw new RuntimeException("Could not find a link with wikipedia in url");

        SearchResult searchResult = potentialSearchResult.get();
        String expectedSearchResultTitle = searchResult.title.getText();

        searchResult.select();

        article.firstHeading.waitFor();
        Assert.assertEquals(expectedSearchResultTitle, driver.getTitle());
    }
}
