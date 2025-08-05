package sun.management.counter;

import java.io.Serializable;

/* loaded from: rt.jar:sun/management/counter/Variability.class */
public class Variability implements Serializable {
    private static final int NATTRIBUTES = 4;
    private String name;
    private int value;
    private static final long serialVersionUID = 6992337162326171013L;
    private static Variability[] map = new Variability[4];
    public static final Variability INVALID = new Variability("Invalid", 0);
    public static final Variability CONSTANT = new Variability("Constant", 1);
    public static final Variability MONOTONIC = new Variability("Monotonic", 2);
    public static final Variability VARIABLE = new Variability("Variable", 3);

    public String toString() {
        return this.name;
    }

    public int intValue() {
        return this.value;
    }

    public static Variability toVariability(int i2) {
        if (i2 < 0 || i2 >= map.length || map[i2] == null) {
            return INVALID;
        }
        return map[i2];
    }

    private Variability(String str, int i2) {
        this.name = str;
        this.value = i2;
        map[i2] = this;
    }
}
