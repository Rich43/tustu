package sun.management.counter;

import java.io.Serializable;

/* loaded from: rt.jar:sun/management/counter/Units.class */
public class Units implements Serializable {
    private static final int NUNITS = 8;
    private final String name;
    private final int value;
    private static final long serialVersionUID = 6992337162326171013L;
    private static Units[] map = new Units[8];
    public static final Units INVALID = new Units("Invalid", 0);
    public static final Units NONE = new Units("None", 1);
    public static final Units BYTES = new Units("Bytes", 2);
    public static final Units TICKS = new Units("Ticks", 3);
    public static final Units EVENTS = new Units("Events", 4);
    public static final Units STRING = new Units("String", 5);
    public static final Units HERTZ = new Units("Hertz", 6);

    public String toString() {
        return this.name;
    }

    public int intValue() {
        return this.value;
    }

    public static Units toUnits(int i2) {
        if (i2 < 0 || i2 >= map.length || map[i2] == null) {
            return INVALID;
        }
        return map[i2];
    }

    private Units(String str, int i2) {
        this.name = str;
        this.value = i2;
        map[i2] = this;
    }
}
