package sun.reflect;

import java.lang.reflect.Field;

/* loaded from: rt.jar:sun/reflect/UnsafeLongFieldAccessorImpl.class */
class UnsafeLongFieldAccessorImpl extends UnsafeFieldAccessorImpl {
    UnsafeLongFieldAccessorImpl(Field field) {
        super(field);
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public Object get(Object obj) throws IllegalArgumentException {
        return new Long(getLong(obj));
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
        ensureObj(obj);
        return unsafe.getLong(obj, this.fieldOffset);
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public float getFloat(Object obj) throws IllegalArgumentException {
        return getLong(obj);
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public double getDouble(Object obj) throws IllegalArgumentException {
        return getLong(obj);
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public void set(Object obj, Object obj2) throws IllegalAccessException, IllegalArgumentException {
        ensureObj(obj);
        if (this.isFinal) {
            throwFinalFieldIllegalAccessException(obj2);
        }
        if (obj2 == null) {
            throwSetIllegalArgumentException(obj2);
        }
        if (obj2 instanceof Byte) {
            unsafe.putLong(obj, this.fieldOffset, ((Byte) obj2).byteValue());
            return;
        }
        if (obj2 instanceof Short) {
            unsafe.putLong(obj, this.fieldOffset, ((Short) obj2).shortValue());
            return;
        }
        if (obj2 instanceof Character) {
            unsafe.putLong(obj, this.fieldOffset, ((Character) obj2).charValue());
            return;
        }
        if (obj2 instanceof Integer) {
            unsafe.putLong(obj, this.fieldOffset, ((Integer) obj2).intValue());
        } else if (obj2 instanceof Long) {
            unsafe.putLong(obj, this.fieldOffset, ((Long) obj2).longValue());
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
        setLong(obj, b2);
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public void setChar(Object obj, char c2) throws IllegalAccessException, IllegalArgumentException {
        setLong(obj, c2);
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public void setShort(Object obj, short s2) throws IllegalAccessException, IllegalArgumentException {
        setLong(obj, s2);
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public void setInt(Object obj, int i2) throws IllegalAccessException, IllegalArgumentException {
        setLong(obj, i2);
    }

    @Override // sun.reflect.FieldAccessorImpl, sun.reflect.FieldAccessor
    public void setLong(Object obj, long j2) throws IllegalAccessException, IllegalArgumentException {
        ensureObj(obj);
        if (this.isFinal) {
            throwFinalFieldIllegalAccessException(j2);
        }
        unsafe.putLong(obj, this.fieldOffset, j2);
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
