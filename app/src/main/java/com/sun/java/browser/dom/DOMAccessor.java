package com.sun.java.browser.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

/* loaded from: rt.jar:com/sun/java/browser/dom/DOMAccessor.class */
public interface DOMAccessor {
    Document getDocument(Object obj) throws DOMException;

    DOMImplementation getDOMImplementation();
}
