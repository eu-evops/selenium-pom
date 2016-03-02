package uk.sponte.automation.seleniumpom.guice;

import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import uk.sponte.automation.seleniumpom.PageElement;
import uk.sponte.automation.seleniumpom.annotations.Section;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by n450777 on 09/04/15.
 */
class PageObjectModelTypeListener implements TypeListener {
    public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
        if (isPageObject(typeLiteral.getRawType()))
            typeEncounter.register(new PageObjectModelInjectionListener(typeEncounter.getProvider(Injector.class)));
    }

    private boolean isPageObject(Class<?> rawType) {
        for (Field field : rawType.getFields()) {
            Class<?> type = field.getType();
            if(PageElement.class.isAssignableFrom(type)) return true;
            if(field.isAnnotationPresent(Section.class)) return true;
            if(field.isAnnotationPresent(FindBy.class)) return true;
            if(field.isAnnotationPresent(FindBys.class)) return true;
            if(field.isAnnotationPresent(FindAll.class)) return true;

            if(List.class.isAssignableFrom(type)) {
                Type genericType = field.getGenericType();
                if(genericType instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) genericType;
                    if(!(parameterizedType.getActualTypeArguments()[0] instanceof Class)) return false;
                    Class genericTypeArgument = (Class<?>)parameterizedType.getActualTypeArguments()[0];
                    if(PageElement.class.isAssignableFrom(genericTypeArgument)) return true;
                }
            }
        }

        return false;
    }
}
