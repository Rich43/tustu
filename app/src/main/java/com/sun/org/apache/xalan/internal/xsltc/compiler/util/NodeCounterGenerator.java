package com.sun.org.apache.xalan.internal.xsltc.compiler.util;

import com.sun.org.apache.bcel.internal.generic.ALOAD;
import com.sun.org.apache.bcel.internal.generic.Instruction;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Stylesheet;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/util/NodeCounterGenerator.class */
public final class NodeCounterGenerator extends ClassGenerator {
    private Instruction _aloadTranslet;

    public NodeCounterGenerator(String className, String superClassName, String fileName, int accessFlags, String[] interfaces, Stylesheet stylesheet) {
        super(className, superClassName, fileName, accessFlags, interfaces, stylesheet);
    }

    public void setTransletIndex(int index) {
        this._aloadTranslet = new ALOAD(index);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator
    public Instruction loadTranslet() {
        return this._aloadTranslet;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator
    public boolean isExternal() {
        return true;
    }
}
