package sun.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Member;

/* loaded from: rt.jar:sun/reflect/ConstantPool.class */
public class ConstantPool {
    private Object constantPoolOop;

    private native int getSize0(Object obj);

    private native Class<?> getClassAt0(Object obj, int i2);

    private native Class<?> getClassAtIfLoaded0(Object obj, int i2);

    private native Member getMethodAt0(Object obj, int i2);

    private native Member getMethodAtIfLoaded0(Object obj, int i2);

    private native Field getFieldAt0(Object obj, int i2);

    private native Field getFieldAtIfLoaded0(Object obj, int i2);

    private native String[] getMemberRefInfoAt0(Object obj, int i2);

    private native int getIntAt0(Object obj, int i2);

    private native long getLongAt0(Object obj, int i2);

    private native float getFloatAt0(Object obj, int i2);

    private native double getDoubleAt0(Object obj, int i2);

    private native String getStringAt0(Object obj, int i2);

    private native String getUTF8At0(Object obj, int i2);

    public int getSize() {
        return getSize0(this.constantPoolOop);
    }

    public Class<?> getClassAt(int i2) {
        return getClassAt0(this.constantPoolOop, i2);
    }

    public Class<?> getClassAtIfLoaded(int i2) {
        return getClassAtIfLoaded0(this.constantPoolOop, i2);
    }

    public Member getMethodAt(int i2) {
        return getMethodAt0(this.constantPoolOop, i2);
    }

    public Member getMethodAtIfLoaded(int i2) {
        return getMethodAtIfLoaded0(this.constantPoolOop, i2);
    }

    public Field getFieldAt(int i2) {
        return getFieldAt0(this.constantPoolOop, i2);
    }

    public Field getFieldAtIfLoaded(int i2) {
        return getFieldAtIfLoaded0(this.constantPoolOop, i2);
    }

    public String[] getMemberRefInfoAt(int i2) {
        return getMemberRefInfoAt0(this.constantPoolOop, i2);
    }

    public int getIntAt(int i2) {
        return getIntAt0(this.constantPoolOop, i2);
    }

    public long getLongAt(int i2) {
        return getLongAt0(this.constantPoolOop, i2);
    }

    public float getFloatAt(int i2) {
        return getFloatAt0(this.constantPoolOop, i2);
    }

    public double getDoubleAt(int i2) {
        return getDoubleAt0(this.constantPoolOop, i2);
    }

    public String getStringAt(int i2) {
        return getStringAt0(this.constantPoolOop, i2);
    }

    public String getUTF8At(int i2) {
        return getUTF8At0(this.constantPoolOop, i2);
    }

    static {
        Reflection.registerFieldsToFilter(ConstantPool.class, "constantPoolOop");
    }
}
