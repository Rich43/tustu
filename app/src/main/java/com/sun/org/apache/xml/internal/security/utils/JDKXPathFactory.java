package com.sun.org.apache.xml.internal.security.utils;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/JDKXPathFactory.class */
public class JDKXPathFactory extends XPathFactory {
    @Override // com.sun.org.apache.xml.internal.security.utils.XPathFactory
    public XPathAPI newXPathAPI() {
        return new JDKXPathAPI();
    }
}
