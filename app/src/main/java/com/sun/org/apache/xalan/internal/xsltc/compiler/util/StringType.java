package com.sun.org.apache.xalan.internal.xsltc.compiler.util;

import com.sun.org.apache.bcel.internal.generic.ALOAD;
import com.sun.org.apache.bcel.internal.generic.ASTORE;
import com.sun.org.apache.bcel.internal.generic.BranchHandle;
import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.GOTO;
import com.sun.org.apache.bcel.internal.generic.IFEQ;
import com.sun.org.apache.bcel.internal.generic.IFNONNULL;
import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.Instruction;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import com.sun.org.apache.xalan.internal.xsltc.compiler.FlowList;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/util/StringType.class */
public class StringType extends Type {
    protected StringType() {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public String toString() {
        return "string";
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public boolean identicalTo(Type other) {
        return this == other;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public String toSignature() {
        return Constants.STRING_SIG;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public boolean isSimple() {
        return true;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public com.sun.org.apache.bcel.internal.generic.Type toJCType() {
        return com.sun.org.apache.bcel.internal.generic.Type.STRING;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Type type) {
        if (type == Type.Boolean) {
            translateTo(classGen, methodGen, (BooleanType) type);
            return;
        }
        if (type == Type.Real) {
            translateTo(classGen, methodGen, (RealType) type);
            return;
        }
        if (type == Type.Reference) {
            translateTo(classGen, methodGen, (ReferenceType) type);
        } else if (type != Type.ObjectString) {
            ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), type.toString());
            classGen.getParser().reportError(2, err);
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
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        il.append(new INVOKESTATIC(cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, Constants.STRING_TO_REAL, Constants.STRING_TO_REAL_SIG)));
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public FlowList translateToDesynthesized(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        il.append(new INVOKEVIRTUAL(cpg.addMethodref("java.lang.String", "length", "()I")));
        return new FlowList(il.append((BranchInstruction) new IFEQ(null)));
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ReferenceType type) {
        methodGen.getInstructionList().append(NOP);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Class clazz) {
        if (clazz.isAssignableFrom(String.class)) {
            methodGen.getInstructionList().append(NOP);
        } else {
            ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), clazz.getName());
            classGen.getParser().reportError(2, err);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public void translateFrom(ClassGenerator classGen, MethodGenerator methodGen, Class clazz) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        if (clazz.getName().equals("java.lang.String")) {
            il.append(DUP);
            BranchHandle ifNonNull = il.append((BranchInstruction) new IFNONNULL(null));
            il.append(POP);
            il.append(new PUSH(cpg, ""));
            ifNonNull.setTarget(il.append(NOP));
            return;
        }
        ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), clazz.getName());
        classGen.getParser().reportError(2, err);
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
        return "java.lang.String";
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
