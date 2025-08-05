package com.sun.org.apache.xalan.internal.xsltc.compiler;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/CompilerException.class */
public final class CompilerException extends Exception {
    static final long serialVersionUID = 1732939618562742663L;
    private String _msg;

    public CompilerException() {
    }

    public CompilerException(Exception e2) {
        super(e2.toString());
        this._msg = e2.toString();
    }

    public CompilerException(String message) {
        super(message);
        this._msg = message;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        int col = this._msg.indexOf(58);
        if (col > -1) {
            return this._msg.substring(col);
        }
        return this._msg;
    }
}
