package com.sun.org.apache.xerces.internal.util;

import com.sun.org.apache.xerces.internal.xni.XMLLocator;
import org.xml.sax.Locator;
import org.xml.sax.ext.Locator2;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/SAXLocatorWrapper.class */
public final class SAXLocatorWrapper implements XMLLocator {
    private Locator fLocator = null;
    private Locator2 fLocator2 = null;

    public void setLocator(Locator locator) {
        this.fLocator = locator;
        if ((locator instanceof Locator2) || locator == null) {
            this.fLocator2 = (Locator2) locator;
        }
    }

    public Locator getLocator() {
        return this.fLocator;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public String getPublicId() {
        if (this.fLocator != null) {
            return this.fLocator.getPublicId();
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public String getLiteralSystemId() {
        if (this.fLocator != null) {
            return this.fLocator.getSystemId();
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public String getBaseSystemId() {
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public String getExpandedSystemId() {
        return getLiteralSystemId();
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public int getLineNumber() {
        if (this.fLocator != null) {
            return this.fLocator.getLineNumber();
        }
        return -1;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public int getColumnNumber() {
        if (this.fLocator != null) {
            return this.fLocator.getColumnNumber();
        }
        return -1;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public int getCharacterOffset() {
        return -1;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public String getEncoding() {
        if (this.fLocator2 != null) {
            return this.fLocator2.getEncoding();
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public String getXMLVersion() {
        if (this.fLocator2 != null) {
            return this.fLocator2.getXMLVersion();
        }
        return null;
    }
}
