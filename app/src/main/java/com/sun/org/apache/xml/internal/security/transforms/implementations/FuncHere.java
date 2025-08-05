package com.sun.org.apache.xml.internal.security.transforms.implementations;

import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.security.utils.I18n;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.apache.xpath.internal.NodeSetDTM;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.functions.Function;
import com.sun.org.apache.xpath.internal.objects.XNodeSet;
import com.sun.org.apache.xpath.internal.objects.XObject;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/transforms/implementations/FuncHere.class */
public class FuncHere extends Function {
    private static final long serialVersionUID = 1;

    @Override // com.sun.org.apache.xpath.internal.functions.Function, com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xPathContext) throws TransformerException {
        Node node = (Node) xPathContext.getOwnerObject();
        if (node == null) {
            return null;
        }
        int dTMHandleFromNode = xPathContext.getDTMHandleFromNode(node);
        int currentNode = xPathContext.getCurrentNode();
        DTM dtm = xPathContext.getDTM(currentNode);
        if (-1 == dtm.getDocument()) {
            error(xPathContext, "ER_CONTEXT_HAS_NO_OWNERDOC", null);
        }
        if (XMLUtils.getOwnerDocument(dtm.getNode(currentNode)) != XMLUtils.getOwnerDocument(node)) {
            throw new TransformerException(I18n.translate("xpath.funcHere.documentsDiffer"));
        }
        XNodeSet xNodeSet = new XNodeSet(xPathContext.getDTMManager());
        NodeSetDTM nodeSetDTMMutableNodeset = xNodeSet.mutableNodeset();
        switch (dtm.getNodeType(dTMHandleFromNode)) {
            case 2:
            case 7:
                nodeSetDTMMutableNodeset.addNode(dTMHandleFromNode);
                break;
            case 3:
                nodeSetDTMMutableNodeset.addNode(dtm.getParent(dTMHandleFromNode));
                break;
        }
        nodeSetDTMMutableNodeset.detach();
        return xNodeSet;
    }

    @Override // com.sun.org.apache.xpath.internal.Expression
    public void fixupVariables(Vector vector, int i2) {
    }
}
