package com.sun.org.apache.xpath.internal.operations;

import com.sun.org.apache.xalan.internal.res.XSLMessages;
import com.sun.org.apache.xml.internal.utils.QName;
import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
import com.sun.org.apache.xpath.internal.Expression;
import com.sun.org.apache.xpath.internal.ExpressionOwner;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.XPathVisitor;
import com.sun.org.apache.xpath.internal.axes.PathComponent;
import com.sun.org.apache.xpath.internal.objects.XNodeSet;
import com.sun.org.apache.xpath.internal.objects.XObject;
import java.util.Vector;
import javafx.fxml.FXMLLoader;
import javax.xml.transform.TransformerException;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/operations/Variable.class */
public class Variable extends Expression implements PathComponent {
    static final long serialVersionUID = -4334975375609297049L;
    protected QName m_qname;
    protected int m_index;
    static final java.lang.String PSUEDOVARNAMESPACE = "http://xml.apache.org/xalan/psuedovar";
    private boolean m_fixUpWasCalled = false;
    protected boolean m_isGlobal = false;

    public void setIndex(int index) {
        this.m_index = index;
    }

    public int getIndex() {
        return this.m_index;
    }

    public void setIsGlobal(boolean isGlobal) {
        this.m_isGlobal = isGlobal;
    }

    public boolean getGlobal() {
        return this.m_isGlobal;
    }

    @Override // com.sun.org.apache.xpath.internal.Expression
    public void fixupVariables(Vector vars, int globalsSize) {
        this.m_fixUpWasCalled = true;
        vars.size();
        for (int i2 = vars.size() - 1; i2 >= 0; i2--) {
            QName qn = (QName) vars.elementAt(i2);
            if (qn.equals(this.m_qname)) {
                if (i2 < globalsSize) {
                    this.m_isGlobal = true;
                    this.m_index = i2;
                    return;
                } else {
                    this.m_index = i2 - globalsSize;
                    return;
                }
            }
        }
        java.lang.String msg = XSLMessages.createXPATHMessage("ER_COULD_NOT_FIND_VAR", new Object[]{this.m_qname.toString()});
        TransformerException te = new TransformerException(msg, this);
        throw new WrappedRuntimeException(te);
    }

    public void setQName(QName qname) {
        this.m_qname = qname;
    }

    public QName getQName() {
        return this.m_qname;
    }

    @Override // com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xctxt) throws TransformerException {
        return execute(xctxt, false);
    }

    @Override // com.sun.org.apache.xpath.internal.Expression
    public XObject execute(XPathContext xctxt, boolean destructiveOK) throws TransformerException {
        XObject result;
        xctxt.getNamespaceContext();
        if (this.m_fixUpWasCalled) {
            if (this.m_isGlobal) {
                result = xctxt.getVarStack().getGlobalVariable(xctxt, this.m_index, destructiveOK);
            } else {
                result = xctxt.getVarStack().getLocalVariable(xctxt, this.m_index, destructiveOK);
            }
        } else {
            result = xctxt.getVarStack().getVariableOrParam(xctxt, this.m_qname);
        }
        if (null == result) {
            warn(xctxt, "WG_ILLEGAL_VARIABLE_REFERENCE", new Object[]{this.m_qname.getLocalPart()});
            result = new XNodeSet(xctxt.getDTMManager());
        }
        return result;
    }

    @Override // com.sun.org.apache.xpath.internal.Expression
    public boolean isStableNumber() {
        return true;
    }

    @Override // com.sun.org.apache.xpath.internal.axes.PathComponent
    public int getAnalysisBits() {
        return 67108864;
    }

    @Override // com.sun.org.apache.xpath.internal.XPathVisitable
    public void callVisitors(ExpressionOwner owner, XPathVisitor visitor) {
        visitor.visitVariableRef(owner, this);
    }

    @Override // com.sun.org.apache.xpath.internal.Expression
    public boolean deepEquals(Expression expr) {
        if (!isSameClass(expr) || !this.m_qname.equals(((Variable) expr).m_qname)) {
            return false;
        }
        return true;
    }

    public boolean isPsuedoVarRef() {
        java.lang.String ns = this.m_qname.getNamespaceURI();
        if (null != ns && ns.equals(PSUEDOVARNAMESPACE) && this.m_qname.getLocalName().startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX)) {
            return true;
        }
        return false;
    }
}
