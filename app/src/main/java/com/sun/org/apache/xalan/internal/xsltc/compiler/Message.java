package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/Message.class */
final class Message extends Instruction {
    private boolean _terminate = false;

    Message() {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        String termstr = getAttribute(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_TERMINATE);
        if (termstr != null) {
            this._terminate = termstr.equals("yes");
        }
        parseChildren(parser);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        typeCheckContents(stable);
        return Type.Void;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        il.append(classGen.loadTranslet());
        switch (elementCount()) {
            case 0:
                il.append(new PUSH(cpg, ""));
                break;
            case 1:
                SyntaxTreeNode child = elementAt(0);
                if (child instanceof Text) {
                    il.append(new PUSH(cpg, ((Text) child).getText()));
                    break;
                }
            default:
                il.append(methodGen.loadHandler());
                il.append(new NEW(cpg.addClass(Constants.STREAM_XML_OUTPUT)));
                il.append(methodGen.storeHandler());
                il.append(new NEW(cpg.addClass(Constants.STRING_WRITER)));
                il.append(DUP);
                il.append(DUP);
                il.append(new INVOKESPECIAL(cpg.addMethodref(Constants.STRING_WRITER, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "()V")));
                il.append(methodGen.loadHandler());
                il.append(new INVOKESPECIAL(cpg.addMethodref(Constants.STREAM_XML_OUTPUT, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "()V")));
                il.append(methodGen.loadHandler());
                il.append(SWAP);
                il.append(new INVOKEINTERFACE(cpg.addInterfaceMethodref(Constants.TRANSLET_OUTPUT_INTERFACE, "setWriter", "(Ljava/io/Writer;)V"), 2));
                il.append(methodGen.loadHandler());
                il.append(new PUSH(cpg, "UTF-8"));
                il.append(new INVOKEINTERFACE(cpg.addInterfaceMethodref(Constants.TRANSLET_OUTPUT_INTERFACE, "setEncoding", "(Ljava/lang/String;)V"), 2));
                il.append(methodGen.loadHandler());
                il.append(ICONST_1);
                il.append(new INVOKEINTERFACE(cpg.addInterfaceMethodref(Constants.TRANSLET_OUTPUT_INTERFACE, "setOmitXMLDeclaration", "(Z)V"), 2));
                il.append(methodGen.loadHandler());
                il.append(new INVOKEINTERFACE(cpg.addInterfaceMethodref(Constants.TRANSLET_OUTPUT_INTERFACE, "startDocument", "()V"), 1));
                translateContents(classGen, methodGen);
                il.append(methodGen.loadHandler());
                il.append(new INVOKEINTERFACE(cpg.addInterfaceMethodref(Constants.TRANSLET_OUTPUT_INTERFACE, "endDocument", "()V"), 1));
                il.append(new INVOKEVIRTUAL(cpg.addMethodref(Constants.STRING_WRITER, "toString", "()Ljava/lang/String;")));
                il.append(SWAP);
                il.append(methodGen.storeHandler());
                break;
        }
        il.append(new INVOKEVIRTUAL(cpg.addMethodref(Constants.TRANSLET_CLASS, "displayMessage", "(Ljava/lang/String;)V")));
        if (this._terminate) {
            int einit = cpg.addMethodref("java.lang.RuntimeException", com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "(Ljava/lang/String;)V");
            il.append(new NEW(cpg.addClass("java.lang.RuntimeException")));
            il.append(DUP);
            il.append(new PUSH(cpg, "Termination forced by an xsl:message instruction"));
            il.append(new INVOKESPECIAL(einit));
            il.append(ATHROW);
        }
    }
}
