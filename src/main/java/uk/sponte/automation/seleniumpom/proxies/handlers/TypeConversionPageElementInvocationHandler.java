package uk.sponte.automation.seleniumpom.proxies.handlers;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import uk.sponte.automation.seleniumpom.PageElement;
import uk.sponte.automation.seleniumpom.typeconverters.TypeConverter;

import java.lang.reflect.Method;

/**
 * Created by n450777 on 27/11/2015.
 * Proxy handler for pageelements wrapped in type converters
 */
public class TypeConversionPageElementInvocationHandler implements
        MethodInterceptor {

    private PageElement pageElement;

    private TypeConverter typeConverter;

    public TypeConversionPageElementInvocationHandler(
            PageElement pageElement, TypeConverter typeConverter) {

        this.pageElement = pageElement;
        this.typeConverter = typeConverter;
    }


    @Override
    public Object intercept(Object o, Method method, Object[] objects,
            MethodProxy methodProxy) throws Throwable {
        return typeConverter.convert(pageElement.getText());
    }
}
