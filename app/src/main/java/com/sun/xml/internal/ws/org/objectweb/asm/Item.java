package com.sun.xml.internal.ws.org.objectweb.asm;

/* loaded from: rt.jar:com/sun/xml/internal/ws/org/objectweb/asm/Item.class */
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

    Item(int index) {
        this.index = index;
    }

    Item(int index, Item i2) {
        this.index = index;
        this.type = i2.type;
        this.intVal = i2.intVal;
        this.longVal = i2.longVal;
        this.strVal1 = i2.strVal1;
        this.strVal2 = i2.strVal2;
        this.strVal3 = i2.strVal3;
        this.hashCode = i2.hashCode;
    }

    void set(int intVal) {
        this.type = 3;
        this.intVal = intVal;
        this.hashCode = Integer.MAX_VALUE & (this.type + intVal);
    }

    void set(long longVal) {
        this.type = 5;
        this.longVal = longVal;
        this.hashCode = Integer.MAX_VALUE & (this.type + ((int) longVal));
    }

    void set(float floatVal) {
        this.type = 4;
        this.intVal = Float.floatToRawIntBits(floatVal);
        this.hashCode = Integer.MAX_VALUE & (this.type + ((int) floatVal));
    }

    void set(double doubleVal) {
        this.type = 6;
        this.longVal = Double.doubleToRawLongBits(doubleVal);
        this.hashCode = Integer.MAX_VALUE & (this.type + ((int) doubleVal));
    }

    void set(int type, String strVal1, String strVal2, String strVal3) {
        this.type = type;
        this.strVal1 = strVal1;
        this.strVal2 = strVal2;
        this.strVal3 = strVal3;
        switch (type) {
            case 1:
            case 7:
            case 8:
            case 13:
                this.hashCode = Integer.MAX_VALUE & (type + strVal1.hashCode());
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 9:
            case 10:
            case 11:
            default:
                this.hashCode = Integer.MAX_VALUE & (type + (strVal1.hashCode() * strVal2.hashCode() * strVal3.hashCode()));
                break;
            case 12:
                this.hashCode = Integer.MAX_VALUE & (type + (strVal1.hashCode() * strVal2.hashCode()));
                break;
        }
    }

    boolean isEqualTo(Item i2) {
        if (i2.type == this.type) {
            switch (this.type) {
                case 1:
                case 7:
                case 8:
                case 13:
                    return i2.strVal1.equals(this.strVal1);
                case 2:
                case 9:
                case 10:
                case 11:
                default:
                    return i2.strVal1.equals(this.strVal1) && i2.strVal2.equals(this.strVal2) && i2.strVal3.equals(this.strVal3);
                case 3:
                case 4:
                    return i2.intVal == this.intVal;
                case 5:
                case 6:
                case 15:
                    return i2.longVal == this.longVal;
                case 12:
                    return i2.strVal1.equals(this.strVal1) && i2.strVal2.equals(this.strVal2);
                case 14:
                    return i2.intVal == this.intVal && i2.strVal1.equals(this.strVal1);
            }
        }
        return false;
    }
}
