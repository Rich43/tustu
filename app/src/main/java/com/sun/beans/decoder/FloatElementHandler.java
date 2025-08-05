package com.sun.beans.decoder;

/* loaded from: rt.jar:com/sun/beans/decoder/FloatElementHandler.class */
final class FloatElementHandler extends StringElementHandler {
    FloatElementHandler() {
    }

    @Override // com.sun.beans.decoder.StringElementHandler
    public Object getValue(String str) {
        return Float.valueOf(str);
    }
}
