# Selenium Page Object Model [![Build Status](https://teamcity.sponte.uk/app/rest/builds/buildType:(id:SeleniumPom_Ci)/statusIcon)](https://teamcity.sponte.uk/project.html?projectId=SeleniumPom&tab=projectOverview) #

THIS DOCUMENTATION IS WORK IN PROGRESS

## What is it ##

Selenium POM (Page Object Model) is a Java framework built on top of [Selenium](https://github.com/SeleniumHQ/selenium) that provides you the ability to describe your web application as a hierarchy of models and adding helper methods for most common functions such us double click, or get value.

## What about Selenium Page Objects? ##

You might be thinking _but selenium already has [Page Object Model](https://code.google.com/p/selenium/wiki/PageObjects), why would I use another framework?_ And you would be partially right. Selenium does indeed provide page object model but it is limited to a flat structure of models. What Selenium-POM provides is an ability to describe your page as a hierarchy of models. For example, have a look at google's search results page:

![Google search results page](https://cdn.pbrd.co/images/8mWndwr.png)

More information can be found in the ![wiki](https://github.com/sponte/selenium-pom.wiki)

## Requirements ##

Java 6

## How do I get it ##

Simplest approach is to use Maven, please add this to your pom file:

```xml
<dependency>
    <groupId>uk.sponte.automation</groupId>
    <artifactId>selenium-pom</artifactId>
    <version>1.0.3</version>
    <scope>test</scope>
</dependency>
```

