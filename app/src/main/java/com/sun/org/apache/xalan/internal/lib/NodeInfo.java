package com.sun.org.apache.xalan.internal.lib;

import com.sun.org.apache.xalan.internal.extensions.ExpressionContext;
import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeProxy;
import javax.xml.transform.SourceLocator;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/lib/NodeInfo.class */
public class NodeInfo {
    public static String systemId(ExpressionContext context) {
        Node contextNode = context.getContextNode();
        int nodeHandler = ((DTMNodeProxy) contextNode).getDTMNodeNumber();
        SourceLocator locator = ((DTMNodeProxy) contextNode).getDTM().getSourceLocatorFor(nodeHandler);
        if (locator != null) {
            return locator.getSystemId();
        }
        return null;
    }

    public static String systemId(NodeList nodeList) {
        if (nodeList == null || nodeList.getLength() == 0) {
            return null;
        }
        Node node = nodeList.item(0);
        int nodeHandler = ((DTMNodeProxy) node).getDTMNodeNumber();
        SourceLocator locator = ((DTMNodeProxy) node).getDTM().getSourceLocatorFor(nodeHandler);
        if (locator != null) {
            return locator.getSystemId();
        }
        return null;
    }

    public static String publicId(ExpressionContext context) {
        Node contextNode = context.getContextNode();
        int nodeHandler = ((DTMNodeProxy) contextNode).getDTMNodeNumber();
        SourceLocator locator = ((DTMNodeProxy) contextNode).getDTM().getSourceLocatorFor(nodeHandler);
        if (locator != null) {
            return locator.getPublicId();
        }
        return null;
    }

    public static String publicId(NodeList nodeList) {
        if (nodeList == null || nodeList.getLength() == 0) {
            return null;
        }
        Node node = nodeList.item(0);
        int nodeHandler = ((DTMNodeProxy) node).getDTMNodeNumber();
        SourceLocator locator = ((DTMNodeProxy) node).getDTM().getSourceLocatorFor(nodeHandler);
        if (locator != null) {
            return locator.getPublicId();
        }
        return null;
    }

    public static int lineNumber(ExpressionContext context) {
        Node contextNode = context.getContextNode();
        int nodeHandler = ((DTMNodeProxy) contextNode).getDTMNodeNumber();
        SourceLocator locator = ((DTMNodeProxy) contextNode).getDTM().getSourceLocatorFor(nodeHandler);
        if (locator != null) {
            return locator.getLineNumber();
        }
        return -1;
    }

    public static int lineNumber(NodeList nodeList) {
        if (nodeList == null || nodeList.getLength() == 0) {
            return -1;
        }
        Node node = nodeList.item(0);
        int nodeHandler = ((DTMNodeProxy) node).getDTMNodeNumber();
        SourceLocator locator = ((DTMNodeProxy) node).getDTM().getSourceLocatorFor(nodeHandler);
        if (locator != null) {
            return locator.getLineNumber();
        }
        return -1;
    }

    public static int columnNumber(ExpressionContext context) {
        Node contextNode = context.getContextNode();
        int nodeHandler = ((DTMNodeProxy) contextNode).getDTMNodeNumber();
        SourceLocator locator = ((DTMNodeProxy) contextNode).getDTM().getSourceLocatorFor(nodeHandler);
        if (locator != null) {
            return locator.getColumnNumber();
        }
        return -1;
    }

    public static int columnNumber(NodeList nodeList) {
        if (nodeList == null || nodeList.getLength() == 0) {
            return -1;
        }
        Node node = nodeList.item(0);
        int nodeHandler = ((DTMNodeProxy) node).getDTMNodeNumber();
        SourceLocator locator = ((DTMNodeProxy) node).getDTM().getSourceLocatorFor(nodeHandler);
        if (locator != null) {
            return locator.getColumnNumber();
        }
        return -1;
    }
}
