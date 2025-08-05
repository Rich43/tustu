package com.sun.beans.decoder;

/* loaded from: rt.jar:com/sun/beans/decoder/TrueElementHandler.class */
final class TrueElementHandler extends NullElementHandler {
    TrueElementHandler() {
    }

    @Override // com.sun.beans.decoder.NullElementHandler, com.sun.beans.decoder.ValueObject
    public Object getValue() {
        return Boolean.TRUE;
    }
}
