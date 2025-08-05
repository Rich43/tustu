package com.sun.org.apache.xml.internal.utils;

import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/StopParseException.class */
public class StopParseException extends SAXException {
    static final long serialVersionUID = 210102479218258961L;

    StopParseException() {
        super("Stylesheet PIs found, stop the parse");
    }
}
