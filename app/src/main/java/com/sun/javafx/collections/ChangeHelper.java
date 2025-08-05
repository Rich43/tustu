package com.sun.javafx.collections;

import java.util.Arrays;
import java.util.List;

/* loaded from: jfxrt.jar:com/sun/javafx/collections/ChangeHelper.class */
public class ChangeHelper {
    public static String addRemoveChangeToString(int from, int to, List<?> list, List<?> removed) {
        StringBuilder b2 = new StringBuilder();
        if (removed.isEmpty()) {
            b2.append((Object) list.subList(from, to));
            b2.append(" added at ").append(from);
        } else {
            b2.append((Object) removed);
            if (from == to) {
                b2.append(" removed at ").append(from);
            } else {
                b2.append(" replaced by ");
                b2.append((Object) list.subList(from, to));
                b2.append(" at ").append(from);
            }
        }
        return b2.toString();
    }

    public static String permChangeToString(int[] permutation) {
        return "permutated by " + Arrays.toString(permutation);
    }

    public static String updateChangeToString(int from, int to) {
        return "updated at range [" + from + ", " + to + ")";
    }
}
