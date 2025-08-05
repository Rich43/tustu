package com.sun.org.apache.xpath.internal;

import com.sun.org.apache.xalan.internal.res.XSLMessages;
import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.dtm.DTMIterator;
import com.sun.org.apache.xml.internal.utils.XMLString;
import com.sun.org.apache.xpath.internal.objects.XNodeSet;
import com.sun.org.apache.xpath.internal.objects.XObject;
import java.io.Serializable;
import java.util.Vector;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/Expression.class */
public abstract class Expression implements Serializable, ExpressionNode, XPathVisitable {
    static final long serialVersionUID = 565665869777906902L;
    private ExpressionNode m_parent;

    public abstract XObject execute(XPathContext xPathContext) throws TransformerException;

    public abstract void fixupVariables(Vector vector, int i2);

    public abstract boolean deepEquals(Expression expression);

    public boolean canTraverseOutsideSubtree() {
        return false;
    }

    public XObject execute(XPathContext xctxt, int currentNode) throws TransformerException {
        return execute(xctxt);
    }

    public XObject execute(XPathContext xctxt, int currentNode, DTM dtm, int expType) throws TransformerException {
        return execute(xctxt);
    }

    public XObject execute(XPathContext xctxt, boolean destructiveOK) throws TransformerException {
        return execute(xctxt);
    }

    public double num(XPathContext xctxt) throws TransformerException {
        return execute(xctxt).num();
    }

    public boolean bool(XPathContext xctxt) throws TransformerException {
        return execute(xctxt).bool();
    }

    public XMLString xstr(XPathContext xctxt) throws TransformerException {
        return execute(xctxt).xstr();
    }

    public boolean isNodesetExpr() {
        return false;
    }

    public int asNode(XPathContext xctxt) throws TransformerException {
        DTMIterator iter = execute(xctxt).iter();
        return iter.nextNode();
    }

    public DTMIterator asIterator(XPathContext xctxt, int contextNode) throws TransformerException {
        try {
            xctxt.pushCurrentNodeAndExpression(contextNode, contextNode);
            DTMIterator dTMIteratorIter = execute(xctxt).iter();
            xctxt.popCurrentNodeAndExpression();
            return dTMIteratorIter;
        } catch (Throwable th) {
            xctxt.popCurrentNodeAndExpression();
            throw th;
        }
    }

    public DTMIterator asIteratorRaw(XPathContext xctxt, int contextNode) throws TransformerException {
        try {
            xctxt.pushCurrentNodeAndExpression(contextNode, contextNode);
            XNodeSet nodeset = (XNodeSet) execute(xctxt);
            DTMIterator dTMIteratorIterRaw = nodeset.iterRaw();
            xctxt.popCurrentNodeAndExpression();
            return dTMIteratorIterRaw;
        } catch (Throwable th) {
            xctxt.popCurrentNodeAndExpression();
            throw th;
        }
    }

    public void executeCharsToContentHandler(XPathContext xctxt, ContentHandler handler) throws TransformerException, SAXException {
        XObject obj = execute(xctxt);
        obj.dispatchCharactersEvents(handler);
        obj.detach();
    }

    public boolean isStableNumber() {
        return false;
    }

    protected final boolean isSameClass(Expression expr) {
        return null != expr && getClass() == expr.getClass();
    }

    public void warn(XPathContext xctxt, String msg, Object[] args) throws TransformerException {
        String fmsg = XSLMessages.createXPATHWarning(msg, args);
        if (null != xctxt) {
            ErrorListener eh = xctxt.getErrorListener();
            eh.warning(new TransformerException(fmsg, xctxt.getSAXLocator()));
        }
    }

    public void assertion(boolean b2, String msg) {
        if (!b2) {
            String fMsg = XSLMessages.createXPATHMessage("ER_INCORRECT_PROGRAMMER_ASSERTION", new Object[]{msg});
            throw new RuntimeException(fMsg);
        }
    }

    public void error(XPathContext xctxt, String msg, Object[] args) throws TransformerException {
        String fmsg = XSLMessages.createXPATHMessage(msg, args);
        if (null != xctxt) {
            ErrorListener eh = xctxt.getErrorListener();
            TransformerException te = new TransformerException(fmsg, this);
            eh.fatalError(te);
        }
    }

    public ExpressionNode getExpressionOwner() {
        ExpressionNode parent;
        ExpressionNode expressionNodeExprGetParent = exprGetParent();
        while (true) {
            parent = expressionNodeExprGetParent;
            if (null == parent || !(parent instanceof Expression)) {
                break;
            }
            expressionNodeExprGetParent = parent.exprGetParent();
        }
        return parent;
    }

    @Override // com.sun.org.apache.xpath.internal.ExpressionNode
    public void exprSetParent(ExpressionNode n2) {
        assertion(n2 != this, "Can not parent an expression to itself!");
        this.m_parent = n2;
    }

    @Override // com.sun.org.apache.xpath.internal.ExpressionNode
    public ExpressionNode exprGetParent() {
        return this.m_parent;
    }

    @Override // com.sun.org.apache.xpath.internal.ExpressionNode
    public void exprAddChild(ExpressionNode n2, int i2) {
        assertion(false, "exprAddChild method not implemented!");
    }

    @Override // com.sun.org.apache.xpath.internal.ExpressionNode
    public ExpressionNode exprGetChild(int i2) {
        return null;
    }

    @Override // com.sun.org.apache.xpath.internal.ExpressionNode
    public int exprGetNumChildren() {
        return 0;
    }

    @Override // javax.xml.transform.SourceLocator
    public String getPublicId() {
        if (null == this.m_parent) {
            return null;
        }
        return this.m_parent.getPublicId();
    }

    @Override // javax.xml.transform.SourceLocator
    public String getSystemId() {
        if (null == this.m_parent) {
            return null;
        }
        return this.m_parent.getSystemId();
    }

    @Override // javax.xml.transform.SourceLocator
    public int getLineNumber() {
        if (null == this.m_parent) {
            return 0;
        }
        return this.m_parent.getLineNumber();
    }

    @Override // javax.xml.transform.SourceLocator
    public int getColumnNumber() {
        if (null == this.m_parent) {
            return 0;
        }
        return this.m_parent.getColumnNumber();
    }
}
