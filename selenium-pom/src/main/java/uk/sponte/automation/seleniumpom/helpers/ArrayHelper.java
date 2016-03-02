package uk.sponte.automation.seleniumpom.helpers;

import java.util.Collection;

/**
 * Created by evops on 08/02/2016.
 */
public class ArrayHelper {
    public static <T> String join(Iterable<T> aArr, String sSep) {
        StringBuilder sbStr = new StringBuilder();

        boolean first = true;
        for (Object o : aArr) {
            if(!first) sbStr.append(sSep);
            first = false;
            sbStr.append(o.toString());
        }

        return sbStr.toString();
    }
}
