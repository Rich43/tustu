package java.util.logging;

/* loaded from: rt.jar:java/util/logging/ErrorManager.class */
public class ErrorManager {
    private boolean reported = false;
    public static final int GENERIC_FAILURE = 0;
    public static final int WRITE_FAILURE = 1;
    public static final int FLUSH_FAILURE = 2;
    public static final int CLOSE_FAILURE = 3;
    public static final int OPEN_FAILURE = 4;
    public static final int FORMAT_FAILURE = 5;

    public synchronized void error(String str, Exception exc, int i2) {
        if (this.reported) {
            return;
        }
        this.reported = true;
        String str2 = "java.util.logging.ErrorManager: " + i2;
        if (str != null) {
            str2 = str2 + ": " + str;
        }
        System.err.println(str2);
        if (exc != null) {
            exc.printStackTrace();
        }
    }
}
