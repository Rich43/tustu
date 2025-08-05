package com.sun.org.apache.xpath.internal.functions;

import com.sun.org.apache.xml.internal.dtm.DTMIterator;
import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.axes.SubContextList;
import com.sun.org.apache.xpath.internal.compiler.Compiler;
import com.sun.org.apache.xpath.internal.objects.XNumber;
import com.sun.org.apache.xpath.internal.objects.XObject;
import java.util.Vector;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/functions/FuncPosition.class */
public class FuncPosition extends Function {
    static final long serialVersionUID = -9092846348197271582L;
    private boolean m_isTopLevel;

    @Override // com.sun.org.apache.xpath.internal.functions.Function
    public void postCompileStep(Compiler compiler) {
        this.m_isTopLevel = compiler.getLocationPathDepth() == -1;
    }

    public int getPositionInContextNodeList(XPathContext xctxt) {
        int n2;
        SubContextList iter = this.m_isTopLevel ? null : xctxt.getSubContextList();
        if (null != iter) {
            int prox = iter.getProximityPosition(xctxt);
            return prox;
        }
        DTMIterator cnl = xctxt.getContextNodeList();
        if (null != cnl) {
            if (cnl.getCurrentNode() == -1) {
                if (cnl.getCurrentPos() == 0) {
                    return 0;
                }
                try {
                    cnl = cnl.cloneWithReset();
                    int currentNode = xctxt.getContextNode();
                    do {
                        n2 = cnl.nextNode();
                        if (-1 == n2) {
                            break;
                        }
                    } while (n2 != currentNode);
                } catch (CloneNotSupportedException cnse) {
                    throw new WrappedRuntimeException(cnse);
                }
            }
            return cnl.getCurrentPos();
        }
        return -1;
    }

    @Override // com.sun.org.apache.xpath.internal.functions.Function, com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xctxt) throws TransformerException {
        double pos = getPositionInContextNodeList(xctxt);
        return new XNumber(pos);
    }

    @Override // com.sun.org.apache.xpath.internal.Expression
    public void fixupVariables(Vector vars, int globalsSize) {
    }
}
