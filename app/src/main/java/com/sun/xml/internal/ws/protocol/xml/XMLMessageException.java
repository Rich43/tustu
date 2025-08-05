package com.sun.xml.internal.ws.protocol.xml;

import com.sun.istack.internal.localization.Localizable;
import com.sun.xml.internal.ws.util.exception.JAXWSExceptionBase;

/* loaded from: rt.jar:com/sun/xml/internal/ws/protocol/xml/XMLMessageException.class */
public class XMLMessageException extends JAXWSExceptionBase {
    public XMLMessageException(String key, Object... args) {
        super(key, args);
    }

    public XMLMessageException(Throwable throwable) {
        super(throwable);
    }

    public XMLMessageException(Localizable arg) {
        super("server.rt.err", arg);
    }

    @Override // com.sun.xml.internal.ws.util.exception.JAXWSExceptionBase
    public String getDefaultResourceBundleName() {
        return "com.sun.xml.internal.ws.resources.xmlmessage";
    }
}
