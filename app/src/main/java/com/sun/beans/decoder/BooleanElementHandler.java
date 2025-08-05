package com.sun.beans.decoder;

/* loaded from: rt.jar:com/sun/beans/decoder/BooleanElementHandler.class */
final class BooleanElementHandler extends StringElementHandler {
    BooleanElementHandler() {
    }

    @Override // com.sun.beans.decoder.StringElementHandler
    public Object getValue(String str) {
        if (Boolean.TRUE.toString().equalsIgnoreCase(str)) {
            return Boolean.TRUE;
        }
        if (Boolean.FALSE.toString().equalsIgnoreCase(str)) {
            return Boolean.FALSE;
        }
        throw new IllegalArgumentException("Unsupported boolean argument: " + str);
    }
}
