package com.sun.beans.decoder;

/* loaded from: rt.jar:com/sun/beans/decoder/NullElementHandler.class */
class NullElementHandler extends ElementHandler implements ValueObject {
    NullElementHandler() {
    }

    @Override // com.sun.beans.decoder.ElementHandler
    protected final ValueObject getValueObject() {
        return this;
    }

    public Object getValue() {
        return null;
    }

    @Override // com.sun.beans.decoder.ValueObject
    public final boolean isVoid() {
        return false;
    }
}
