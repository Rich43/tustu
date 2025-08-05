package com.sun.org.apache.xerces.internal.util;

import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
import com.sun.org.apache.xerces.internal.util.URI;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import javax.xml.transform.Source;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/XMLInputSourceAdaptor.class */
public final class XMLInputSourceAdaptor implements Source {
    public final XMLInputSource fSource;

    public XMLInputSourceAdaptor(XMLInputSource core) {
        this.fSource = core;
    }

    @Override // javax.xml.transform.Source
    public void setSystemId(String systemId) {
        this.fSource.setSystemId(systemId);
    }

    @Override // javax.xml.transform.Source
    public String getSystemId() {
        try {
            return XMLEntityManager.expandSystemId(this.fSource.getSystemId(), this.fSource.getBaseSystemId(), false);
        } catch (URI.MalformedURIException e2) {
            return this.fSource.getSystemId();
        }
    }
}
