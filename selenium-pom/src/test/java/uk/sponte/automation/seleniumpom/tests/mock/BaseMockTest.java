package uk.sponte.automation.seleniumpom.tests.mock;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ByIdOrName;
import uk.sponte.automation.seleniumpom.PageFactory;
import uk.sponte.automation.seleniumpom.dependencies.DependencyInjector;
import uk.sponte.automation.seleniumpom.dependencies.GuiceDependencyInjector;
import uk.sponte.automation.seleniumpom.dependencies.WebDriverFactory;
import uk.sponte.automation.seleniumpom.testobjects.pages.TestPage;
import uk.sponte.automation.seleniumpom.testobjects.sections.FrameSection;

import static org.mockito.Mockito.*;

/**
 * Base test class for mocked tests
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
    WebDriverFactory webDriverFactoryMock;

    @Before
    public void setup() {
        dependencyInjector = spy(new GuiceDependencyInjector(null));

        webDriverFactoryMock = mock(WebDriverFactory.class);

        WebDriver.TargetLocator targetLocatorMock = mock(WebDriver.TargetLocator.class);
        webDriverMock = mock(WebDriver.class);
        doReturn(webDriverMock).when(dependencyInjector).get(WebDriver.class);

        WebDriver webDriver = dependencyInjector.get(WebDriver.class);

        when(dependencyInjector.get(WebDriver.class)).thenReturn(webDriverMock);
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
        doReturn(webDriverMock).when(pageFactory).getDriver();

        testPage = pageFactory.get(TestPage.class);
    }

    @After
    public void cleanup() {
        webDriverMock.quit();
    }

}
