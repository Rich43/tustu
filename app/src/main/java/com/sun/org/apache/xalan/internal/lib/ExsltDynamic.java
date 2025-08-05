package com.sun.org.apache.xalan.internal.lib;

import com.sun.org.apache.xalan.internal.extensions.ExpressionContext;
import com.sun.org.apache.xalan.internal.res.XSLMessages;
import com.sun.org.apache.xpath.internal.NodeSet;
import com.sun.org.apache.xpath.internal.NodeSetDTM;
import com.sun.org.apache.xpath.internal.XPath;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.objects.XBoolean;
import com.sun.org.apache.xpath.internal.objects.XNodeSet;
import com.sun.org.apache.xpath.internal.objects.XNumber;
import com.sun.org.apache.xpath.internal.objects.XObject;
import javax.xml.transform.TransformerException;
import jdk.xml.internal.JdkXmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXNotSupportedException;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/lib/ExsltDynamic.class */
public class ExsltDynamic extends ExsltBase {
    public static final String EXSL_URI = "http://exslt.org/common";

    public static double max(ExpressionContext myContext, NodeList nl, String expr) throws SAXNotSupportedException {
        if (myContext instanceof XPathContext.XPathExpressionContext) {
            XPathContext xctxt = ((XPathContext.XPathExpressionContext) myContext).getXPathContext();
            if (expr == null || expr.length() == 0) {
                return Double.NaN;
            }
            NodeSetDTM contextNodes = new NodeSetDTM(nl, xctxt);
            xctxt.pushContextNodeList(contextNodes);
            double maxValue = -1.7976931348623157E308d;
            for (int i2 = 0; i2 < contextNodes.getLength(); i2++) {
                int contextNode = contextNodes.item(i2);
                xctxt.pushCurrentNode(contextNode);
                try {
                    XPath dynamicXPath = new XPath(expr, xctxt.getSAXLocator(), xctxt.getNamespaceContext(), 0);
                    double result = dynamicXPath.execute(xctxt, contextNode, xctxt.getNamespaceContext()).num();
                    xctxt.popCurrentNode();
                    if (result > maxValue) {
                        maxValue = result;
                    }
                } catch (TransformerException e2) {
                    xctxt.popCurrentNode();
                    xctxt.popContextNodeList();
                    return Double.NaN;
                }
            }
            xctxt.popContextNodeList();
            return maxValue;
        }
        throw new SAXNotSupportedException(XSLMessages.createMessage("ER_INVALID_CONTEXT_PASSED", new Object[]{myContext}));
    }

    public static double min(ExpressionContext myContext, NodeList nl, String expr) throws SAXNotSupportedException {
        if (myContext instanceof XPathContext.XPathExpressionContext) {
            XPathContext xctxt = ((XPathContext.XPathExpressionContext) myContext).getXPathContext();
            if (expr == null || expr.length() == 0) {
                return Double.NaN;
            }
            NodeSetDTM contextNodes = new NodeSetDTM(nl, xctxt);
            xctxt.pushContextNodeList(contextNodes);
            double minValue = Double.MAX_VALUE;
            for (int i2 = 0; i2 < nl.getLength(); i2++) {
                int contextNode = contextNodes.item(i2);
                xctxt.pushCurrentNode(contextNode);
                try {
                    XPath dynamicXPath = new XPath(expr, xctxt.getSAXLocator(), xctxt.getNamespaceContext(), 0);
                    double result = dynamicXPath.execute(xctxt, contextNode, xctxt.getNamespaceContext()).num();
                    xctxt.popCurrentNode();
                    if (result < minValue) {
                        minValue = result;
                    }
                } catch (TransformerException e2) {
                    xctxt.popCurrentNode();
                    xctxt.popContextNodeList();
                    return Double.NaN;
                }
            }
            xctxt.popContextNodeList();
            return minValue;
        }
        throw new SAXNotSupportedException(XSLMessages.createMessage("ER_INVALID_CONTEXT_PASSED", new Object[]{myContext}));
    }

    public static double sum(ExpressionContext myContext, NodeList nl, String expr) throws SAXNotSupportedException {
        if (myContext instanceof XPathContext.XPathExpressionContext) {
            XPathContext xctxt = ((XPathContext.XPathExpressionContext) myContext).getXPathContext();
            if (expr == null || expr.length() == 0) {
                return Double.NaN;
            }
            NodeSetDTM contextNodes = new NodeSetDTM(nl, xctxt);
            xctxt.pushContextNodeList(contextNodes);
            double sum = 0.0d;
            for (int i2 = 0; i2 < nl.getLength(); i2++) {
                int contextNode = contextNodes.item(i2);
                xctxt.pushCurrentNode(contextNode);
                try {
                    XPath dynamicXPath = new XPath(expr, xctxt.getSAXLocator(), xctxt.getNamespaceContext(), 0);
                    double result = dynamicXPath.execute(xctxt, contextNode, xctxt.getNamespaceContext()).num();
                    xctxt.popCurrentNode();
                    sum += result;
                } catch (TransformerException e2) {
                    xctxt.popCurrentNode();
                    xctxt.popContextNodeList();
                    return Double.NaN;
                }
            }
            xctxt.popContextNodeList();
            return sum;
        }
        throw new SAXNotSupportedException(XSLMessages.createMessage("ER_INVALID_CONTEXT_PASSED", new Object[]{myContext}));
    }

    public static NodeList map(ExpressionContext myContext, NodeList nl, String expr) throws SAXNotSupportedException {
        Element element;
        Document lDoc = null;
        if (myContext instanceof XPathContext.XPathExpressionContext) {
            XPathContext xctxt = ((XPathContext.XPathExpressionContext) myContext).getXPathContext();
            if (expr == null || expr.length() == 0) {
                return new NodeSet();
            }
            NodeSetDTM contextNodes = new NodeSetDTM(nl, xctxt);
            xctxt.pushContextNodeList(contextNodes);
            NodeSet resultSet = new NodeSet();
            resultSet.setShouldCacheNodes(true);
            for (int i2 = 0; i2 < nl.getLength(); i2++) {
                int contextNode = contextNodes.item(i2);
                xctxt.pushCurrentNode(contextNode);
                try {
                    XPath dynamicXPath = new XPath(expr, xctxt.getSAXLocator(), xctxt.getNamespaceContext(), 0);
                    XObject object = dynamicXPath.execute(xctxt, contextNode, xctxt.getNamespaceContext());
                    if (object instanceof XNodeSet) {
                        NodeList nodelist = ((XNodeSet) object).nodelist();
                        for (int k2 = 0; k2 < nodelist.getLength(); k2++) {
                            Node n2 = nodelist.item(k2);
                            if (!resultSet.contains(n2)) {
                                resultSet.addNode(n2);
                            }
                        }
                    } else {
                        if (lDoc == null) {
                            lDoc = JdkXmlUtils.getDOMDocument();
                        }
                        if (object instanceof XNumber) {
                            element = lDoc.createElementNS("http://exslt.org/common", "exsl:number");
                        } else if (object instanceof XBoolean) {
                            element = lDoc.createElementNS("http://exslt.org/common", "exsl:boolean");
                        } else {
                            element = lDoc.createElementNS("http://exslt.org/common", "exsl:string");
                        }
                        Text textNode = lDoc.createTextNode(object.str());
                        element.appendChild(textNode);
                        resultSet.addNode(element);
                    }
                    xctxt.popCurrentNode();
                } catch (Exception e2) {
                    xctxt.popCurrentNode();
                    xctxt.popContextNodeList();
                    return new NodeSet();
                }
            }
            xctxt.popContextNodeList();
            return resultSet;
        }
        throw new SAXNotSupportedException(XSLMessages.createMessage("ER_INVALID_CONTEXT_PASSED", new Object[]{myContext}));
    }

    public static XObject evaluate(ExpressionContext myContext, String xpathExpr) throws SAXNotSupportedException {
        if (myContext instanceof XPathContext.XPathExpressionContext) {
            XPathContext xctxt = null;
            try {
                xctxt = ((XPathContext.XPathExpressionContext) myContext).getXPathContext();
                XPath dynamicXPath = new XPath(xpathExpr, xctxt.getSAXLocator(), xctxt.getNamespaceContext(), 0);
                return dynamicXPath.execute(xctxt, myContext.getContextNode(), xctxt.getNamespaceContext());
            } catch (TransformerException e2) {
                return new XNodeSet(xctxt.getDTMManager());
            }
        }
        throw new SAXNotSupportedException(XSLMessages.createMessage("ER_INVALID_CONTEXT_PASSED", new Object[]{myContext}));
    }

    public static NodeList closure(ExpressionContext myContext, NodeList nl, String expr) throws SAXNotSupportedException {
        if (myContext instanceof XPathContext.XPathExpressionContext) {
            XPathContext xctxt = ((XPathContext.XPathExpressionContext) myContext).getXPathContext();
            if (expr == null || expr.length() == 0) {
                return new NodeSet();
            }
            NodeSet closureSet = new NodeSet();
            closureSet.setShouldCacheNodes(true);
            NodeList iterationList = nl;
            do {
                NodeSet iterationSet = new NodeSet();
                NodeSetDTM contextNodes = new NodeSetDTM(iterationList, xctxt);
                xctxt.pushContextNodeList(contextNodes);
                for (int i2 = 0; i2 < iterationList.getLength(); i2++) {
                    int contextNode = contextNodes.item(i2);
                    xctxt.pushCurrentNode(contextNode);
                    try {
                        XPath dynamicXPath = new XPath(expr, xctxt.getSAXLocator(), xctxt.getNamespaceContext(), 0);
                        XObject object = dynamicXPath.execute(xctxt, contextNode, xctxt.getNamespaceContext());
                        if (object instanceof XNodeSet) {
                            NodeList nodelist = ((XNodeSet) object).nodelist();
                            for (int k2 = 0; k2 < nodelist.getLength(); k2++) {
                                Node n2 = nodelist.item(k2);
                                if (!iterationSet.contains(n2)) {
                                    iterationSet.addNode(n2);
                                }
                            }
                            xctxt.popCurrentNode();
                        } else {
                            xctxt.popCurrentNode();
                            xctxt.popContextNodeList();
                            return new NodeSet();
                        }
                    } catch (TransformerException e2) {
                        xctxt.popCurrentNode();
                        xctxt.popContextNodeList();
                        return new NodeSet();
                    }
                }
                xctxt.popContextNodeList();
                iterationList = iterationSet;
                for (int i3 = 0; i3 < iterationList.getLength(); i3++) {
                    Node n3 = iterationList.item(i3);
                    if (!closureSet.contains(n3)) {
                        closureSet.addNode(n3);
                    }
                }
            } while (iterationList.getLength() > 0);
            return closureSet;
        }
        throw new SAXNotSupportedException(XSLMessages.createMessage("ER_INVALID_CONTEXT_PASSED", new Object[]{myContext}));
    }
}
