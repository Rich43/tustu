package com.sun.org.apache.xalan.internal.xsltc;

import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/TransletException.class */
public final class TransletException extends SAXException {
    static final long serialVersionUID = -878916829521217293L;

    public TransletException() {
        super("Translet error");
    }

    public TransletException(Exception e2) {
        super(e2.toString());
        initCause(e2);
    }

    public TransletException(String message) {
        super(message);
    }
}
