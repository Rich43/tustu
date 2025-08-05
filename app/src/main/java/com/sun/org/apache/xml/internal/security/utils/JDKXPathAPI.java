package com.sun.org.apache.xml.internal.security.utils;

import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactoryConfigurationException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/JDKXPathAPI.class */
public class JDKXPathAPI implements XPathAPI {
    private javax.xml.xpath.XPathFactory xpf;
    private String xpathStr;
    private XPathExpression xpathExpression;

    @Override // com.sun.org.apache.xml.internal.security.utils.XPathAPI
    public NodeList selectNodeList(Node node, Node node2, String str, Node node3) throws TransformerException {
        if (!str.equals(this.xpathStr) || this.xpathExpression == null) {
            if (this.xpf == null) {
                this.xpf = javax.xml.xpath.XPathFactory.newInstance();
                try {
                    this.xpf.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", Boolean.TRUE.booleanValue());
                } catch (XPathFactoryConfigurationException e2) {
                    throw new TransformerException(e2);
                }
            }
            XPath xPathNewXPath = this.xpf.newXPath();
            xPathNewXPath.setNamespaceContext(new DOMNamespaceContext(node3));
            this.xpathStr = str;
            try {
                this.xpathExpression = xPathNewXPath.compile(this.xpathStr);
            } catch (XPathExpressionException e3) {
                throw new TransformerException(e3);
            }
        }
        try {
            return (NodeList) this.xpathExpression.evaluate(node, XPathConstants.NODESET);
        } catch (XPathExpressionException e4) {
            throw new TransformerException(e4);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.XPathAPI
    public boolean evaluate(Node node, Node node2, String str, Node node3) throws TransformerException {
        if (!str.equals(this.xpathStr) || this.xpathExpression == null) {
            if (this.xpf == null) {
                this.xpf = javax.xml.xpath.XPathFactory.newInstance();
                try {
                    this.xpf.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", Boolean.TRUE.booleanValue());
                } catch (XPathFactoryConfigurationException e2) {
                    throw new TransformerException(e2);
                }
            }
            XPath xPathNewXPath = this.xpf.newXPath();
            xPathNewXPath.setNamespaceContext(new DOMNamespaceContext(node3));
            this.xpathStr = str;
            try {
                this.xpathExpression = xPathNewXPath.compile(this.xpathStr);
            } catch (XPathExpressionException e3) {
                throw new TransformerException(e3);
            }
        }
        try {
            return ((Boolean) this.xpathExpression.evaluate(node, XPathConstants.BOOLEAN)).booleanValue();
        } catch (XPathExpressionException e4) {
            throw new TransformerException(e4);
        }
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.XPathAPI
    public void clear() {
        this.xpathStr = null;
        this.xpathExpression = null;
        this.xpf = null;
    }
}
