package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ALOAD;
import com.sun.org.apache.bcel.internal.generic.ASTORE;
import com.sun.org.apache.bcel.internal.generic.BranchHandle;
import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.IFEQ;
import com.sun.org.apache.bcel.internal.generic.IFNULL;
import com.sun.org.apache.bcel.internal.generic.ILOAD;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.ISTORE;
import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/Copy.class */
final class Copy extends Instruction {
    private UseAttributeSets _useSets;

    Copy() {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        String useSets = getAttribute(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_USEATTRIBUTESETS);
        if (useSets.length() > 0) {
            if (!Util.isValidQNames(useSets)) {
                ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", (Object) useSets, (SyntaxTreeNode) this);
                parser.reportError(3, err);
            }
            this._useSets = new UseAttributeSets(useSets, parser);
        }
        parseChildren(parser);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void display(int indent) {
        indent(indent);
        Util.println("Copy");
        indent(indent + 4);
        displayContents(indent + 4);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (this._useSets != null) {
            this._useSets.typeCheck(stable);
        }
        typeCheckContents(stable);
        return Type.Void;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        LocalVariableGen name = methodGen.addLocalVariable2("name", Util.getJCRefType(Constants.STRING_SIG), null);
        LocalVariableGen length = methodGen.addLocalVariable2("length", Util.getJCRefType("I"), null);
        il.append(methodGen.loadDOM());
        il.append(methodGen.loadCurrentNode());
        il.append(methodGen.loadHandler());
        int cpy = cpg.addInterfaceMethodref(Constants.DOM_INTF, "shallowCopy", "(ILcom/sun/org/apache/xml/internal/serializer/SerializationHandler;)Ljava/lang/String;");
        il.append(new INVOKEINTERFACE(cpy, 3));
        il.append(DUP);
        name.setStart(il.append(new ASTORE(name.getIndex())));
        BranchHandle ifBlock1 = il.append((BranchInstruction) new IFNULL(null));
        il.append(new ALOAD(name.getIndex()));
        int lengthMethod = cpg.addMethodref("java.lang.String", "length", "()I");
        il.append(new INVOKEVIRTUAL(lengthMethod));
        il.append(DUP);
        length.setStart(il.append(new ISTORE(length.getIndex())));
        BranchHandle ifBlock4 = il.append((BranchInstruction) new IFEQ(null));
        if (this._useSets != null) {
            SyntaxTreeNode parent = getParent();
            if ((parent instanceof LiteralElement) || (parent instanceof LiteralElement)) {
                this._useSets.translate(classGen, methodGen);
            } else {
                il.append(new ILOAD(length.getIndex()));
                BranchHandle ifBlock2 = il.append((BranchInstruction) new IFEQ(null));
                this._useSets.translate(classGen, methodGen);
                ifBlock2.setTarget(il.append(NOP));
            }
        }
        ifBlock4.setTarget(il.append(NOP));
        translateContents(classGen, methodGen);
        length.setEnd(il.append(new ILOAD(length.getIndex())));
        BranchHandle ifBlock3 = il.append((BranchInstruction) new IFEQ(null));
        il.append(methodGen.loadHandler());
        name.setEnd(il.append(new ALOAD(name.getIndex())));
        il.append(methodGen.endElement());
        InstructionHandle end = il.append(NOP);
        ifBlock1.setTarget(end);
        ifBlock3.setTarget(end);
        methodGen.removeLocalVariable(name);
        methodGen.removeLocalVariable(length);
    }
}
