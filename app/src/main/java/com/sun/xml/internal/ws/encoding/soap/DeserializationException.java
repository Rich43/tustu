package com.sun.xml.internal.ws.encoding.soap;

import com.sun.istack.internal.localization.Localizable;
import com.sun.xml.internal.ws.util.exception.JAXWSExceptionBase;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/soap/DeserializationException.class */
public class DeserializationException extends JAXWSExceptionBase {
    public DeserializationException(String key, Object... args) {
        super(key, args);
    }

    public DeserializationException(Throwable throwable) {
        super(throwable);
    }

    public DeserializationException(Localizable arg) {
        super("nestedDeserializationError", arg);
    }

    @Override // com.sun.xml.internal.ws.util.exception.JAXWSExceptionBase
    public String getDefaultResourceBundleName() {
        return "com.sun.xml.internal.ws.resources.encoding";
    }
}
