package com.sun.org.apache.xalan.internal.xsltc.compiler.util;

import com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/util/TypeCheckError.class */
public class TypeCheckError extends Exception {
    static final long serialVersionUID = 3246224233917854640L;
    ErrorMsg _error;
    SyntaxTreeNode _node;

    public TypeCheckError(SyntaxTreeNode node) {
        this._error = null;
        this._node = null;
        this._node = node;
    }

    public TypeCheckError(ErrorMsg error) {
        this._error = null;
        this._node = null;
        this._error = error;
    }

    public TypeCheckError(String code, Object param) {
        this._error = null;
        this._node = null;
        this._error = new ErrorMsg(code, param);
    }

    public TypeCheckError(String code, Object param1, Object param2) {
        this._error = null;
        this._node = null;
        this._error = new ErrorMsg(code, param1, param2);
    }

    public ErrorMsg getErrorMsg() {
        return this._error;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return toString();
    }

    @Override // java.lang.Throwable
    public String toString() {
        if (this._error == null) {
            if (this._node != null) {
                this._error = new ErrorMsg(ErrorMsg.TYPE_CHECK_ERR, this._node.toString());
            } else {
                this._error = new ErrorMsg(ErrorMsg.TYPE_CHECK_UNK_LOC_ERR);
            }
        }
        return this._error.toString();
    }
}
