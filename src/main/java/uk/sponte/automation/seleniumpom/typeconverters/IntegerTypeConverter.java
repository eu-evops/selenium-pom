package uk.sponte.automation.seleniumpom.typeconverters;

/**
 * Created by n450777 on 27/11/2015.
 * Converts strings into integers
 */
public class IntegerTypeConverter implements TypeConverter<Integer> {
    @Override
    public Integer convert(String text) {
        return Integer.parseInt(text);
    }
}
