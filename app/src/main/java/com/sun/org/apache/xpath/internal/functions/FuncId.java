package com.sun.org.apache.xpath.internal.functions;

import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.dtm.DTMIterator;
import com.sun.org.apache.xml.internal.utils.StringVector;
import com.sun.org.apache.xpath.internal.NodeSetDTM;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.objects.XNodeSet;
import com.sun.org.apache.xpath.internal.objects.XObject;
import java.util.StringTokenizer;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/functions/FuncId.class */
public class FuncId extends FunctionOneArg {
    static final long serialVersionUID = 8930573966143567310L;

    private StringVector getNodesByID(XPathContext xctxt, int docContext, String refval, StringVector usedrefs, NodeSetDTM nodeSet, boolean mayBeMore) {
        if (null != refval) {
            StringTokenizer tokenizer = new StringTokenizer(refval);
            boolean hasMore = tokenizer.hasMoreTokens();
            DTM dtm = xctxt.getDTM(docContext);
            while (hasMore) {
                String ref = tokenizer.nextToken();
                hasMore = tokenizer.hasMoreTokens();
                if (null == usedrefs || !usedrefs.contains(ref)) {
                    int node = dtm.getElementById(ref);
                    if (-1 != node) {
                        nodeSet.addNodeInDocOrder(node, xctxt);
                    }
                    if (null != ref && (hasMore || mayBeMore)) {
                        if (null == usedrefs) {
                            usedrefs = new StringVector();
                        }
                        usedrefs.addElement(ref);
                    }
                }
            }
        }
        return usedrefs;
    }

    @Override // com.sun.org.apache.xpath.internal.functions.Function, com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xctxt) throws TransformerException {
        int context = xctxt.getCurrentNode();
        DTM dtm = xctxt.getDTM(context);
        int docContext = dtm.getDocument();
        if (-1 == docContext) {
            error(xctxt, "ER_CONTEXT_HAS_NO_OWNERDOC", null);
        }
        XObject arg = this.m_arg0.execute(xctxt);
        int argType = arg.getType();
        XNodeSet nodes = new XNodeSet(xctxt.getDTMManager());
        NodeSetDTM nodeSet = nodes.mutableNodeset();
        if (4 == argType) {
            DTMIterator ni = arg.iter();
            StringVector usedrefs = null;
            int pos = ni.nextNode();
            while (-1 != pos) {
                DTM ndtm = ni.getDTM(pos);
                String refval = ndtm.getStringValue(pos).toString();
                pos = ni.nextNode();
                usedrefs = getNodesByID(xctxt, docContext, refval, usedrefs, nodeSet, -1 != pos);
            }
        } else {
            if (-1 == argType) {
                return nodes;
            }
            String refval2 = arg.str();
            getNodesByID(xctxt, docContext, refval2, null, nodeSet, false);
        }
        return nodes;
    }
}
