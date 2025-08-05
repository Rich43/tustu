package jdk.internal.org.objectweb.asm;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/Item.class */
final class Item {
    int index;
    int type;
    int intVal;
    long longVal;
    String strVal1;
    String strVal2;
    String strVal3;
    int hashCode;
    Item next;

    Item() {
    }

    Item(int i2) {
        this.index = i2;
    }

    Item(int i2, Item item) {
        this.index = i2;
        this.type = item.type;
        this.intVal = item.intVal;
        this.longVal = item.longVal;
        this.strVal1 = item.strVal1;
        this.strVal2 = item.strVal2;
        this.strVal3 = item.strVal3;
        this.hashCode = item.hashCode;
    }

    void set(int i2) {
        this.type = 3;
        this.intVal = i2;
        this.hashCode = Integer.MAX_VALUE & (this.type + i2);
    }

    void set(long j2) {
        this.type = 5;
        this.longVal = j2;
        this.hashCode = Integer.MAX_VALUE & (this.type + ((int) j2));
    }

    void set(float f2) {
        this.type = 4;
        this.intVal = Float.floatToRawIntBits(f2);
        this.hashCode = Integer.MAX_VALUE & (this.type + ((int) f2));
    }

    void set(double d2) {
        this.type = 6;
        this.longVal = Double.doubleToRawLongBits(d2);
        this.hashCode = Integer.MAX_VALUE & (this.type + ((int) d2));
    }

    void set(int i2, String str, String str2, String str3) {
        this.type = i2;
        this.strVal1 = str;
        this.strVal2 = str2;
        this.strVal3 = str3;
        switch (i2) {
            case 1:
            case 8:
            case 16:
            case 30:
                break;
            case 7:
                this.intVal = 0;
                break;
            case 12:
                this.hashCode = Integer.MAX_VALUE & (i2 + (str.hashCode() * str2.hashCode()));
                return;
            default:
                this.hashCode = Integer.MAX_VALUE & (i2 + (str.hashCode() * str2.hashCode() * str3.hashCode()));
                return;
        }
        this.hashCode = Integer.MAX_VALUE & (i2 + str.hashCode());
    }

    void set(String str, String str2, int i2) {
        this.type = 18;
        this.longVal = i2;
        this.strVal1 = str;
        this.strVal2 = str2;
        this.hashCode = Integer.MAX_VALUE & (18 + (i2 * this.strVal1.hashCode() * this.strVal2.hashCode()));
    }

    void set(int i2, int i3) {
        this.type = 33;
        this.intVal = i2;
        this.hashCode = i3;
    }

    boolean isEqualTo(Item item) {
        switch (this.type) {
            case 1:
            case 7:
            case 8:
            case 16:
            case 30:
                return item.strVal1.equals(this.strVal1);
            case 2:
            case 9:
            case 10:
            case 11:
            case 13:
            case 14:
            case 15:
            case 17:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            default:
                return item.strVal1.equals(this.strVal1) && item.strVal2.equals(this.strVal2) && item.strVal3.equals(this.strVal3);
            case 3:
            case 4:
                return item.intVal == this.intVal;
            case 5:
            case 6:
            case 32:
                return item.longVal == this.longVal;
            case 12:
                return item.strVal1.equals(this.strVal1) && item.strVal2.equals(this.strVal2);
            case 18:
                return item.longVal == this.longVal && item.strVal1.equals(this.strVal1) && item.strVal2.equals(this.strVal2);
            case 31:
                return item.intVal == this.intVal && item.strVal1.equals(this.strVal1);
        }
    }
}
