package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/GenerateIdCall.class */
final class GenerateIdCall extends FunctionCall {
    public GenerateIdCall(QName fname, Vector arguments) {
        super(fname, arguments);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.FunctionCall, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        InstructionList il = methodGen.getInstructionList();
        if (argumentCount() == 0) {
            il.append(methodGen.loadContextNode());
        } else {
            argument().translate(classGen, methodGen);
        }
        ConstantPoolGen cpg = classGen.getConstantPool();
        il.append(new INVOKESTATIC(cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "generate_idF", "(I)Ljava/lang/String;")));
    }
}
