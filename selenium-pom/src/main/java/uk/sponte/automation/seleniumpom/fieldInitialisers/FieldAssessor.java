package uk.sponte.automation.seleniumpom.fieldInitialisers;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import uk.sponte.automation.seleniumpom.PageElement;
import uk.sponte.automation.seleniumpom.PageSection;
import uk.sponte.automation.seleniumpom.annotations.Section;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by n450777 on 04/03/2016.
 */
public class FieldAssessor {

    public static boolean isValidPageElementList(Field field) {
        Class<?> fieldType = field.getType();
        if (!List.class.isAssignableFrom(fieldType)) return false;
        Type genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) return false;

        ParameterizedType genericTypeImpl = (ParameterizedType) genericType;
        if(!(genericTypeImpl.getActualTypeArguments()[0] instanceof Class<?>)) return false;

        if (!PageElement.class.isAssignableFrom((Class<?>)genericTypeImpl.getActualTypeArguments()[0])) return false;
        return true;
    }

    public static boolean isValidPageElement(Field field) {
        return PageElement.class.isAssignableFrom(field.getType());
    }

    public static boolean isValidPageSectionList(Field field) {
        // return false if it's not a list
        if (!List.class.isAssignableFrom(field.getType())) return false;

        // If we marked field with Section annotation, I'll assume you know what you're doing
        if (field.isAnnotationPresent(Section.class)) return true;

        // If it's not generic, return false
        Type genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) return false;

        ParameterizedType genericTypeImpl = (ParameterizedType) genericType;
        if(!(genericTypeImpl.getActualTypeArguments()[0] instanceof Class<?>)) return false;

        Class<?> genericTypeArgument = (Class<?>) genericTypeImpl.getActualTypeArguments()[0];

        // PageElement list is not pageSection
        if (PageElement.class.isAssignableFrom(genericTypeArgument)) return false;
        if (WebElement.class.isAssignableFrom(genericTypeArgument)) return false;

        if (PageSection.class.isAssignableFrom(genericTypeArgument)) return true;

        // If it's a list and has FindBy annotation it's a valid page section list as far as we're concerned
        if(hasSeleniumFindByAnnotation(field)) return true;

        return false;
    }

    public static boolean isValidPageSection(Field field) {
        Class<?> fieldType = field.getType();

        if(List.class.isAssignableFrom(fieldType)) return false;
        if(PageElement.class.isAssignableFrom(fieldType)) return false;
        if(WebElement.class.isAssignableFrom(fieldType)) return false;

        if(field.isAnnotationPresent(Section.class)) return true;
        if(PageSection.class.isAssignableFrom(fieldType)) return true;

        if(hasSeleniumFindByAnnotation(field)) return true;

        return false;
    }


    @SuppressWarnings("RedundantIfStatement")
    private static boolean hasSeleniumFindByAnnotation(Field field) {
        if(field.getAnnotation(FindBy.class) != null) return true;
        if(field.getAnnotation(FindBys.class) != null) return true;
        if(field.getAnnotation(FindAll.class) != null) return true;

        return false;
    }

    public static boolean isValidWebElement(Field field) {
        return !PageElement.class.isAssignableFrom(field.getType()) &&
                WebElement.class.isAssignableFrom(field.getType());
    }

    public static boolean isValidWebElementList(Field field) {
        Class<?> fieldType = field.getType();
        if (!List.class.isAssignableFrom(fieldType)) return false;
        Type genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) return false;
        ParameterizedType genericTypeImpl = (ParameterizedType) genericType;
        if(!(genericTypeImpl.getActualTypeArguments()[0] instanceof Class<?>)) return false;

        if (!WebElement.class.isAssignableFrom((Class<?>)genericTypeImpl.getActualTypeArguments()[0])) return false;

        return true;
    }

    public static boolean isSeleniumPomListField(Field field) {
        return isValidPageElementList(field) ||
                isValidWebElementList(field) ||
                isValidPageSectionList(field);
    }

    public static boolean isSeleniumPomNonListField(Field field) {
        return isValidPageElement(field) ||
                isValidPageSection(field) ||
                isValidWebElement(field);
    }

    public static boolean isSeleniumPomField(Field field) {
        return isValidPageElement(field) ||
                isValidPageSection(field) ||
                isValidPageElementList(field) ||
                isValidPageSectionList(field) ||
                isValidWebElement(field) ||
                isValidWebElementList(field);
    }
}
