package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/LocalNameCall.class */
final class LocalNameCall extends NameBase {
    public LocalNameCall(QName fname) {
        super(fname);
    }

    public LocalNameCall(QName fname, Vector arguments) {
        super(fname, arguments);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.NameBase, com.sun.org.apache.xalan.internal.xsltc.compiler.FunctionCall, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        int getNodeName = cpg.addInterfaceMethodref(Constants.DOM_INTF, "getNodeName", "(I)Ljava/lang/String;");
        int getLocalName = cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "getLocalName", Constants.GET_UNPARSED_ENTITY_URI_SIG);
        super.translate(classGen, methodGen);
        il.append(new INVOKEINTERFACE(getNodeName, 2));
        il.append(new INVOKESTATIC(getLocalName));
    }
}
