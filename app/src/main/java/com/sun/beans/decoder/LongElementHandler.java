package com.sun.beans.decoder;

/* loaded from: rt.jar:com/sun/beans/decoder/LongElementHandler.class */
final class LongElementHandler extends StringElementHandler {
    LongElementHandler() {
    }

    @Override // com.sun.beans.decoder.StringElementHandler
    public Object getValue(String str) {
        return Long.decode(str);
    }
}
