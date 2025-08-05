package com.sun.xml.internal.bind;

import java.util.concurrent.Callable;
import javax.xml.bind.ValidationEventHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/IDResolver.class */
public abstract class IDResolver {
    public abstract void bind(String str, Object obj) throws SAXException;

    public abstract Callable<?> resolve(String str, Class cls) throws SAXException;

    public void startDocument(ValidationEventHandler eventHandler) throws SAXException {
    }

    public void endDocument() throws SAXException {
    }
}
