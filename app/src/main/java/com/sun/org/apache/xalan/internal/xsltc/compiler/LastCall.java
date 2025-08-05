package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.ILOAD;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.CompareGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TestGenerator;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/LastCall.class */
final class LastCall extends FunctionCall {
    public LastCall(QName fname) {
        super(fname);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public boolean hasPositionCall() {
        return true;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public boolean hasLastCall() {
        return true;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.FunctionCall, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        InstructionList il = methodGen.getInstructionList();
        if (methodGen instanceof CompareGenerator) {
            il.append(((CompareGenerator) methodGen).loadLastNode());
            return;
        }
        if (methodGen instanceof TestGenerator) {
            il.append(new ILOAD(3));
            return;
        }
        ConstantPoolGen cpg = classGen.getConstantPool();
        int getLast = cpg.addInterfaceMethodref(Constants.NODE_ITERATOR, "getLast", "()I");
        il.append(methodGen.loadIterator());
        il.append(new INVOKEINTERFACE(getLast, 1));
    }
}
