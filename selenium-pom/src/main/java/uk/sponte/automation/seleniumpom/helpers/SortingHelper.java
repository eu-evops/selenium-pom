package uk.sponte.automation.seleniumpom.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Created by evops on 05/02/2016.
 */
public class SortingHelper {
    public static <T> List<T> asSortedList(Collection<T> c, Comparator<T> comparator) {
        List<T> list = new ArrayList<T>(c);
        java.util.Collections.sort(list, comparator);
        return list;
    }
}
