package com.sun.beans.decoder;

/* loaded from: rt.jar:com/sun/beans/decoder/ShortElementHandler.class */
final class ShortElementHandler extends StringElementHandler {
    ShortElementHandler() {
    }

    @Override // com.sun.beans.decoder.StringElementHandler
    public Object getValue(String str) {
        return Short.decode(str);
    }
}
