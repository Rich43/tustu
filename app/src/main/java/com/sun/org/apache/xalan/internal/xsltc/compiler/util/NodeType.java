package com.sun.org.apache.xalan.internal.xsltc.compiler.util;

import com.sun.org.apache.bcel.internal.generic.BranchHandle;
import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.CHECKCAST;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.GETFIELD;
import com.sun.org.apache.bcel.internal.generic.GOTO;
import com.sun.org.apache.bcel.internal.generic.IFEQ;
import com.sun.org.apache.bcel.internal.generic.ILOAD;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
import com.sun.org.apache.bcel.internal.generic.ISTORE;
import com.sun.org.apache.bcel.internal.generic.Instruction;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import com.sun.org.apache.xalan.internal.xsltc.compiler.FlowList;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/util/NodeType.class */
public final class NodeType extends Type {
    private final int _type;

    protected NodeType() {
        this(-1);
    }

    protected NodeType(int type) {
        this._type = type;
    }

    public int getType() {
        return this._type;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public String toString() {
        return "node-type";
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public boolean identicalTo(Type other) {
        return other instanceof NodeType;
    }

    public int hashCode() {
        return this._type;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public String toSignature() {
        return "I";
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public com.sun.org.apache.bcel.internal.generic.Type toJCType() {
        return com.sun.org.apache.bcel.internal.generic.Type.INT;
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
        if (type == Type.NodeSet) {
            translateTo(classGen, methodGen, (NodeSetType) type);
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

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, StringType type) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        switch (this._type) {
            case -1:
            case 2:
            case 7:
            case 8:
                il.append(methodGen.loadDOM());
                il.append(SWAP);
                int index = cpg.addInterfaceMethodref(Constants.DOM_INTF, Constants.GET_NODE_VALUE, "(I)Ljava/lang/String;");
                il.append(new INVOKEINTERFACE(index, 2));
                break;
            case 0:
            case 3:
            case 4:
            case 5:
            case 6:
            default:
                ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), type.toString());
                classGen.getParser().reportError(2, err);
                break;
            case 1:
            case 9:
                il.append(methodGen.loadDOM());
                il.append(SWAP);
                int index2 = cpg.addInterfaceMethodref(Constants.DOM_INTF, Constants.GET_ELEMENT_VALUE, "(I)Ljava/lang/String;");
                il.append(new INVOKEINTERFACE(index2, 2));
                break;
        }
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type) {
        InstructionList il = methodGen.getInstructionList();
        FlowList falsel = translateToDesynthesized(classGen, methodGen, type);
        il.append(ICONST_1);
        BranchHandle truec = il.append((BranchInstruction) new GOTO(null));
        falsel.backPatch(il.append(ICONST_0));
        truec.setTarget(il.append(NOP));
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, RealType type) {
        translateTo(classGen, methodGen, Type.String);
        Type.String.translateTo(classGen, methodGen, Type.Real);
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, NodeSetType type) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        il.append(new NEW(cpg.addClass(Constants.SINGLETON_ITERATOR)));
        il.append(DUP_X1);
        il.append(SWAP);
        int init = cpg.addMethodref(Constants.SINGLETON_ITERATOR, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "(I)V");
        il.append(new INVOKESPECIAL(init));
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ObjectType type) {
        methodGen.getInstructionList().append(NOP);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public FlowList translateToDesynthesized(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type) {
        InstructionList il = methodGen.getInstructionList();
        return new FlowList(il.append((BranchInstruction) new IFEQ(null)));
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ReferenceType type) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        il.append(new NEW(cpg.addClass(Constants.RUNTIME_NODE_CLASS)));
        il.append(DUP_X1);
        il.append(SWAP);
        il.append(new PUSH(cpg, this._type));
        il.append(new INVOKESPECIAL(cpg.addMethodref(Constants.RUNTIME_NODE_CLASS, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "(II)V")));
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Class clazz) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        String className = clazz.getName();
        if (className.equals("java.lang.String")) {
            translateTo(classGen, methodGen, Type.String);
            return;
        }
        il.append(methodGen.loadDOM());
        il.append(SWAP);
        if (className.equals("org.w3c.dom.Node") || className.equals(Constants.OBJECT_CLASS)) {
            int index = cpg.addInterfaceMethodref(Constants.DOM_INTF, Constants.MAKE_NODE, Constants.MAKE_NODE_SIG);
            il.append(new INVOKEINTERFACE(index, 2));
        } else if (className.equals("org.w3c.dom.NodeList")) {
            int index2 = cpg.addInterfaceMethodref(Constants.DOM_INTF, Constants.MAKE_NODE_LIST, Constants.MAKE_NODE_LIST_SIG);
            il.append(new INVOKEINTERFACE(index2, 2));
        } else {
            ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), className);
            classGen.getParser().reportError(2, err);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public void translateBox(ClassGenerator classGen, MethodGenerator methodGen) {
        translateTo(classGen, methodGen, Type.Reference);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public void translateUnBox(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        il.append(new CHECKCAST(cpg.addClass(Constants.RUNTIME_NODE_CLASS)));
        il.append(new GETFIELD(cpg.addFieldref(Constants.RUNTIME_NODE_CLASS, "node", "I")));
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public String getClassName() {
        return Constants.RUNTIME_NODE_CLASS;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public Instruction LOAD(int slot) {
        return new ILOAD(slot);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public Instruction STORE(int slot) {
        return new ISTORE(slot);
    }
}
