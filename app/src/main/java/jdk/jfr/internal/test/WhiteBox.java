package jdk.jfr.internal.test;

/* loaded from: jfr.jar:jdk/jfr/internal/test/WhiteBox.class */
public final class WhiteBox {
    private static boolean writeAllObjectSamples;

    public static void setWriteAllObjectSamples(boolean z2) {
        writeAllObjectSamples = z2;
    }

    public static boolean getWriteAllObjectSamples() {
        return writeAllObjectSamples;
    }
}
