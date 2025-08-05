package com.sun.org.apache.xpath.internal.domapi;

import com.sun.org.apache.xml.internal.utils.PrefixResolver;
import com.sun.org.apache.xpath.internal.XPath;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.objects.XObject;
import com.sun.org.apache.xpath.internal.res.XPATHMessages;
import javax.xml.transform.TransformerException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.xpath.XPathException;
import org.w3c.dom.xpath.XPathExpression;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/domapi/XPathExpressionImpl.class */
class XPathExpressionImpl implements XPathExpression {
    private final XPath m_xpath;
    private final Document m_doc;

    XPathExpressionImpl(XPath xpath, Document doc) {
        this.m_xpath = xpath;
        this.m_doc = doc;
    }

    @Override // org.w3c.dom.xpath.XPathExpression
    public Object evaluate(Node contextNode, short type, Object result) throws DOMException, XPathException {
        if (this.m_doc != null) {
            if (contextNode != this.m_doc && !contextNode.getOwnerDocument().equals(this.m_doc)) {
                String fmsg = XPATHMessages.createXPATHMessage("ER_WRONG_DOCUMENT", null);
                throw new DOMException((short) 4, fmsg);
            }
            short nodeType = contextNode.getNodeType();
            if (nodeType != 9 && nodeType != 1 && nodeType != 2 && nodeType != 3 && nodeType != 4 && nodeType != 8 && nodeType != 7 && nodeType != 13) {
                String fmsg2 = XPATHMessages.createXPATHMessage("ER_WRONG_NODETYPE", null);
                throw new DOMException((short) 9, fmsg2);
            }
        }
        if (!XPathResultImpl.isValidType(type)) {
            String fmsg3 = XPATHMessages.createXPATHMessage("ER_INVALID_XPATH_TYPE", new Object[]{new Integer(type)});
            throw new XPathException((short) 2, fmsg3);
        }
        XPathContext xpathSupport = new XPathContext();
        if (null != this.m_doc) {
            xpathSupport.getDTMHandleFromNode(this.m_doc);
        }
        try {
            XObject xobj = this.m_xpath.execute(xpathSupport, contextNode, (PrefixResolver) null);
            return new XPathResultImpl(type, xobj, contextNode, this.m_xpath);
        } catch (TransformerException te) {
            throw new XPathException((short) 1, te.getMessageAndLocation());
        }
    }
}
