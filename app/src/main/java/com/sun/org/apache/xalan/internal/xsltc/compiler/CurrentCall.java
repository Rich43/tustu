package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/CurrentCall.class */
final class CurrentCall extends FunctionCall {
    public CurrentCall(QName fname) {
        super(fname);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.FunctionCall, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        methodGen.getInstructionList().append(methodGen.loadCurrentNode());
    }
}
