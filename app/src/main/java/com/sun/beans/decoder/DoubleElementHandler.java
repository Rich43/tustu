package com.sun.beans.decoder;

/* loaded from: rt.jar:com/sun/beans/decoder/DoubleElementHandler.class */
final class DoubleElementHandler extends StringElementHandler {
    DoubleElementHandler() {
    }

    @Override // com.sun.beans.decoder.StringElementHandler
    public Object getValue(String str) {
        return Double.valueOf(str);
    }
}
