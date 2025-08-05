package com.sun.org.apache.xml.internal.security.utils;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/XalanXPathFactory.class */
public class XalanXPathFactory extends XPathFactory {
    @Override // com.sun.org.apache.xml.internal.security.utils.XPathFactory
    public XPathAPI newXPathAPI() {
        return new XalanXPathAPI();
    }
}
