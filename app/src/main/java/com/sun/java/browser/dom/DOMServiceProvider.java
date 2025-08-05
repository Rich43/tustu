package com.sun.java.browser.dom;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

/* loaded from: rt.jar:com/sun/java/browser/dom/DOMServiceProvider.class */
public abstract class DOMServiceProvider {
    public abstract boolean canHandle(Object obj);

    public abstract Document getDocument(Object obj) throws DOMUnsupportedException;

    public abstract DOMImplementation getDOMImplementation();
}
