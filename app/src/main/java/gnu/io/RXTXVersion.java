package gnu.io;

/* loaded from: RXTXcomm.jar:gnu/io/RXTXVersion.class */
public class RXTXVersion {
    private static String Version;

    public static native String nativeGetVersion();

    static {
        System.loadLibrary("rxtxSerial");
        Version = "RXTX-2.2pre1";
    }

    public static String getVersion() {
        return Version;
    }
}
