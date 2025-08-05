package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/NameCall.class */
final class NameCall extends NameBase {
    public NameCall(QName fname) {
        super(fname);
    }

    public NameCall(QName fname, Vector arguments) {
        super(fname, arguments);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.NameBase, com.sun.org.apache.xalan.internal.xsltc.compiler.FunctionCall, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        int getName = cpg.addInterfaceMethodref(Constants.DOM_INTF, Constants.GET_NODE_NAME, "(I)Ljava/lang/String;");
        super.translate(classGen, methodGen);
        il.append(new INVOKEINTERFACE(getName, 2));
    }
}
