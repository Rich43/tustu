package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.GETFIELD;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/Comment.class */
final class Comment extends Instruction {
    Comment() {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        parseChildren(parser);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        typeCheckContents(stable);
        return Type.String;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        Text rawText = null;
        if (elementCount() == 1) {
            Object content = elementAt(0);
            if (content instanceof Text) {
                rawText = (Text) content;
            }
        }
        if (rawText != null) {
            il.append(methodGen.loadHandler());
            if (rawText.canLoadAsArrayOffsetLength()) {
                rawText.loadAsArrayOffsetLength(classGen, methodGen);
                int comment = cpg.addInterfaceMethodref(Constants.TRANSLET_OUTPUT_INTERFACE, "comment", "([CII)V");
                il.append(new INVOKEINTERFACE(comment, 4));
                return;
            } else {
                il.append(new PUSH(cpg, rawText.getText()));
                int comment2 = cpg.addInterfaceMethodref(Constants.TRANSLET_OUTPUT_INTERFACE, "comment", "(Ljava/lang/String;)V");
                il.append(new INVOKEINTERFACE(comment2, 2));
                return;
            }
        }
        il.append(methodGen.loadHandler());
        il.append(DUP);
        il.append(classGen.loadTranslet());
        il.append(new GETFIELD(cpg.addFieldref(Constants.TRANSLET_CLASS, "stringValueHandler", Constants.STRING_VALUE_HANDLER_SIG)));
        il.append(DUP);
        il.append(methodGen.storeHandler());
        translateContents(classGen, methodGen);
        il.append(new INVOKEVIRTUAL(cpg.addMethodref(Constants.STRING_VALUE_HANDLER, "getValue", "()Ljava/lang/String;")));
        int comment3 = cpg.addInterfaceMethodref(Constants.TRANSLET_OUTPUT_INTERFACE, "comment", "(Ljava/lang/String;)V");
        il.append(new INVOKEINTERFACE(comment3, 2));
        il.append(methodGen.storeHandler());
    }
}
