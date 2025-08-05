package com.sun.org.apache.xerces.internal.xni;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xni/XMLLocator.class */
public interface XMLLocator {
    String getPublicId();

    String getLiteralSystemId();

    String getBaseSystemId();

    String getExpandedSystemId();

    int getLineNumber();

    int getColumnNumber();

    int getCharacterOffset();

    String getEncoding();

    String getXMLVersion();
}
