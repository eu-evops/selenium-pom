package uk.sponte.automation.seleniumpom.tests.mock;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import uk.sponte.automation.seleniumpom.PageElement;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by swozniak-ba on 02/04/15.
 */
public class PageTest extends BaseMockTest {

    @Test
    public void canFindElementUsingAnnotations() {
        By by = By.tagName("h1");

        testPage.headline.getText();
        verify(webDriverMock).findElement(by);
    }

    @Test
    public void canGetText() {
        testPage.headline.getText();
        verify(headlineWebElementMock).getText();
    }

    @Test
    public void canGetValue() {
        testPage.headline.getValue();
        verify(headlineWebElementMock).getAttribute("value");
    }

    @Test
    public void canUseListOfSectionsFieldWithoutSectionAnnotation() {
        assertThat(testPage.sectionListWithoutAnnotation, is(not(nullValue())));
    }

    @Test
    public void returnsPageElement() {
        assertTrue(PageElement.class.isAssignableFrom(testPage.headline.getClass()));
    }

    @Test
    public void mapsListOfPageElements() {
        ArrayList<WebElement> elements = new ArrayList<WebElement>();
        elements.add(headlineWebElementMock);
        elements.add(headlineWebElementMock);
        elements.add(headlineWebElementMock);
        when(webDriverMock.findElements(By.className("item"))).thenReturn(elements);
        String expectedText = "test";
        when(headlineWebElementMock.getText()).thenReturn(expectedText);

        assertEquals("There should be 3 list elements on the page", 3, testPage.listPageElements.size());
        assertEquals(expectedText, testPage.listPageElements.get(0).getText());
    }

    @Test
    public void canReadElementsText() {
        assertEquals(testPage.headline.getText(), "Headline");
    }

    @Test
    public void canGetPageSection() {
        assertNotNull("page section should be initialized", testPage.parent);
    }
}
