package com.sun.org.apache.xpath.internal.functions;

import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xpath.internal.NodeSetDTM;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.objects.XNodeSet;
import com.sun.org.apache.xpath.internal.objects.XObject;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/functions/FuncHere.class */
public final class FuncHere extends Function {
    private static final long serialVersionUID = 4328660760070034592L;

    @Override // com.sun.org.apache.xpath.internal.functions.Function, com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xctxt) throws TransformerException {
        Node xpathOwnerNode = (Node) xctxt.getOwnerObject();
        if (xpathOwnerNode == null) {
            return null;
        }
        int xpathOwnerNodeDTM = xctxt.getDTMHandleFromNode(xpathOwnerNode);
        int currentNode = xctxt.getCurrentNode();
        DTM dtm = xctxt.getDTM(currentNode);
        int docContext = dtm.getDocument();
        if (docContext == -1) {
            error(xctxt, "ER_CONTEXT_HAS_NO_OWNERDOC", null);
        }
        Document currentDoc = getOwnerDocument(dtm.getNode(currentNode));
        Document xpathOwnerDoc = getOwnerDocument(xpathOwnerNode);
        if (currentDoc != xpathOwnerDoc) {
            throw new TransformerException("Owner documents differ");
        }
        XNodeSet nodes = new XNodeSet(xctxt.getDTMManager());
        NodeSetDTM nodeSet = nodes.mutableNodeset();
        switch (dtm.getNodeType(xpathOwnerNodeDTM)) {
            case 2:
            case 7:
                nodeSet.addNode(xpathOwnerNodeDTM);
                break;
            case 3:
                int hereNode = dtm.getParent(xpathOwnerNodeDTM);
                nodeSet.addNode(hereNode);
                break;
        }
        nodeSet.detach();
        return nodes;
    }

    private static Document getOwnerDocument(Node node) {
        if (node.getNodeType() == 9) {
            return (Document) node;
        }
        return node.getOwnerDocument();
    }

    @Override // com.sun.org.apache.xpath.internal.Expression
    public void fixupVariables(Vector vars, int globalsSize) {
    }
}
