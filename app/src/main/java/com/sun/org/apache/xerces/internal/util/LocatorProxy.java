package com.sun.org.apache.xerces.internal.util;

import com.sun.org.apache.xerces.internal.xni.XMLLocator;
import org.xml.sax.ext.Locator2;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/LocatorProxy.class */
public class LocatorProxy implements Locator2 {
    private final XMLLocator fLocator;

    public LocatorProxy(XMLLocator locator) {
        this.fLocator = locator;
    }

    @Override // org.xml.sax.Locator
    public String getPublicId() {
        return this.fLocator.getPublicId();
    }

    @Override // org.xml.sax.Locator
    public String getSystemId() {
        return this.fLocator.getExpandedSystemId();
    }

    @Override // org.xml.sax.Locator
    public int getLineNumber() {
        return this.fLocator.getLineNumber();
    }

    @Override // org.xml.sax.Locator
    public int getColumnNumber() {
        return this.fLocator.getColumnNumber();
    }

    @Override // org.xml.sax.ext.Locator2
    public String getXMLVersion() {
        return this.fLocator.getXMLVersion();
    }

    @Override // org.xml.sax.ext.Locator2
    public String getEncoding() {
        return this.fLocator.getEncoding();
    }
}
