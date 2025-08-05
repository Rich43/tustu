package com.sun.beans.decoder;

/* loaded from: rt.jar:com/sun/beans/decoder/ValueObjectImpl.class */
final class ValueObjectImpl implements ValueObject {
    static final ValueObject NULL = new ValueObjectImpl(null);
    static final ValueObject VOID = new ValueObjectImpl();
    private Object value;
    private boolean isVoid;

    static ValueObject create(Object obj) {
        return obj != null ? new ValueObjectImpl(obj) : NULL;
    }

    private ValueObjectImpl() {
        this.isVoid = true;
    }

    private ValueObjectImpl(Object obj) {
        this.value = obj;
    }

    @Override // com.sun.beans.decoder.ValueObject
    public Object getValue() {
        return this.value;
    }

    @Override // com.sun.beans.decoder.ValueObject
    public boolean isVoid() {
        return this.isVoid;
    }
}
