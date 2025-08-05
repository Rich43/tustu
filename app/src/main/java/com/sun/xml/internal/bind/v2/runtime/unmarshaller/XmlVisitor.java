package com.sun.xml.internal.bind.v2.runtime.unmarshaller;

import javax.xml.namespace.NamespaceContext;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/XmlVisitor.class */
public interface XmlVisitor {

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/XmlVisitor$TextPredictor.class */
    public interface TextPredictor {
        boolean expectText();
    }

    void startDocument(LocatorEx locatorEx, NamespaceContext namespaceContext) throws SAXException;

    void endDocument() throws SAXException;

    void startElement(TagName tagName) throws SAXException;

    void endElement(TagName tagName) throws SAXException;

    void startPrefixMapping(String str, String str2) throws SAXException;

    void endPrefixMapping(String str) throws SAXException;

    void text(CharSequence charSequence) throws SAXException;

    UnmarshallingContext getContext();

    TextPredictor getPredictor();
}
