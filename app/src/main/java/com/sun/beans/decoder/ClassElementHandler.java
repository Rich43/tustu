package com.sun.beans.decoder;

/* loaded from: rt.jar:com/sun/beans/decoder/ClassElementHandler.class */
final class ClassElementHandler extends StringElementHandler {
    ClassElementHandler() {
    }

    @Override // com.sun.beans.decoder.StringElementHandler
    public Object getValue(String str) {
        return getOwner().findClass(str);
    }
}
