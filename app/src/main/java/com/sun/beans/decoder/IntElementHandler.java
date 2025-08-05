package com.sun.beans.decoder;

/* loaded from: rt.jar:com/sun/beans/decoder/IntElementHandler.class */
final class IntElementHandler extends StringElementHandler {
    IntElementHandler() {
    }

    @Override // com.sun.beans.decoder.StringElementHandler
    public Object getValue(String str) {
        return Integer.decode(str);
    }
}
