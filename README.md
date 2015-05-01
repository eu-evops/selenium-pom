# Selenium Page Object Model [![Build Status](https://travis-ci.org/sponte/selenium-pom.svg?branch=develop)](https://travis-ci.org/sponte/selenium-pom) #

THIS DOCUMENTATION IS WORK IN PROGRESS

## What is it ##

Selenium POM (Page Object Model) is a Java framework built on top of [Selenium](https://github.com/SeleniumHQ/selenium) that provides you the ability to describe your web application as a hierarchy of models and adding helper methods for most common functions such us double click, or get value.

## What about Selenium Page Objects? ##

You might be thinking _but selenium already has [Page Object Model](https://code.google.com/p/selenium/wiki/PageObjects), why would I use another framework?_ And you would be partially right. Selenium does indeed provide page object model but it is limited to a flat structure of models. What Selenium-POM provides is an ability to describe your page as a hierarchy of models. For example, have a look at google's search results page:

![Google search results page](https://cdn.pbrd.co/images/8mWndwr.png)

When you look at the page, you can visually break it down into areas:

- Header
- Authentication
- Search results
- Wikipedia

With selenium PageFactory, you can expose search results as follows:

```java
public class GoogleSearchResults {
    @FindBy(css = 'h3.r')
    List<WebElement> results;
}
```

This gives you access to all search result links on the page, but if you wanted to access link description? You would need to add a second field:

```java
public class GoogleSearchResults {
    @FindBy(css = 'h3.r')
    List<WebElement> results;

    @FindBy(css = 'div.rc .s .st')
    List<WebElement> resultDescriptions;
}
```

Now in order to find search result description for a link with certain url, you would have to do something like this:

```java
GoogleSearchResults searchResults = PageFactory.initElements(driver, GoogleSearchResults.class);
System.out.printf("Found %d results%n", searchResults.results.size());

for(int i=0; i<searchResults.results.size(); i++) {
    if(searchResults.results.get(i).getAttribute("href").contains("wikipedia")) {
        String resultDescription = searchResults.resultDescriptions.get(i).getText();
        System.out.println("Found result with description: " + resultDescription);
        break;
    }
}
```

With Sponte Selenium-POM you can define a collection of SearchResult objects as follows:



## Requirements ##

Java 6

## How do I get it ##

Simplest approach is to use Maven, please add this to your pom file:

```xml
<dependency>
    <groupId>uk.sponte.automation</groupId>
    <artifactId>selenium-pom</artifactId>
    <version>1.0.2</version>
    <scope>test</scope>
</dependency>
```

## How do I use it ##

Create a sample page object and expose properties:

Homepage.java

```java
public class Homepage {
    @FindBy(tagName = "title")
    public PageElement title;
}
```

Application.java

```java
public static class Application {
    public static void main(String[] args) {
        WebDriver driver = new FirefoxDriver();
    }
}
```