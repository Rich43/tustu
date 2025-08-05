package com.sun.beans.decoder;

/* loaded from: rt.jar:com/sun/beans/decoder/FalseElementHandler.class */
final class FalseElementHandler extends NullElementHandler {
    FalseElementHandler() {
    }

    @Override // com.sun.beans.decoder.NullElementHandler, com.sun.beans.decoder.ValueObject
    public Object getValue() {
        return Boolean.FALSE;
    }
}
