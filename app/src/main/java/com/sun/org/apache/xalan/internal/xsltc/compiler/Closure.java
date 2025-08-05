package com.sun.org.apache.xalan.internal.xsltc.compiler;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/Closure.class */
public interface Closure {
    boolean inInnerClass();

    Closure getParentClosure();

    String getInnerClassName();

    void addVariable(VariableRefBase variableRefBase);
}
