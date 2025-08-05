package com.sun.xml.internal.ws.encoding.soap;

import com.sun.istack.internal.localization.Localizable;
import com.sun.xml.internal.ws.util.exception.JAXWSExceptionBase;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/soap/SerializationException.class */
public class SerializationException extends JAXWSExceptionBase {
    public SerializationException(String key, Object... args) {
        super(key, args);
    }

    public SerializationException(Localizable arg) {
        super("nestedSerializationError", arg);
    }

    public SerializationException(Throwable throwable) {
        super(throwable);
    }

    @Override // com.sun.xml.internal.ws.util.exception.JAXWSExceptionBase
    public String getDefaultResourceBundleName() {
        return "com.sun.xml.internal.ws.resources.encoding";
    }
}
