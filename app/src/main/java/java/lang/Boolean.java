package java.lang;

import java.io.Serializable;

/* loaded from: rt.jar:java/lang/Boolean.class */
public final class Boolean implements Serializable, Comparable<Boolean> {
    public static final Boolean TRUE = new Boolean(true);
    public static final Boolean FALSE = new Boolean(false);
    public static final Class<Boolean> TYPE = Class.getPrimitiveClass("boolean");
    private final boolean value;
    private static final long serialVersionUID = -3665804199014368530L;

    public Boolean(boolean z2) {
        this.value = z2;
    }

    public Boolean(String str) {
        this(parseBoolean(str));
    }

    public static boolean parseBoolean(String str) {
        return str != null && str.equalsIgnoreCase("true");
    }

    public boolean booleanValue() {
        return this.value;
    }

    public static Boolean valueOf(boolean z2) {
        return z2 ? TRUE : FALSE;
    }

    public static Boolean valueOf(String str) {
        return parseBoolean(str) ? TRUE : FALSE;
    }

    public static String toString(boolean z2) {
        return z2 ? "true" : "false";
    }

    public String toString() {
        return this.value ? "true" : "false";
    }

    public int hashCode() {
        return hashCode(this.value);
    }

    public static int hashCode(boolean z2) {
        return z2 ? 1231 : 1237;
    }

    public boolean equals(Object obj) {
        return (obj instanceof Boolean) && this.value == ((Boolean) obj).booleanValue();
    }

    public static boolean getBoolean(String str) {
        boolean z2 = false;
        try {
            z2 = parseBoolean(System.getProperty(str));
        } catch (IllegalArgumentException | NullPointerException e2) {
        }
        return z2;
    }

    @Override // java.lang.Comparable
    public int compareTo(Boolean bool) {
        return compare(this.value, bool.value);
    }

    public static int compare(boolean z2, boolean z3) {
        if (z2 == z3) {
            return 0;
        }
        return z2 ? 1 : -1;
    }

    public static boolean logicalAnd(boolean z2, boolean z3) {
        return z2 && z3;
    }

    public static boolean logicalOr(boolean z2, boolean z3) {
        return z2 || z3;
    }

    public static boolean logicalXor(boolean z2, boolean z3) {
        return z2 ^ z3;
    }
}
