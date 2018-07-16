import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.google.Homepage;
import pages.google.SearchResultsPage;
import pages.google.sections.searchresults.SearchResult;
import pages.wikipedia.Article;
import uk.sponte.automation.seleniumpom.PageFactory;
import uk.sponte.automation.seleniumpom.guice.DependencyInjectionConfiguration;
import uk.sponte.automation.seleniumpom.guice.SeleniumPomGuiceParallelModule;

import java.util.Optional;
import java.util.concurrent.TimeoutException;

/**
 * Created by fecobar on 16/07/2018.
 */
public class GoogleGuiceDependencyInjectionTestNGParallel {
    private PageFactory pageFactory;

    @BeforeMethod
    public void setupDependencyInjection() {
        SeleniumPomGuiceParallelModule seleniumPomGuiceModule = new SeleniumPomGuiceParallelModule(new DependencyInjectionConfiguration());
        seleniumPomGuiceModule.injectMembers(this);
        pageFactory = seleniumPomGuiceModule.get(PageFactory.class);

        pageFactory.get().getDriver().navigate().to("https://www.google.com/ncr");
    }

    @AfterMethod
    public void teardown() {
        pageFactory.get().getDriver().quit();
    }

    @Test(threadPoolSize = 2, invocationCount = 3)
    public void runExampleTest() throws TimeoutException {
        Homepage homepage = pageFactory.get(Homepage.class);

        homepage.searchForm.searchFor("selenium");

        SearchResultsPage searchResultsPage = pageFactory.get(SearchResultsPage.class);

        searchResultsPage.resultStats.waitFor();

        System.out.printf("Found %d results%n", searchResultsPage.searchResults.results.size());

        Optional<SearchResult> potentialSearchResult = searchResultsPage
                .searchResults
                .results
                .parallelStream()
                .filter(result -> result.getUrl().getHost().equals("en.wikipedia.org"))
                .findFirst();

        if (!potentialSearchResult.isPresent())
            throw new RuntimeException("Could not find a link with wikipedia in url");

        SearchResult searchResult = potentialSearchResult.get();
        String expectedSearchResultTitle = searchResult.title.getText();

        searchResult.select();

        Article article = pageFactory.get(Article.class);
        article.firstHeading.waitFor();
        Assert.assertEquals(pageFactory.get().getDriver().getTitle(), expectedSearchResultTitle);
    }
}
