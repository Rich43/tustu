package com.sun.org.apache.xerces.internal.util;

import com.sun.org.apache.xerces.internal.xni.XMLLocator;
import javax.xml.stream.Location;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/StAXLocationWrapper.class */
public final class StAXLocationWrapper implements XMLLocator {
    private Location fLocation = null;

    public void setLocation(Location location) {
        this.fLocation = location;
    }

    public Location getLocation() {
        return this.fLocation;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public String getPublicId() {
        if (this.fLocation != null) {
            return this.fLocation.getPublicId();
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public String getLiteralSystemId() {
        if (this.fLocation != null) {
            return this.fLocation.getSystemId();
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
        if (this.fLocation != null) {
            return this.fLocation.getLineNumber();
        }
        return -1;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public int getColumnNumber() {
        if (this.fLocation != null) {
            return this.fLocation.getColumnNumber();
        }
        return -1;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public int getCharacterOffset() {
        if (this.fLocation != null) {
            return this.fLocation.getCharacterOffset();
        }
        return -1;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public String getEncoding() {
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLLocator
    public String getXMLVersion() {
        return null;
    }
}
