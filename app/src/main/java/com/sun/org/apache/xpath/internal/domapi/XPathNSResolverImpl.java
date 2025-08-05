package com.sun.org.apache.xpath.internal.domapi;

import com.sun.org.apache.xml.internal.utils.PrefixResolverDefault;
import org.w3c.dom.Node;
import org.w3c.dom.xpath.XPathNSResolver;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/domapi/XPathNSResolverImpl.class */
class XPathNSResolverImpl extends PrefixResolverDefault implements XPathNSResolver {
    public XPathNSResolverImpl(Node xpathExpressionContext) {
        super(xpathExpressionContext);
    }

    @Override // org.w3c.dom.xpath.XPathNSResolver
    public String lookupNamespaceURI(String prefix) {
        return super.getNamespaceForPrefix(prefix);
    }
}
