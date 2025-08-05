package com.sun.org.apache.xalan.internal.xsltc.compiler.util;

import com.sun.org.apache.bcel.internal.generic.BranchHandle;
import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.CHECKCAST;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.GOTO;
import com.sun.org.apache.bcel.internal.generic.IFEQ;
import com.sun.org.apache.bcel.internal.generic.IFGE;
import com.sun.org.apache.bcel.internal.generic.IFGT;
import com.sun.org.apache.bcel.internal.generic.IFLE;
import com.sun.org.apache.bcel.internal.generic.IFLT;
import com.sun.org.apache.bcel.internal.generic.IF_ICMPGE;
import com.sun.org.apache.bcel.internal.generic.IF_ICMPGT;
import com.sun.org.apache.bcel.internal.generic.IF_ICMPLE;
import com.sun.org.apache.bcel.internal.generic.IF_ICMPLT;
import com.sun.org.apache.bcel.internal.generic.ILOAD;
import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.ISTORE;
import com.sun.org.apache.bcel.internal.generic.Instruction;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/util/BooleanType.class */
public final class BooleanType extends Type {
    protected BooleanType() {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public String toString() {
        return "boolean";
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public boolean identicalTo(Type other) {
        return this == other;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public String toSignature() {
        return Constants.HASIDCALL_INDEX_SIG;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public boolean isSimple() {
        return true;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public com.sun.org.apache.bcel.internal.generic.Type toJCType() {
        return com.sun.org.apache.bcel.internal.generic.Type.BOOLEAN;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Type type) {
        if (type == Type.String) {
            translateTo(classGen, methodGen, (StringType) type);
            return;
        }
        if (type == Type.Real) {
            translateTo(classGen, methodGen, (RealType) type);
        } else if (type == Type.Reference) {
            translateTo(classGen, methodGen, (ReferenceType) type);
        } else {
            ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), type.toString());
            classGen.getParser().reportError(2, err);
        }
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, StringType type) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        BranchHandle falsec = il.append((BranchInstruction) new IFEQ(null));
        il.append(new PUSH(cpg, "true"));
        BranchHandle truec = il.append((BranchInstruction) new GOTO(null));
        falsec.setTarget(il.append(new PUSH(cpg, "false")));
        truec.setTarget(il.append(NOP));
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, RealType type) {
        methodGen.getInstructionList().append(I2D);
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ReferenceType type) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        il.append(new NEW(cpg.addClass(Constants.BOOLEAN_CLASS)));
        il.append(DUP_X1);
        il.append(SWAP);
        il.append(new INVOKESPECIAL(cpg.addMethodref(Constants.BOOLEAN_CLASS, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "(Z)V")));
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Class clazz) {
        if (clazz == Boolean.TYPE) {
            methodGen.getInstructionList().append(NOP);
        } else if (clazz.isAssignableFrom(Boolean.class)) {
            translateTo(classGen, methodGen, Type.Reference);
        } else {
            ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), clazz.getName());
            classGen.getParser().reportError(2, err);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public void translateFrom(ClassGenerator classGen, MethodGenerator methodGen, Class clazz) {
        translateTo(classGen, methodGen, clazz);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public void translateBox(ClassGenerator classGen, MethodGenerator methodGen) {
        translateTo(classGen, methodGen, Type.Reference);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public void translateUnBox(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        il.append(new CHECKCAST(cpg.addClass(Constants.BOOLEAN_CLASS)));
        il.append(new INVOKEVIRTUAL(cpg.addMethodref(Constants.BOOLEAN_CLASS, Constants.BOOLEAN_VALUE, Constants.BOOLEAN_VALUE_SIG)));
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public Instruction LOAD(int slot) {
        return new ILOAD(slot);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public Instruction STORE(int slot) {
        return new ISTORE(slot);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public BranchInstruction GT(boolean tozero) {
        return tozero ? new IFGT(null) : new IF_ICMPGT(null);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public BranchInstruction GE(boolean tozero) {
        return tozero ? new IFGE(null) : new IF_ICMPGE(null);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public BranchInstruction LT(boolean tozero) {
        return tozero ? new IFLT(null) : new IF_ICMPLT(null);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public BranchInstruction LE(boolean tozero) {
        return tozero ? new IFLE(null) : new IF_ICMPLE(null);
    }
}
