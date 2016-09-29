package uk.sponte.automation.seleniumpom.guice;

import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import uk.sponte.automation.seleniumpom.fieldInitialisers.FieldAssessor;
import uk.sponte.automation.seleniumpom.helpers.ReflectionHelper;

import java.lang.reflect.Field;

/**
 * Created by n450777 on 09/04/15.
 */
class PageObjectModelTypeListener implements TypeListener {
    public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
        if (isPageObject(typeLiteral.getRawType()))
            typeEncounter.register(new PageObjectModelInjectionListener(typeEncounter.getProvider(Injector.class)));
    }

    private boolean isPageObject(Class<?> rawType) {
        for (Field field : ReflectionHelper.getAllClassFields(rawType)) {
            if(FieldAssessor.isSeleniumPomField(field)) return true;
        }

        return false;
    }
}
