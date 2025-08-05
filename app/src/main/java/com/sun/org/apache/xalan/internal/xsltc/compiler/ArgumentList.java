package com.sun.org.apache.xalan.internal.xsltc.compiler;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/ArgumentList.class */
final class ArgumentList {
    private final Expression _arg;
    private final ArgumentList _rest;

    public ArgumentList(Expression arg, ArgumentList rest) {
        this._arg = arg;
        this._rest = rest;
    }

    public String toString() {
        if (this._rest == null) {
            return this._arg.toString();
        }
        return this._arg.toString() + ", " + this._rest.toString();
    }
}
