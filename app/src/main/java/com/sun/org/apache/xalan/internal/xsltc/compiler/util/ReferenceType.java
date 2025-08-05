package com.sun.org.apache.xalan.internal.xsltc.compiler.util;

import com.sun.org.apache.bcel.internal.generic.ALOAD;
import com.sun.org.apache.bcel.internal.generic.ASTORE;
import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.IFEQ;
import com.sun.org.apache.bcel.internal.generic.ILOAD;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import com.sun.org.apache.bcel.internal.generic.Instruction;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import com.sun.org.apache.xalan.internal.xsltc.compiler.FlowList;
import com.sun.org.apache.xpath.internal.compiler.Keywords;
import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/util/ReferenceType.class */
public final class ReferenceType extends Type {
    protected ReferenceType() {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public String toString() {
        return FXMLLoader.REFERENCE_TAG;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public boolean identicalTo(Type other) {
        return this == other;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public String toSignature() {
        return Constants.OBJECT_SIG;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public com.sun.org.apache.bcel.internal.generic.Type toJCType() {
        return com.sun.org.apache.bcel.internal.generic.Type.OBJECT;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Type type) {
        if (type == Type.String) {
            translateTo(classGen, methodGen, (StringType) type);
            return;
        }
        if (type == Type.Real) {
            translateTo(classGen, methodGen, (RealType) type);
            return;
        }
        if (type == Type.Boolean) {
            translateTo(classGen, methodGen, (BooleanType) type);
            return;
        }
        if (type == Type.NodeSet) {
            translateTo(classGen, methodGen, (NodeSetType) type);
            return;
        }
        if (type == Type.Node) {
            translateTo(classGen, methodGen, (NodeType) type);
            return;
        }
        if (type == Type.ResultTree) {
            translateTo(classGen, methodGen, (ResultTreeType) type);
            return;
        }
        if (type == Type.Object) {
            translateTo(classGen, methodGen, (ObjectType) type);
        } else if (type != Type.Reference) {
            ErrorMsg err = new ErrorMsg(ErrorMsg.INTERNAL_ERR, type.toString());
            classGen.getParser().reportError(2, err);
        }
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, StringType type) {
        int current = methodGen.getLocalIndex(Keywords.FUNC_CURRENT_STRING);
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        if (current < 0) {
            il.append(new PUSH(cpg, 0));
        } else {
            il.append(new ILOAD(current));
        }
        il.append(methodGen.loadDOM());
        int stringF = cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "stringF", "(Ljava/lang/Object;ILcom/sun/org/apache/xalan/internal/xsltc/DOM;)Ljava/lang/String;");
        il.append(new INVOKESTATIC(stringF));
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, RealType type) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        il.append(methodGen.loadDOM());
        int index = cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "numberF", "(Ljava/lang/Object;Lcom/sun/org/apache/xalan/internal/xsltc/DOM;)D");
        il.append(new INVOKESTATIC(index));
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        int index = cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "booleanF", "(Ljava/lang/Object;)Z");
        il.append(new INVOKESTATIC(index));
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, NodeSetType type) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        int index = cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "referenceToNodeSet", "(Ljava/lang/Object;)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
        il.append(new INVOKESTATIC(index));
        int index2 = cpg.addInterfaceMethodref(Constants.NODE_ITERATOR, Constants.RESET, "()Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
        il.append(new INVOKEINTERFACE(index2, 1));
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, NodeType type) {
        translateTo(classGen, methodGen, Type.NodeSet);
        Type.NodeSet.translateTo(classGen, methodGen, type);
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ResultTreeType type) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        int index = cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "referenceToResultTree", "(Ljava/lang/Object;)Lcom/sun/org/apache/xalan/internal/xsltc/DOM;");
        il.append(new INVOKESTATIC(index));
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ObjectType type) {
        methodGen.getInstructionList().append(NOP);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Class clazz) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        int referenceToLong = cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "referenceToLong", "(Ljava/lang/Object;)J");
        int referenceToDouble = cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "referenceToDouble", "(Ljava/lang/Object;)D");
        int referenceToBoolean = cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "referenceToBoolean", "(Ljava/lang/Object;)Z");
        if (clazz.getName().equals(Constants.OBJECT_CLASS)) {
            il.append(NOP);
            return;
        }
        if (clazz == Double.TYPE) {
            il.append(new INVOKESTATIC(referenceToDouble));
            return;
        }
        if (clazz.getName().equals(Constants.DOUBLE_CLASS)) {
            il.append(new INVOKESTATIC(referenceToDouble));
            Type.Real.translateTo(classGen, methodGen, Type.Reference);
            return;
        }
        if (clazz == Float.TYPE) {
            il.append(new INVOKESTATIC(referenceToDouble));
            il.append(D2F);
            return;
        }
        if (clazz.getName().equals("java.lang.String")) {
            int index = cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "referenceToString", "(Ljava/lang/Object;Lcom/sun/org/apache/xalan/internal/xsltc/DOM;)Ljava/lang/String;");
            il.append(methodGen.loadDOM());
            il.append(new INVOKESTATIC(index));
            return;
        }
        if (clazz.getName().equals("org.w3c.dom.Node")) {
            int index2 = cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "referenceToNode", "(Ljava/lang/Object;Lcom/sun/org/apache/xalan/internal/xsltc/DOM;)Lorg/w3c/dom/Node;");
            il.append(methodGen.loadDOM());
            il.append(new INVOKESTATIC(index2));
            return;
        }
        if (clazz.getName().equals("org.w3c.dom.NodeList")) {
            int index3 = cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "referenceToNodeList", "(Ljava/lang/Object;Lcom/sun/org/apache/xalan/internal/xsltc/DOM;)Lorg/w3c/dom/NodeList;");
            il.append(methodGen.loadDOM());
            il.append(new INVOKESTATIC(index3));
            return;
        }
        if (clazz.getName().equals(Constants.DOM_INTF)) {
            translateTo(classGen, methodGen, Type.ResultTree);
            return;
        }
        if (clazz == Long.TYPE) {
            il.append(new INVOKESTATIC(referenceToLong));
            return;
        }
        if (clazz == Integer.TYPE) {
            il.append(new INVOKESTATIC(referenceToLong));
            il.append(L2I);
            return;
        }
        if (clazz == Short.TYPE) {
            il.append(new INVOKESTATIC(referenceToLong));
            il.append(L2I);
            il.append(I2S);
            return;
        }
        if (clazz == Byte.TYPE) {
            il.append(new INVOKESTATIC(referenceToLong));
            il.append(L2I);
            il.append(I2B);
        } else if (clazz == Character.TYPE) {
            il.append(new INVOKESTATIC(referenceToLong));
            il.append(L2I);
            il.append(I2C);
        } else if (clazz == Boolean.TYPE) {
            il.append(new INVOKESTATIC(referenceToBoolean));
        } else if (clazz.getName().equals(Constants.BOOLEAN_CLASS)) {
            il.append(new INVOKESTATIC(referenceToBoolean));
            Type.Boolean.translateTo(classGen, methodGen, Type.Reference);
        } else {
            ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), clazz.getName());
            classGen.getParser().reportError(2, err);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public void translateFrom(ClassGenerator classGen, MethodGenerator methodGen, Class clazz) {
        if (clazz.getName().equals(Constants.OBJECT_CLASS)) {
            methodGen.getInstructionList().append(NOP);
        } else {
            ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), clazz.getName());
            classGen.getParser().reportError(2, err);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public FlowList translateToDesynthesized(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type) {
        InstructionList il = methodGen.getInstructionList();
        translateTo(classGen, methodGen, type);
        return new FlowList(il.append((BranchInstruction) new IFEQ(null)));
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public void translateBox(ClassGenerator classGen, MethodGenerator methodGen) {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public void translateUnBox(ClassGenerator classGen, MethodGenerator methodGen) {
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
