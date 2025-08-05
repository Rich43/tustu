package com.sun.xml.internal.bind.unmarshaller;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.LocatorEx;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/unmarshaller/InfosetScanner.class */
public interface InfosetScanner<XmlNode> {
    void scan(XmlNode xmlnode) throws SAXException;

    void setContentHandler(ContentHandler contentHandler);

    ContentHandler getContentHandler();

    XmlNode getCurrentElement();

    LocatorEx getLocator();
}
