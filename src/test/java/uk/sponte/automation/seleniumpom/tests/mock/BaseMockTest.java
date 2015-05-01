package uk.sponte.automation.seleniumpom.tests.mock;

import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ByIdOrName;
import uk.sponte.automation.seleniumpom.PageFactory;
import uk.sponte.automation.seleniumpom.dependencies.DefaultDependencyInjectorImpl;
import uk.sponte.automation.seleniumpom.dependencies.DependencyInjector;
import uk.sponte.automation.seleniumpom.testobjects.pages.TestPage;
import uk.sponte.automation.seleniumpom.testobjects.sections.FrameSection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Created by n450777 on 01/05/15.
 */
public class BaseMockTest {
    TestPage testPage;
    WebDriver webDriverMock;
    PageFactory pageFactory;
    WebElement headlineWebElementMock;
    WebElement someInputWebElementMock;
    DependencyInjector dependencyInjector;
    FrameSection frameSectionWebElementMock;

    @Before
    public void setup() {
        dependencyInjector = spy(new DefaultDependencyInjectorImpl());
        WebDriver.TargetLocator targetLocatorMock = mock(WebDriver.TargetLocator.class);

        webDriverMock = mock(WebDriver.class);
        when(webDriverMock.switchTo()).thenReturn(targetLocatorMock);

        headlineWebElementMock = mock(WebElement.class);
        someInputWebElementMock = mock(WebElement.class);
        frameSectionWebElementMock = mock(FrameSection.class);

        when(headlineWebElementMock.getText()).thenReturn("Headline");

        WebElement plainSectionChildElementMock = mock(WebElement.class);

        when(plainSectionChildElementMock.getTagName()).thenReturn("Hello World");

        when(webDriverMock.findElement(new By.ById("plainSectionChild"))).thenReturn(plainSectionChildElementMock);
        when(webDriverMock.findElement(By.tagName("h1"))).thenReturn(headlineWebElementMock);
        when(webDriverMock.findElement(new ByIdOrName("someInput"))).thenReturn(someInputWebElementMock);

        pageFactory = spy(new PageFactory(dependencyInjector));

        when(pageFactory.getDriver()).thenReturn(webDriverMock);

        testPage = pageFactory.get(TestPage.class);
    }

}
