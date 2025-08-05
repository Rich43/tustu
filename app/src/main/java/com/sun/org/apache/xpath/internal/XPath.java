package com.sun.org.apache.xpath.internal;

import com.sun.org.apache.xalan.internal.res.XSLMessages;
import com.sun.org.apache.xml.internal.utils.DefaultErrorHandler;
import com.sun.org.apache.xml.internal.utils.PrefixResolver;
import com.sun.org.apache.xml.internal.utils.SAXSourceLocator;
import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
import com.sun.org.apache.xpath.internal.compiler.Compiler;
import com.sun.org.apache.xpath.internal.compiler.FunctionTable;
import com.sun.org.apache.xpath.internal.compiler.XPathParser;
import com.sun.org.apache.xpath.internal.objects.XObject;
import java.io.Serializable;
import java.util.Vector;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import jdk.xml.internal.XMLSecurityManager;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/XPath.class */
public class XPath implements Serializable, ExpressionOwner {
    static final long serialVersionUID = 3976493477939110553L;
    private Expression m_mainExp;
    private transient FunctionTable m_funcTable;
    String m_patternString;
    public static final int SELECT = 0;
    public static final int MATCH = 1;
    private static final boolean DEBUG_MATCHES = false;
    public static final double MATCH_SCORE_NONE = Double.NEGATIVE_INFINITY;
    public static final double MATCH_SCORE_QNAME = 0.0d;
    public static final double MATCH_SCORE_NSWILD = -0.25d;
    public static final double MATCH_SCORE_NODETEST = -0.5d;
    public static final double MATCH_SCORE_OTHER = 0.5d;

    private void initFunctionTable() {
        this.m_funcTable = new FunctionTable();
    }

    @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
    public Expression getExpression() {
        return this.m_mainExp;
    }

    public void fixupVariables(Vector vars, int globalsSize) {
        this.m_mainExp.fixupVariables(vars, globalsSize);
    }

    @Override // com.sun.org.apache.xpath.internal.ExpressionOwner
    public void setExpression(Expression exp) {
        if (null != this.m_mainExp) {
            exp.exprSetParent(this.m_mainExp.exprGetParent());
        }
        this.m_mainExp = exp;
    }

    public SourceLocator getLocator() {
        return this.m_mainExp;
    }

    public String getPatternString() {
        return this.m_patternString;
    }

    public XPath(String exprString, SourceLocator locator, PrefixResolver prefixResolver, int type, ErrorListener errorListener) throws TransformerException {
        this(exprString, locator, prefixResolver, type, errorListener, null);
    }

    public XPath(String exprString, SourceLocator locator, PrefixResolver prefixResolver, int type, ErrorListener errorListener, FunctionTable funcTable, XMLSecurityManager xmlSecMgr) throws TransformerException {
        this.m_funcTable = null;
        if (funcTable == null) {
            initFunctionTable();
        } else {
            this.m_funcTable = funcTable;
        }
        errorListener = null == errorListener ? new DefaultErrorHandler() : errorListener;
        this.m_patternString = exprString;
        XPathParser parser = new XPathParser(errorListener, locator, xmlSecMgr);
        Compiler compiler = new Compiler(errorListener, locator, this.m_funcTable);
        if (0 == type) {
            parser.initXPath(compiler, exprString, prefixResolver);
        } else if (1 == type) {
            parser.initMatchPattern(compiler, exprString, prefixResolver);
        } else {
            throw new RuntimeException(XSLMessages.createXPATHMessage("ER_CANNOT_DEAL_XPATH_TYPE", new Object[]{Integer.toString(type)}));
        }
        Expression expr = compiler.compileExpression(0);
        setExpression(expr);
        if (null != locator && (locator instanceof ExpressionNode)) {
            expr.exprSetParent((ExpressionNode) locator);
        }
    }

    public XPath(String exprString, SourceLocator locator, PrefixResolver prefixResolver, int type) throws TransformerException {
        this(exprString, locator, prefixResolver, type, null);
    }

    public XPath(String exprString, SourceLocator locator, PrefixResolver prefixResolver, int type, ErrorListener errorListener, FunctionTable funcTable) throws TransformerException {
        this(exprString, locator, prefixResolver, type, errorListener, funcTable, null);
    }

    public XPath(Expression expr) {
        this.m_funcTable = null;
        setExpression(expr);
        initFunctionTable();
    }

    public XObject execute(XPathContext xctxt, Node contextNode, PrefixResolver namespaceContext) throws TransformerException {
        return execute(xctxt, xctxt.getDTMHandleFromNode(contextNode), namespaceContext);
    }

    public XObject execute(XPathContext xctxt, int contextNode, PrefixResolver namespaceContext) throws TransformerException {
        xctxt.pushNamespaceContext(namespaceContext);
        xctxt.pushCurrentNodeAndExpression(contextNode, contextNode);
        XObject xobj = null;
        try {
            try {
                xobj = this.m_mainExp.execute(xctxt);
                xctxt.popNamespaceContext();
                xctxt.popCurrentNodeAndExpression();
            } catch (TransformerException te) {
                te.setLocator(getLocator());
                ErrorListener el = xctxt.getErrorListener();
                if (null == el) {
                    throw te;
                }
                el.error(te);
                xctxt.popNamespaceContext();
                xctxt.popCurrentNodeAndExpression();
            } catch (Exception e2) {
                e = e2;
                while (e instanceof WrappedRuntimeException) {
                    e = ((WrappedRuntimeException) e).getException();
                }
                String msg = e.getMessage();
                if (msg == null || msg.length() == 0) {
                    msg = XSLMessages.createXPATHMessage("ER_XPATH_ERROR", null);
                }
                TransformerException te2 = new TransformerException(msg, getLocator(), e);
                ErrorListener el2 = xctxt.getErrorListener();
                if (null == el2) {
                    throw te2;
                }
                el2.fatalError(te2);
                xctxt.popNamespaceContext();
                xctxt.popCurrentNodeAndExpression();
            }
            return xobj;
        } catch (Throwable th) {
            xctxt.popNamespaceContext();
            xctxt.popCurrentNodeAndExpression();
            throw th;
        }
    }

    public boolean bool(XPathContext xctxt, int contextNode, PrefixResolver namespaceContext) throws TransformerException {
        xctxt.pushNamespaceContext(namespaceContext);
        xctxt.pushCurrentNodeAndExpression(contextNode, contextNode);
        try {
            try {
                boolean zBool = this.m_mainExp.bool(xctxt);
                xctxt.popNamespaceContext();
                xctxt.popCurrentNodeAndExpression();
                return zBool;
            } catch (TransformerException te) {
                te.setLocator(getLocator());
                ErrorListener el = xctxt.getErrorListener();
                if (null == el) {
                    throw te;
                }
                el.error(te);
                xctxt.popNamespaceContext();
                xctxt.popCurrentNodeAndExpression();
                return false;
            } catch (Exception e2) {
                e = e2;
                while (e instanceof WrappedRuntimeException) {
                    e = ((WrappedRuntimeException) e).getException();
                }
                String msg = e.getMessage();
                if (msg == null || msg.length() == 0) {
                    msg = XSLMessages.createXPATHMessage("ER_XPATH_ERROR", null);
                }
                TransformerException te2 = new TransformerException(msg, getLocator(), e);
                ErrorListener el2 = xctxt.getErrorListener();
                if (null == el2) {
                    throw te2;
                }
                el2.fatalError(te2);
                xctxt.popNamespaceContext();
                xctxt.popCurrentNodeAndExpression();
                return false;
            }
        } catch (Throwable th) {
            xctxt.popNamespaceContext();
            xctxt.popCurrentNodeAndExpression();
            throw th;
        }
    }

    public double getMatchScore(XPathContext xctxt, int context) throws TransformerException {
        xctxt.pushCurrentNode(context);
        xctxt.pushCurrentExpressionNode(context);
        try {
            XObject score = this.m_mainExp.execute(xctxt);
            double dNum = score.num();
            xctxt.popCurrentNode();
            xctxt.popCurrentExpressionNode();
            return dNum;
        } catch (Throwable th) {
            xctxt.popCurrentNode();
            xctxt.popCurrentExpressionNode();
            throw th;
        }
    }

    public void warn(XPathContext xctxt, int sourceNode, String msg, Object[] args) throws TransformerException {
        String fmsg = XSLMessages.createXPATHWarning(msg, args);
        ErrorListener ehandler = xctxt.getErrorListener();
        if (null != ehandler) {
            ehandler.warning(new TransformerException(fmsg, (SAXSourceLocator) xctxt.getSAXLocator()));
        }
    }

    public void assertion(boolean b2, String msg) {
        if (!b2) {
            String fMsg = XSLMessages.createXPATHMessage("ER_INCORRECT_PROGRAMMER_ASSERTION", new Object[]{msg});
            throw new RuntimeException(fMsg);
        }
    }

    public void error(XPathContext xctxt, int sourceNode, String msg, Object[] args) throws TransformerException {
        String fmsg = XSLMessages.createXPATHMessage(msg, args);
        ErrorListener ehandler = xctxt.getErrorListener();
        if (null != ehandler) {
            ehandler.fatalError(new TransformerException(fmsg, (SAXSourceLocator) xctxt.getSAXLocator()));
        } else {
            SourceLocator slocator = xctxt.getSAXLocator();
            System.out.println(fmsg + "; file " + slocator.getSystemId() + "; line " + slocator.getLineNumber() + "; column " + slocator.getColumnNumber());
        }
    }

    public void callVisitors(ExpressionOwner owner, XPathVisitor visitor) {
        this.m_mainExp.callVisitors(this, visitor);
    }
}
