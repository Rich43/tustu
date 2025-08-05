package com.sun.org.apache.xerces.internal.xs;

import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.ls.LSInput;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xs/XSLoader.class */
public interface XSLoader {
    DOMConfiguration getConfig();

    XSModel loadURIList(StringList stringList);

    XSModel loadInputList(LSInputList lSInputList);

    XSModel loadURI(String str);

    XSModel load(LSInput lSInput);
}
