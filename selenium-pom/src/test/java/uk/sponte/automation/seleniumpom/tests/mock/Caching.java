package uk.sponte.automation.seleniumpom.tests.mock;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import uk.sponte.automation.seleniumpom.PageElement;
import uk.sponte.automation.seleniumpom.testobjects.sections.ListSubItem;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Created by n450777 on 04/03/2016.
 */
public class Caching extends BaseMockTest {
    @Test
    public void shouldNotCallFindElementMoreThanOnce() {
        testPage.headline.getText();
        testPage.headline.getText();
        verify(webDriverMock, times(1)).findElement(By.tagName("h1"));
    }

    @Test
    public void shouldNotFindSectionsOnTheListMoreThanOnce() {
        ArrayList<WebElement> list = new ArrayList<WebElement>();
        for (int i = 0; i < 5; i++) {
            list.add(someInputWebElementMock);
        }

        By selector = By.cssSelector(".longList li");
        when(webDriverMock.findElements(selector)).thenReturn(list);

        List<ListSubItem> elements = testPage.longListSections;

        for (int i=0; i<elements.size(); i++) {
            elements.get(i);
        }

        verify(webDriverMock, times(1)).findElements(
                selector);
    }

    @Test
    public void shouldNotFindElementsOnTheListMoreThanOnce() {
        ArrayList<WebElement> list = new ArrayList<WebElement>();
        for (int i = 0; i < 5; i++) {
            list.add(someInputWebElementMock);
        }

        By selector = By.cssSelector(".longList li");
        when(webDriverMock.findElements(selector)).thenReturn(list);

        List<PageElement> elements = testPage.longListElements;

        for (int i=0; i<elements.size(); i++) {
            elements.get(i);
        }

        verify(webDriverMock, times(1)).findElements(
                selector);
    }
}
