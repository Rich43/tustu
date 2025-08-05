package sun.reflect;

/* loaded from: rt.jar:sun/reflect/FieldAccessorImpl.class */
abstract class FieldAccessorImpl extends MagicAccessorImpl implements FieldAccessor {
    @Override // sun.reflect.FieldAccessor
    public abstract Object get(Object obj) throws IllegalArgumentException;

    @Override // sun.reflect.FieldAccessor
    public abstract boolean getBoolean(Object obj) throws IllegalArgumentException;

    @Override // sun.reflect.FieldAccessor
    public abstract byte getByte(Object obj) throws IllegalArgumentException;

    @Override // sun.reflect.FieldAccessor
    public abstract char getChar(Object obj) throws IllegalArgumentException;

    @Override // sun.reflect.FieldAccessor
    public abstract short getShort(Object obj) throws IllegalArgumentException;

    @Override // sun.reflect.FieldAccessor
    public abstract int getInt(Object obj) throws IllegalArgumentException;

    @Override // sun.reflect.FieldAccessor
    public abstract long getLong(Object obj) throws IllegalArgumentException;

    @Override // sun.reflect.FieldAccessor
    public abstract float getFloat(Object obj) throws IllegalArgumentException;

    @Override // sun.reflect.FieldAccessor
    public abstract double getDouble(Object obj) throws IllegalArgumentException;

    @Override // sun.reflect.FieldAccessor
    public abstract void set(Object obj, Object obj2) throws IllegalAccessException, IllegalArgumentException;

    @Override // sun.reflect.FieldAccessor
    public abstract void setBoolean(Object obj, boolean z2) throws IllegalAccessException, IllegalArgumentException;

    @Override // sun.reflect.FieldAccessor
    public abstract void setByte(Object obj, byte b2) throws IllegalAccessException, IllegalArgumentException;

    @Override // sun.reflect.FieldAccessor
    public abstract void setChar(Object obj, char c2) throws IllegalAccessException, IllegalArgumentException;

    @Override // sun.reflect.FieldAccessor
    public abstract void setShort(Object obj, short s2) throws IllegalAccessException, IllegalArgumentException;

    @Override // sun.reflect.FieldAccessor
    public abstract void setInt(Object obj, int i2) throws IllegalAccessException, IllegalArgumentException;

    @Override // sun.reflect.FieldAccessor
    public abstract void setLong(Object obj, long j2) throws IllegalAccessException, IllegalArgumentException;

    @Override // sun.reflect.FieldAccessor
    public abstract void setFloat(Object obj, float f2) throws IllegalAccessException, IllegalArgumentException;

    @Override // sun.reflect.FieldAccessor
    public abstract void setDouble(Object obj, double d2) throws IllegalAccessException, IllegalArgumentException;

    FieldAccessorImpl() {
    }
}
