package com.sun.xml.internal.ws.util;

import com.sun.istack.internal.localization.Localizable;
import com.sun.xml.internal.ws.util.exception.JAXWSExceptionBase;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/UtilException.class */
public class UtilException extends JAXWSExceptionBase {
    public UtilException(String key, Object... args) {
        super(key, args);
    }

    public UtilException(Throwable throwable) {
        super(throwable);
    }

    public UtilException(Localizable arg) {
        super("nestedUtilError", arg);
    }

    @Override // com.sun.xml.internal.ws.util.exception.JAXWSExceptionBase
    public String getDefaultResourceBundleName() {
        return "com.sun.xml.internal.ws.resources.util";
    }
}
