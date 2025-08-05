package com.sun.org.apache.xml.internal.dtm;

import org.w3c.dom.DOMException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/DTMDOMException.class */
public class DTMDOMException extends DOMException {
    static final long serialVersionUID = 1895654266613192414L;

    public DTMDOMException(short code, String message) {
        super(code, message);
    }

    public DTMDOMException(short code) {
        super(code, "");
    }
}
