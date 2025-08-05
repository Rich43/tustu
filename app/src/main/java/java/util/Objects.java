package java.util;

import java.util.function.Supplier;

/* loaded from: rt.jar:java/util/Objects.class */
public final class Objects {
    private Objects() {
        throw new AssertionError((Object) "No java.util.Objects instances for you!");
    }

    public static boolean equals(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    public static boolean deepEquals(Object obj, Object obj2) {
        if (obj == obj2) {
            return true;
        }
        if (obj == null || obj2 == null) {
            return false;
        }
        return Arrays.deepEquals0(obj, obj2);
    }

    public static int hashCode(Object obj) {
        if (obj != null) {
            return obj.hashCode();
        }
        return 0;
    }

    public static int hash(Object... objArr) {
        return Arrays.hashCode(objArr);
    }

    public static String toString(Object obj) {
        return String.valueOf(obj);
    }

    public static String toString(Object obj, String str) {
        return obj != null ? obj.toString() : str;
    }

    public static <T> int compare(T t2, T t3, Comparator<? super T> comparator) {
        if (t2 == t3) {
            return 0;
        }
        return comparator.compare(t2, t3);
    }

    public static <T> T requireNonNull(T t2) {
        if (t2 == null) {
            throw new NullPointerException();
        }
        return t2;
    }

    public static <T> T requireNonNull(T t2, String str) {
        if (t2 == null) {
            throw new NullPointerException(str);
        }
        return t2;
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean nonNull(Object obj) {
        return obj != null;
    }

    public static <T> T requireNonNull(T t2, Supplier<String> supplier) {
        if (t2 == null) {
            throw new NullPointerException(supplier.get());
        }
        return t2;
    }
}
