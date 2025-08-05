package sun.reflect;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import sun.misc.Unsafe;

/* loaded from: rt.jar:sun/reflect/UnsafeFieldAccessorImpl.class */
abstract class UnsafeFieldAccessorImpl extends FieldAccessorImpl {
    static final Unsafe unsafe = Unsafe.getUnsafe();
    protected final Field field;
    protected final long fieldOffset;
    protected final boolean isFinal;

    UnsafeFieldAccessorImpl(Field field) {
        this.field = field;
        if (Modifier.isStatic(field.getModifiers())) {
            this.fieldOffset = unsafe.staticFieldOffset(field);
        } else {
            this.fieldOffset = unsafe.objectFieldOffset(field);
        }
        this.isFinal = Modifier.isFinal(field.getModifiers());
    }

    protected void ensureObj(Object obj) {
        if (!this.field.getDeclaringClass().isAssignableFrom(obj.getClass())) {
            throwSetIllegalArgumentException(obj);
        }
    }

    private String getQualifiedFieldName() {
        return this.field.getDeclaringClass().getName() + "." + this.field.getName();
    }

    protected IllegalArgumentException newGetIllegalArgumentException(String str) {
        return new IllegalArgumentException("Attempt to get " + this.field.getType().getName() + " field \"" + getQualifiedFieldName() + "\" with illegal data type conversion to " + str);
    }

    protected void throwFinalFieldIllegalAccessException(String str, String str2) throws IllegalAccessException {
        throw new IllegalAccessException(getSetMessage(str, str2));
    }

    protected void throwFinalFieldIllegalAccessException(Object obj) throws IllegalAccessException {
        throwFinalFieldIllegalAccessException(obj != null ? obj.getClass().getName() : "", "");
    }

    protected void throwFinalFieldIllegalAccessException(boolean z2) throws IllegalAccessException {
        throwFinalFieldIllegalAccessException("boolean", Boolean.toString(z2));
    }

    protected void throwFinalFieldIllegalAccessException(char c2) throws IllegalAccessException {
        throwFinalFieldIllegalAccessException("char", Character.toString(c2));
    }

    protected void throwFinalFieldIllegalAccessException(byte b2) throws IllegalAccessException {
        throwFinalFieldIllegalAccessException(SchemaSymbols.ATTVAL_BYTE, Byte.toString(b2));
    }

    protected void throwFinalFieldIllegalAccessException(short s2) throws IllegalAccessException {
        throwFinalFieldIllegalAccessException(SchemaSymbols.ATTVAL_SHORT, Short.toString(s2));
    }

    protected void throwFinalFieldIllegalAccessException(int i2) throws IllegalAccessException {
        throwFinalFieldIllegalAccessException("int", Integer.toString(i2));
    }

    protected void throwFinalFieldIllegalAccessException(long j2) throws IllegalAccessException {
        throwFinalFieldIllegalAccessException(SchemaSymbols.ATTVAL_LONG, Long.toString(j2));
    }

    protected void throwFinalFieldIllegalAccessException(float f2) throws IllegalAccessException {
        throwFinalFieldIllegalAccessException(SchemaSymbols.ATTVAL_FLOAT, Float.toString(f2));
    }

    protected void throwFinalFieldIllegalAccessException(double d2) throws IllegalAccessException {
        throwFinalFieldIllegalAccessException(SchemaSymbols.ATTVAL_DOUBLE, Double.toString(d2));
    }

    protected IllegalArgumentException newGetBooleanIllegalArgumentException() {
        return newGetIllegalArgumentException("boolean");
    }

    protected IllegalArgumentException newGetByteIllegalArgumentException() {
        return newGetIllegalArgumentException(SchemaSymbols.ATTVAL_BYTE);
    }

    protected IllegalArgumentException newGetCharIllegalArgumentException() {
        return newGetIllegalArgumentException("char");
    }

    protected IllegalArgumentException newGetShortIllegalArgumentException() {
        return newGetIllegalArgumentException(SchemaSymbols.ATTVAL_SHORT);
    }

    protected IllegalArgumentException newGetIntIllegalArgumentException() {
        return newGetIllegalArgumentException("int");
    }

    protected IllegalArgumentException newGetLongIllegalArgumentException() {
        return newGetIllegalArgumentException(SchemaSymbols.ATTVAL_LONG);
    }

    protected IllegalArgumentException newGetFloatIllegalArgumentException() {
        return newGetIllegalArgumentException(SchemaSymbols.ATTVAL_FLOAT);
    }

    protected IllegalArgumentException newGetDoubleIllegalArgumentException() {
        return newGetIllegalArgumentException(SchemaSymbols.ATTVAL_DOUBLE);
    }

    protected String getSetMessage(String str, String str2) {
        String str3;
        String str4 = "Can not set";
        if (Modifier.isStatic(this.field.getModifiers())) {
            str4 = str4 + " static";
        }
        if (this.isFinal) {
            str4 = str4 + " final";
        }
        String str5 = str4 + " " + this.field.getType().getName() + " field " + getQualifiedFieldName() + " to ";
        if (str2.length() > 0) {
            str3 = str5 + "(" + str + ")" + str2;
        } else if (str.length() > 0) {
            str3 = str5 + str;
        } else {
            str3 = str5 + "null value";
        }
        return str3;
    }

    protected void throwSetIllegalArgumentException(String str, String str2) {
        throw new IllegalArgumentException(getSetMessage(str, str2));
    }

    protected void throwSetIllegalArgumentException(Object obj) {
        throwSetIllegalArgumentException(obj != null ? obj.getClass().getName() : "", "");
    }

    protected void throwSetIllegalArgumentException(boolean z2) {
        throwSetIllegalArgumentException("boolean", Boolean.toString(z2));
    }

    protected void throwSetIllegalArgumentException(byte b2) {
        throwSetIllegalArgumentException(SchemaSymbols.ATTVAL_BYTE, Byte.toString(b2));
    }

    protected void throwSetIllegalArgumentException(char c2) {
        throwSetIllegalArgumentException("char", Character.toString(c2));
    }

    protected void throwSetIllegalArgumentException(short s2) {
        throwSetIllegalArgumentException(SchemaSymbols.ATTVAL_SHORT, Short.toString(s2));
    }

    protected void throwSetIllegalArgumentException(int i2) {
        throwSetIllegalArgumentException("int", Integer.toString(i2));
    }

    protected void throwSetIllegalArgumentException(long j2) {
        throwSetIllegalArgumentException(SchemaSymbols.ATTVAL_LONG, Long.toString(j2));
    }

    protected void throwSetIllegalArgumentException(float f2) {
        throwSetIllegalArgumentException(SchemaSymbols.ATTVAL_FLOAT, Float.toString(f2));
    }

    protected void throwSetIllegalArgumentException(double d2) {
        throwSetIllegalArgumentException(SchemaSymbols.ATTVAL_DOUBLE, Double.toString(d2));
    }
}
