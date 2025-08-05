package org.apache.commons.math3.exception.util;

import java.util.ArrayList;
import java.util.List;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/exception/util/ArgUtils.class */
public class ArgUtils {
    private ArgUtils() {
    }

    public static Object[] flatten(Object[] array) {
        List<Object> list = new ArrayList<>();
        if (array != null) {
            for (Object o2 : array) {
                if (o2 instanceof Object[]) {
                    Object[] arr$ = flatten((Object[]) o2);
                    for (Object oR : arr$) {
                        list.add(oR);
                    }
                } else {
                    list.add(o2);
                }
            }
        }
        return list.toArray();
    }
}
