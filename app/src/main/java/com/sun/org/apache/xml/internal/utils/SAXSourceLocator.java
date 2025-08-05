package com.sun.org.apache.xml.internal.utils;

import java.io.Serializable;
import javax.xml.transform.SourceLocator;
import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.LocatorImpl;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/SAXSourceLocator.class */
public class SAXSourceLocator extends LocatorImpl implements SourceLocator, Serializable {
    static final long serialVersionUID = 3181680946321164112L;
    Locator m_locator;

    public SAXSourceLocator() {
    }

    public SAXSourceLocator(Locator locator) {
        this.m_locator = locator;
        setColumnNumber(locator.getColumnNumber());
        setLineNumber(locator.getLineNumber());
        setPublicId(locator.getPublicId());
        setSystemId(locator.getSystemId());
    }

    public SAXSourceLocator(SourceLocator locator) {
        this.m_locator = null;
        setColumnNumber(locator.getColumnNumber());
        setLineNumber(locator.getLineNumber());
        setPublicId(locator.getPublicId());
        setSystemId(locator.getSystemId());
    }

    public SAXSourceLocator(SAXParseException spe) {
        setLineNumber(spe.getLineNumber());
        setColumnNumber(spe.getColumnNumber());
        setPublicId(spe.getPublicId());
        setSystemId(spe.getSystemId());
    }

    @Override // org.xml.sax.helpers.LocatorImpl, org.xml.sax.Locator
    public String getPublicId() {
        return null == this.m_locator ? super.getPublicId() : this.m_locator.getPublicId();
    }

    @Override // org.xml.sax.helpers.LocatorImpl, org.xml.sax.Locator
    public String getSystemId() {
        return null == this.m_locator ? super.getSystemId() : this.m_locator.getSystemId();
    }

    @Override // org.xml.sax.helpers.LocatorImpl, org.xml.sax.Locator
    public int getLineNumber() {
        return null == this.m_locator ? super.getLineNumber() : this.m_locator.getLineNumber();
    }

    @Override // org.xml.sax.helpers.LocatorImpl, org.xml.sax.Locator
    public int getColumnNumber() {
        return null == this.m_locator ? super.getColumnNumber() : this.m_locator.getColumnNumber();
    }
}
