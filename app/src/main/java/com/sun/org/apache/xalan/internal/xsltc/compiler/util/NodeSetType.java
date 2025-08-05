package com.sun.org.apache.xalan.internal.xsltc.compiler.util;

import com.sun.org.apache.bcel.internal.generic.ALOAD;
import com.sun.org.apache.bcel.internal.generic.ASTORE;
import com.sun.org.apache.bcel.internal.generic.BranchHandle;
import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.GOTO;
import com.sun.org.apache.bcel.internal.generic.IFLT;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import com.sun.org.apache.bcel.internal.generic.Instruction;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import com.sun.org.apache.xalan.internal.xsltc.compiler.FlowList;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/util/NodeSetType.class */
public final class NodeSetType extends Type {
    protected NodeSetType() {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public String toString() {
        return "node-set";
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public boolean identicalTo(Type other) {
        return this == other;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public String toSignature() {
        return "Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;";
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public com.sun.org.apache.bcel.internal.generic.Type toJCType() {
        return new com.sun.org.apache.bcel.internal.generic.ObjectType(Constants.NODE_ITERATOR);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Type type) {
        if (type == Type.String) {
            translateTo(classGen, methodGen, (StringType) type);
            return;
        }
        if (type == Type.Boolean) {
            translateTo(classGen, methodGen, (BooleanType) type);
            return;
        }
        if (type == Type.Real) {
            translateTo(classGen, methodGen, (RealType) type);
            return;
        }
        if (type == Type.Node) {
            translateTo(classGen, methodGen, (NodeType) type);
            return;
        }
        if (type == Type.Reference) {
            translateTo(classGen, methodGen, (ReferenceType) type);
        } else if (type == Type.Object) {
            translateTo(classGen, methodGen, (ObjectType) type);
        } else {
            ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), type.toString());
            classGen.getParser().reportError(2, err);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public void translateFrom(ClassGenerator classGen, MethodGenerator methodGen, Class clazz) {
        InstructionList il = methodGen.getInstructionList();
        ConstantPoolGen cpg = classGen.getConstantPool();
        if (clazz.getName().equals("org.w3c.dom.NodeList")) {
            il.append(classGen.loadTranslet());
            il.append(methodGen.loadDOM());
            int convert = cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "nodeList2Iterator", "(Lorg/w3c/dom/NodeList;Lcom/sun/org/apache/xalan/internal/xsltc/Translet;Lcom/sun/org/apache/xalan/internal/xsltc/DOM;)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
            il.append(new INVOKESTATIC(convert));
            return;
        }
        if (clazz.getName().equals("org.w3c.dom.Node")) {
            il.append(classGen.loadTranslet());
            il.append(methodGen.loadDOM());
            int convert2 = cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "node2Iterator", "(Lorg/w3c/dom/Node;Lcom/sun/org/apache/xalan/internal/xsltc/Translet;Lcom/sun/org/apache/xalan/internal/xsltc/DOM;)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
            il.append(new INVOKESTATIC(convert2));
            return;
        }
        ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), clazz.getName());
        classGen.getParser().reportError(2, err);
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type) {
        InstructionList il = methodGen.getInstructionList();
        FlowList falsel = translateToDesynthesized(classGen, methodGen, type);
        il.append(ICONST_1);
        BranchHandle truec = il.append((BranchInstruction) new GOTO(null));
        falsel.backPatch(il.append(ICONST_0));
        truec.setTarget(il.append(NOP));
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, StringType type) {
        InstructionList il = methodGen.getInstructionList();
        getFirstNode(classGen, methodGen);
        il.append(DUP);
        BranchHandle falsec = il.append((BranchInstruction) new IFLT(null));
        Type.Node.translateTo(classGen, methodGen, type);
        BranchHandle truec = il.append((BranchInstruction) new GOTO(null));
        falsec.setTarget(il.append(POP));
        il.append(new PUSH(classGen.getConstantPool(), ""));
        truec.setTarget(il.append(NOP));
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, RealType type) {
        translateTo(classGen, methodGen, Type.String);
        Type.String.translateTo(classGen, methodGen, Type.Real);
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, NodeType type) {
        getFirstNode(classGen, methodGen);
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ObjectType type) {
        methodGen.getInstructionList().append(NOP);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public FlowList translateToDesynthesized(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type) {
        InstructionList il = methodGen.getInstructionList();
        getFirstNode(classGen, methodGen);
        return new FlowList(il.append((BranchInstruction) new IFLT(null)));
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ReferenceType type) {
        methodGen.getInstructionList().append(NOP);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Class clazz) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        String className = clazz.getName();
        il.append(methodGen.loadDOM());
        il.append(SWAP);
        if (className.equals("org.w3c.dom.Node")) {
            int index = cpg.addInterfaceMethodref(Constants.DOM_INTF, Constants.MAKE_NODE, Constants.MAKE_NODE_SIG2);
            il.append(new INVOKEINTERFACE(index, 2));
            return;
        }
        if (className.equals("org.w3c.dom.NodeList") || className.equals(Constants.OBJECT_CLASS)) {
            int index2 = cpg.addInterfaceMethodref(Constants.DOM_INTF, Constants.MAKE_NODE_LIST, Constants.MAKE_NODE_LIST_SIG2);
            il.append(new INVOKEINTERFACE(index2, 2));
        } else {
            if (className.equals("java.lang.String")) {
                int next = cpg.addInterfaceMethodref(Constants.NODE_ITERATOR, Constants.NEXT, "()I");
                int index3 = cpg.addInterfaceMethodref(Constants.DOM_INTF, Constants.GET_NODE_VALUE, "(I)Ljava/lang/String;");
                il.append(new INVOKEINTERFACE(next, 1));
                il.append(new INVOKEINTERFACE(index3, 2));
                return;
            }
            ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), className);
            classGen.getParser().reportError(2, err);
        }
    }

    private void getFirstNode(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        il.append(new INVOKEINTERFACE(cpg.addInterfaceMethodref(Constants.NODE_ITERATOR, Constants.NEXT, "()I"), 1));
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public void translateBox(ClassGenerator classGen, MethodGenerator methodGen) {
        translateTo(classGen, methodGen, Type.Reference);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public void translateUnBox(ClassGenerator classGen, MethodGenerator methodGen) {
        methodGen.getInstructionList().append(NOP);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public String getClassName() {
        return Constants.NODE_ITERATOR;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public Instruction LOAD(int slot) {
        return new ALOAD(slot);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public Instruction STORE(int slot) {
        return new ASTORE(slot);
    }
}
