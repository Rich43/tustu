package com.sun.crypto.provider;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/Preconditions.class */
public class Preconditions {
    private static RuntimeException outOfBounds(BiFunction<String, List<Integer>, ? extends RuntimeException> biFunction, String str, Integer... numArr) {
        List<Integer> listUnmodifiableList = Collections.unmodifiableList(Arrays.asList(numArr));
        RuntimeException runtimeExceptionApply = biFunction == null ? null : biFunction.apply(str, listUnmodifiableList);
        return runtimeExceptionApply == null ? new IndexOutOfBoundsException(outOfBoundsMessage(str, listUnmodifiableList)) : runtimeExceptionApply;
    }

    private static RuntimeException outOfBoundsCheckIndex(BiFunction<String, List<Integer>, ? extends RuntimeException> biFunction, int i2, int i3) {
        return outOfBounds(biFunction, "checkIndex", Integer.valueOf(i2), Integer.valueOf(i3));
    }

    private static RuntimeException outOfBoundsCheckFromToIndex(BiFunction<String, List<Integer>, ? extends RuntimeException> biFunction, int i2, int i3, int i4) {
        return outOfBounds(biFunction, "checkFromToIndex", Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4));
    }

    private static RuntimeException outOfBoundsCheckFromIndexSize(BiFunction<String, List<Integer>, ? extends RuntimeException> biFunction, int i2, int i3, int i4) {
        return outOfBounds(biFunction, "checkFromIndexSize", Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4));
    }

    public static <X extends RuntimeException> BiFunction<String, List<Integer>, X> outOfBoundsExceptionFormatter(final Function<String, X> function) {
        return (BiFunction<String, List<Integer>, X>) new BiFunction<String, List<Integer>, X>() { // from class: com.sun.crypto.provider.Preconditions.1
            @Override // java.util.function.BiFunction
            public /* bridge */ /* synthetic */ Object apply(String str, List<Integer> list) {
                return apply2(str, (List) list);
            }

            /* JADX WARN: Incorrect return type in method signature: (Ljava/lang/String;Ljava/util/List<Ljava/lang/Integer;>;)TX; */
            /* renamed from: apply, reason: avoid collision after fix types in other method */
            public RuntimeException apply2(String str, List list) {
                return (RuntimeException) function.apply(Preconditions.outOfBoundsMessage(str, list));
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String outOfBoundsMessage(String str, List<Integer> list) {
        int i2;
        if (str == null && list == null) {
            return String.format("Range check failed", new Object[0]);
        }
        if (str == null) {
            return String.format("Range check failed: %s", list);
        }
        if (list == null) {
            return String.format("Range check failed: %s", str);
        }
        i2 = 0;
        switch (str) {
            case "checkIndex":
                i2 = 2;
                break;
            case "checkFromToIndex":
            case "checkFromIndexSize":
                i2 = 3;
                break;
        }
        switch (list.size() != i2 ? "" : str) {
            case "checkIndex":
                return String.format("Index %d out-of-bounds for length %d", list.get(0), list.get(1));
            case "checkFromToIndex":
                return String.format("Range [%d, %d) out-of-bounds for length %d", list.get(0), list.get(1), list.get(2));
            case "checkFromIndexSize":
                return String.format("Range [%d, %<d + %d) out-of-bounds for length %d", list.get(0), list.get(1), list.get(2));
            default:
                return String.format("Range check failed: %s %s", str, list);
        }
    }

    public static <X extends RuntimeException> int checkIndex(int i2, int i3, BiFunction<String, List<Integer>, X> biFunction) {
        if (i2 < 0 || i2 >= i3) {
            throw outOfBoundsCheckIndex(biFunction, i2, i3);
        }
        return i2;
    }

    public static <X extends RuntimeException> int checkFromToIndex(int i2, int i3, int i4, BiFunction<String, List<Integer>, X> biFunction) {
        if (i2 < 0 || i2 > i3 || i3 > i4) {
            throw outOfBoundsCheckFromToIndex(biFunction, i2, i3, i4);
        }
        return i2;
    }

    public static <X extends RuntimeException> int checkFromIndexSize(int i2, int i3, int i4, BiFunction<String, List<Integer>, X> biFunction) {
        if ((i4 | i2 | i3) < 0 || i3 > i4 - i2) {
            throw outOfBoundsCheckFromIndexSize(biFunction, i2, i3, i4);
        }
        return i2;
    }
}
