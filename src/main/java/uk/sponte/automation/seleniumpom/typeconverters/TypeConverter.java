package uk.sponte.automation.seleniumpom.typeconverters;

/**
 * Created by n450777 on 27/11/2015.
 * Type converter interface used for converting text into other types
 */
public interface TypeConverter<T> {
    T convert(String text);
}
