package jdk.internal.util;

/* loaded from: rt.jar:jdk/internal/util/StaticProperty.class */
public final class StaticProperty {
    private static final String JDK_SERIAL_FILTER = System.getProperty("jdk.serialFilter");

    private StaticProperty() {
    }

    public static String jdkSerialFilter() {
        return JDK_SERIAL_FILTER;
    }
}
