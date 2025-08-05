package com.sun.org.apache.xerces.internal.xs;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xs/XSImplementation.class */
public interface XSImplementation {
    StringList getRecognizedVersions();

    XSLoader createXSLoader(StringList stringList) throws XSException;
}
