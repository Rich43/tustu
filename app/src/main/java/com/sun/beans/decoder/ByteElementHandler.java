package com.sun.beans.decoder;

/* loaded from: rt.jar:com/sun/beans/decoder/ByteElementHandler.class */
final class ByteElementHandler extends StringElementHandler {
    ByteElementHandler() {
    }

    @Override // com.sun.beans.decoder.StringElementHandler
    public Object getValue(String str) {
        return Byte.decode(str);
    }
}
