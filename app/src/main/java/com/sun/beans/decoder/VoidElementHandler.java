package com.sun.beans.decoder;

/* loaded from: rt.jar:com/sun/beans/decoder/VoidElementHandler.class */
final class VoidElementHandler extends ObjectElementHandler {
    VoidElementHandler() {
    }

    @Override // com.sun.beans.decoder.ObjectElementHandler, com.sun.beans.decoder.ElementHandler
    protected boolean isArgument() {
        return false;
    }
}
