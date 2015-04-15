package uk.sponte.automation.web.tests;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ByIdOrName;
import uk.sponte.automation.web.PageElement;
import uk.sponte.automation.web.PageFactory;
import uk.sponte.automation.web.dependencies.DependencyInjector;
import uk.sponte.automation.web.testobjects.pages.TestPage;
import uk.sponte.automation.web.testobjects.sections.ChildSection;
import uk.sponte.automation.web.testobjects.sections.ParentSection;
import uk.sponte.automation.web.testobjects.sections.PlainSection;
import uk.sponte.automation.web.testobjects.sections.TestSection;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by swozniak-ba on 02/04/15.
 */
public class PageTest {
    TestPage testPage;
    private WebDriver webDriverMock;
    private PageFactory pageFactory;
    private WebElement headlineWebElementMock;
    private WebElement someInputWebElementMock;
    private DependencyInjector mockDependencyInjector;

    @Before
    public void setup() {
        System.out.println(this.getClass().getResource(".").getPath());

        mockDependencyInjector = mock(DependencyInjector.class);
        webDriverMock = mock(WebDriver.class);
        headlineWebElementMock = mock(WebElement.class);
        someInputWebElementMock = mock(WebElement.class);

        when(mockDependencyInjector.get(TestPage.class)).thenReturn(new TestPage());
        when(mockDependencyInjector.get(ParentSection.class)).thenReturn(new ParentSection());
        when(mockDependencyInjector.get(ChildSection.class)).thenReturn(new ChildSection());
        when(mockDependencyInjector.get(TestSection.class)).thenReturn(new TestSection());
        when(mockDependencyInjector.get(WebDriver.class)).thenReturn(webDriverMock);

        PlainSection plainSectionMock = mock(PlainSection.class);
        when(mockDependencyInjector.get(PlainSection.class)).thenReturn(plainSectionMock);

        when(headlineWebElementMock.getText()).thenReturn("Headline");

        WebElement plainSectionChildElementMock = mock(WebElement.class);

        when(plainSectionChildElementMock.getTagName()).thenReturn("Hello World");
        when(webDriverMock.findElement(new By.ById("plainSectionChild"))).thenReturn(plainSectionChildElementMock);
        when(webDriverMock.findElement(By.tagName("h1"))).thenReturn(headlineWebElementMock);
        when(webDriverMock.findElement(new ByIdOrName("someInput"))).thenReturn(someInputWebElementMock);

        pageFactory = new PageFactory(mockDependencyInjector);
        testPage = pageFactory.get(TestPage.class);
    }

    @Test
    public void pageCreation() {
        assertNotNull("page has not been initialised", testPage);
    }

    @Test
    public void pageElement() {
        assertNotNull("pageElement has not been initialized", testPage.headline);
    }

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

    @Test
    public void canGetChildSectionOfASection() {
        ArrayList<WebElement> elements = new ArrayList<WebElement>();
        elements.add(headlineWebElementMock);
        when(webDriverMock.findElements(By.className("item"))).thenReturn(elements);
        WebElement parentMock = mock(WebElement.class);
        when(webDriverMock.findElement(new ByIdOrName("parent"))).thenReturn(parentMock);
        WebElement childMock = mock(WebElement.class);
        when(parentMock.findElement(new ByIdOrName("child"))).thenReturn(childMock);
        when(childMock.findElements(By.className("item"))).thenReturn(elements);

        assertNotNull("should be able to access child elements of a section",
                testPage.parent.child);

        assertEquals(1, testPage.parent.child.children.size());
    }
}
