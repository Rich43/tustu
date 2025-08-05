package sun.reflect;

import java.lang.reflect.Field;

/* loaded from: rt.jar:sun/reflect/UnsafeStaticShortFieldAccessorImpl.class */
class UnsafeStaticShortFieldAccessorImpl extends UnsafeStaticFieldAccessorImpl {
    UnsafeStaticShortFieldAccessorImpl(Field field) {
        super(field);
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public Object get(Object obj) throws IllegalArgumentException {
        return new Short(getShort(obj));
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
        return unsafe.getShort(this.base, this.fieldOffset);
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public int getInt(Object obj) throws IllegalArgumentException {
        return getShort(obj);
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public long getLong(Object obj) throws IllegalArgumentException {
        return getShort(obj);
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public float getFloat(Object obj) throws IllegalArgumentException {
        return getShort(obj);
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public double getDouble(Object obj) throws IllegalArgumentException {
        return getShort(obj);
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public void set(Object obj, Object obj2) throws IllegalAccessException, IllegalArgumentException {
        if (this.isFinal) {
            throwFinalFieldIllegalAccessException(obj2);
        }
        if (obj2 == null) {
            throwSetIllegalArgumentException(obj2);
        }
        if (obj2 instanceof Byte) {
            unsafe.putShort(this.base, this.fieldOffset, ((Byte) obj2).byteValue());
        } else if (obj2 instanceof Short) {
            unsafe.putShort(this.base, this.fieldOffset, ((Short) obj2).shortValue());
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
        setShort(obj, b2);
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public void setChar(Object obj, char c2) throws IllegalAccessException, IllegalArgumentException {
        throwSetIllegalArgumentException(c2);
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public void setShort(Object obj, short s2) throws IllegalAccessException, IllegalArgumentException {
        if (this.isFinal) {
            throwFinalFieldIllegalAccessException(s2);
        }
        unsafe.putShort(this.base, this.fieldOffset, s2);
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public void setInt(Object obj, int i2) throws IllegalAccessException, IllegalArgumentException {
        throwSetIllegalArgumentException(i2);
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public void setLong(Object obj, long j2) throws IllegalAccessException, IllegalArgumentException {
        throwSetIllegalArgumentException(j2);
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public void setFloat(Object obj, float f2) throws IllegalAccessException, IllegalArgumentException {
        throwSetIllegalArgumentException(f2);
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public void setDouble(Object obj, double d2) throws IllegalAccessException, IllegalArgumentException {
        throwSetIllegalArgumentException(d2);
    }
}
