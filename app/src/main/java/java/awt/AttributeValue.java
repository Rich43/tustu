package java.awt;

import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:java/awt/AttributeValue.class */
abstract class AttributeValue {
    private static final PlatformLogger log = PlatformLogger.getLogger("java.awt.AttributeValue");
    private final int value;
    private final String[] names;

    protected AttributeValue(int i2, String[] strArr) {
        if (log.isLoggable(PlatformLogger.Level.FINEST)) {
            log.finest("value = " + i2 + ", names = " + ((Object) strArr));
        }
        if (log.isLoggable(PlatformLogger.Level.FINER) && (i2 < 0 || strArr == null || i2 >= strArr.length)) {
            log.finer("Assertion failed");
        }
        this.value = i2;
        this.names = strArr;
    }

    public int hashCode() {
        return this.value;
    }

    public String toString() {
        return this.names[this.value];
    }
}
