package com.sun.org.apache.xpath.internal.domapi;

import com.sun.org.apache.xml.internal.utils.PrefixResolver;
import com.sun.org.apache.xpath.internal.XPath;
import com.sun.org.apache.xpath.internal.res.XPATHMessages;
import javax.xml.transform.TransformerException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.xpath.XPathEvaluator;
import org.w3c.dom.xpath.XPathException;
import org.w3c.dom.xpath.XPathExpression;
import org.w3c.dom.xpath.XPathNSResolver;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/domapi/XPathEvaluatorImpl.class */
public final class XPathEvaluatorImpl implements XPathEvaluator {
    private final Document m_doc;

    /* loaded from: rt.jar:com/sun/org/apache/xpath/internal/domapi/XPathEvaluatorImpl$DummyPrefixResolver.class */
    private class DummyPrefixResolver implements PrefixResolver {
        DummyPrefixResolver() {
        }

        @Override // com.sun.org.apache.xml.internal.utils.PrefixResolver
        public String getNamespaceForPrefix(String prefix, Node context) {
            String fmsg = XPATHMessages.createXPATHMessage("ER_NULL_RESOLVER", null);
            throw new DOMException((short) 14, fmsg);
        }

        @Override // com.sun.org.apache.xml.internal.utils.PrefixResolver
        public String getNamespaceForPrefix(String prefix) {
            return getNamespaceForPrefix(prefix, null);
        }

        @Override // com.sun.org.apache.xml.internal.utils.PrefixResolver
        public boolean handlesNullPrefixes() {
            return false;
        }

        @Override // com.sun.org.apache.xml.internal.utils.PrefixResolver
        public String getBaseIdentifier() {
            return null;
        }
    }

    public XPathEvaluatorImpl(Document doc) {
        this.m_doc = doc;
    }

    public XPathEvaluatorImpl() {
        this.m_doc = null;
    }

    @Override // org.w3c.dom.xpath.XPathEvaluator
    public XPathExpression createExpression(String expression, XPathNSResolver resolver) throws DOMException, XPathException {
        try {
            XPath xpath = new XPath(expression, null, null == resolver ? new DummyPrefixResolver() : (PrefixResolver) resolver, 0);
            return new XPathExpressionImpl(xpath, this.m_doc);
        } catch (TransformerException e2) {
            if (e2 instanceof XPathStylesheetDOM3Exception) {
                throw new DOMException((short) 14, e2.getMessageAndLocation());
            }
            throw new XPathException((short) 1, e2.getMessageAndLocation());
        }
    }

    @Override // org.w3c.dom.xpath.XPathEvaluator
    public XPathNSResolver createNSResolver(Node nodeResolver) {
        return new XPathNSResolverImpl(nodeResolver.getNodeType() == 9 ? ((Document) nodeResolver).getDocumentElement() : nodeResolver);
    }

    @Override // org.w3c.dom.xpath.XPathEvaluator
    public Object evaluate(String expression, Node contextNode, XPathNSResolver resolver, short type, Object result) throws DOMException, XPathException {
        XPathExpression xpathExpression = createExpression(expression, resolver);
        return xpathExpression.evaluate(contextNode, type, result);
    }
}
