package sun.reflect;

import java.lang.reflect.Field;

/* loaded from: rt.jar:sun/reflect/UnsafeQualifiedStaticDoubleFieldAccessorImpl.class */
class UnsafeQualifiedStaticDoubleFieldAccessorImpl extends UnsafeQualifiedStaticFieldAccessorImpl {
    UnsafeQualifiedStaticDoubleFieldAccessorImpl(Field field, boolean z2) {
        super(field, z2);
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public Object get(Object obj) throws IllegalArgumentException {
        return new Double(getDouble(obj));
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public boolean getBoolean(Object obj) throws IllegalArgumentException {
        throw newGetBooleanIllegalArgumentException();
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public byte getByte(Object obj) throws IllegalArgumentException {
        throw newGetByteIllegalArgumentException();
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public char getChar(Object obj) throws IllegalArgumentException {
        throw newGetCharIllegalArgumentException();
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public short getShort(Object obj) throws IllegalArgumentException {
        throw newGetShortIllegalArgumentException();
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public int getInt(Object obj) throws IllegalArgumentException {
        throw newGetIntIllegalArgumentException();
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public long getLong(Object obj) throws IllegalArgumentException {
        throw newGetLongIllegalArgumentException();
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public float getFloat(Object obj) throws IllegalArgumentException {
        throw newGetFloatIllegalArgumentException();
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public double getDouble(Object obj) throws IllegalArgumentException {
        return unsafe.getDoubleVolatile(this.base, this.fieldOffset);
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public void set(Object obj, Object obj2) throws IllegalAccessException, IllegalArgumentException {
        if (this.isReadOnly) {
            throwFinalFieldIllegalAccessException(obj2);
        }
        if (obj2 == null) {
            throwSetIllegalArgumentException(obj2);
        }
        if (obj2 instanceof Byte) {
            unsafe.putDoubleVolatile(this.base, this.fieldOffset, ((Byte) obj2).byteValue());
            return;
        }
        if (obj2 instanceof Short) {
            unsafe.putDoubleVolatile(this.base, this.fieldOffset, ((Short) obj2).shortValue());
            return;
        }
        if (obj2 instanceof Character) {
            unsafe.putDoubleVolatile(this.base, this.fieldOffset, ((Character) obj2).charValue());
            return;
        }
        if (obj2 instanceof Integer) {
            unsafe.putDoubleVolatile(this.base, this.fieldOffset, ((Integer) obj2).intValue());
            return;
        }
        if (obj2 instanceof Long) {
            unsafe.putDoubleVolatile(this.base, this.fieldOffset, ((Long) obj2).longValue());
            return;
        }
        if (obj2 instanceof Float) {
            unsafe.putDoubleVolatile(this.base, this.fieldOffset, ((Float) obj2).floatValue());
        } else if (obj2 instanceof Double) {
            unsafe.putDoubleVolatile(this.base, this.fieldOffset, ((Double) obj2).doubleValue());
        } else {
            throwSetIllegalArgumentException(obj2);
        }
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public void setBoolean(Object obj, boolean z2) throws IllegalAccessException, IllegalArgumentException {
        throwSetIllegalArgumentException(z2);
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public void setByte(Object obj, byte b2) throws IllegalAccessException, IllegalArgumentException {
        setDouble(obj, b2);
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public void setChar(Object obj, char c2) throws IllegalAccessException, IllegalArgumentException {
        setDouble(obj, c2);
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public void setShort(Object obj, short s2) throws IllegalAccessException, IllegalArgumentException {
        setDouble(obj, s2);
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public void setInt(Object obj, int i2) throws IllegalAccessException, IllegalArgumentException {
        setDouble(obj, i2);
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public void setLong(Object obj, long j2) throws IllegalAccessException, IllegalArgumentException {
        setDouble(obj, j2);
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public void setFloat(Object obj, float f2) throws IllegalAccessException, IllegalArgumentException {
        setDouble(obj, f2);
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public void setDouble(Object obj, double d2) throws IllegalAccessException, IllegalArgumentException {
        if (this.isReadOnly) {
            throwFinalFieldIllegalAccessException(d2);
        }
        unsafe.putDoubleVolatile(this.base, this.fieldOffset, d2);
    }
}
