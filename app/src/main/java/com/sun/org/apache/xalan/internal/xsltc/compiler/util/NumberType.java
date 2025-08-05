package com.sun.org.apache.xalan.internal.xsltc.compiler.util;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/util/NumberType.class */
public abstract class NumberType extends Type {
    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public boolean isNumber() {
        return true;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public boolean isSimple() {
        return true;
    }
}
