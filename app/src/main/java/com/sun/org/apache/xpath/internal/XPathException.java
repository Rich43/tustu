package com.sun.org.apache.xpath.internal;

import java.io.PrintStream;
import java.io.PrintWriter;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/XPathException.class */
public class XPathException extends TransformerException {
    static final long serialVersionUID = 4263549717619045963L;
    Object m_styleNode;
    protected Exception m_exception;

    public Object getStylesheetNode() {
        return this.m_styleNode;
    }

    public void setStylesheetNode(Object styleNode) {
        this.m_styleNode = styleNode;
    }

    public XPathException(String message, ExpressionNode ex) {
        super(message);
        this.m_styleNode = null;
        setLocator(ex);
        setStylesheetNode(getStylesheetNode(ex));
    }

    public XPathException(String message) {
        super(message);
        this.m_styleNode = null;
    }

    public Node getStylesheetNode(ExpressionNode ex) {
        ExpressionNode owner = getExpressionOwner(ex);
        if (null != owner && (owner instanceof Node)) {
            return (Node) owner;
        }
        return null;
    }

    protected ExpressionNode getExpressionOwner(ExpressionNode ex) {
        ExpressionNode parent;
        ExpressionNode expressionNodeExprGetParent = ex.exprGetParent();
        while (true) {
            parent = expressionNodeExprGetParent;
            if (null == parent || !(parent instanceof Expression)) {
                break;
            }
            expressionNodeExprGetParent = parent.exprGetParent();
        }
        return parent;
    }

    public XPathException(String message, Object styleNode) {
        super(message);
        this.m_styleNode = null;
        this.m_styleNode = styleNode;
    }

    public XPathException(String message, Node styleNode, Exception e2) {
        super(message);
        this.m_styleNode = null;
        this.m_styleNode = styleNode;
        this.m_exception = e2;
    }

    public XPathException(String message, Exception e2) {
        super(message);
        this.m_styleNode = null;
        this.m_exception = e2;
    }

    @Override // javax.xml.transform.TransformerException, java.lang.Throwable
    public void printStackTrace(PrintStream s2) {
        if (s2 == null) {
            s2 = System.err;
        }
        try {
            super.printStackTrace(s2);
        } catch (Exception e2) {
        }
        Throwable exception = this.m_exception;
        for (int i2 = 0; i2 < 10 && null != exception; i2++) {
            s2.println("---------");
            exception.printStackTrace(s2);
            if (exception instanceof TransformerException) {
                TransformerException se = (TransformerException) exception;
                Throwable prev = exception;
                exception = se.getException();
                if (prev == exception) {
                    return;
                }
            } else {
                exception = null;
            }
        }
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        String lastMessage = super.getMessage();
        Throwable exception = this.m_exception;
        while (null != exception) {
            String nextMessage = exception.getMessage();
            if (null != nextMessage) {
                lastMessage = nextMessage;
            }
            if (exception instanceof TransformerException) {
                TransformerException se = (TransformerException) exception;
                Throwable prev = exception;
                exception = se.getException();
                if (prev == exception) {
                    break;
                }
            } else {
                exception = null;
            }
        }
        return null != lastMessage ? lastMessage : "";
    }

    @Override // javax.xml.transform.TransformerException, java.lang.Throwable
    public void printStackTrace(PrintWriter s2) throws SecurityException {
        if (s2 == null) {
            s2 = new PrintWriter(System.err);
        }
        try {
            super.printStackTrace(s2);
        } catch (Exception e2) {
        }
        boolean isJdk14OrHigher = false;
        try {
            Throwable.class.getMethod("getCause", (Class[]) null);
            isJdk14OrHigher = true;
        } catch (NoSuchMethodException e3) {
        }
        if (!isJdk14OrHigher) {
            Throwable exception = this.m_exception;
            for (int i2 = 0; i2 < 10 && null != exception; i2++) {
                s2.println("---------");
                try {
                    exception.printStackTrace(s2);
                } catch (Exception e4) {
                    s2.println("Could not print stack trace...");
                }
                if (exception instanceof TransformerException) {
                    TransformerException se = (TransformerException) exception;
                    Throwable prev = exception;
                    exception = se.getException();
                    if (prev == exception) {
                        return;
                    }
                } else {
                    exception = null;
                }
            }
        }
    }

    @Override // javax.xml.transform.TransformerException
    public Throwable getException() {
        return this.m_exception;
    }
}
