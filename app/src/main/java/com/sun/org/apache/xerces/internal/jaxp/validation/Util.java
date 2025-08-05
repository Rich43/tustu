package com.sun.org.apache.xerces.internal.jaxp.validation;

import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import com.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/validation/Util.class */
final class Util {
    Util() {
    }

    public static final XMLInputSource toXMLInputSource(StreamSource in) {
        if (in.getReader() != null) {
            return new XMLInputSource(in.getPublicId(), in.getSystemId(), in.getSystemId(), in.getReader(), (String) null);
        }
        if (in.getInputStream() != null) {
            return new XMLInputSource(in.getPublicId(), in.getSystemId(), in.getSystemId(), in.getInputStream(), (String) null);
        }
        return new XMLInputSource(in.getPublicId(), in.getSystemId(), in.getSystemId());
    }

    public static SAXException toSAXException(XNIException e2) {
        if (e2 instanceof XMLParseException) {
            return toSAXParseException((XMLParseException) e2);
        }
        if (e2.getException() instanceof SAXException) {
            return (SAXException) e2.getException();
        }
        return new SAXException(e2.getMessage(), e2.getException());
    }

    public static SAXParseException toSAXParseException(XMLParseException e2) {
        if (e2.getException() instanceof SAXParseException) {
            return (SAXParseException) e2.getException();
        }
        return new SAXParseException(e2.getMessage(), e2.getPublicId(), e2.getExpandedSystemId(), e2.getLineNumber(), e2.getColumnNumber(), e2.getException());
    }
}
