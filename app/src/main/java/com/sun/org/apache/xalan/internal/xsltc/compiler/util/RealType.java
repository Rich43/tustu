package com.sun.org.apache.xalan.internal.xsltc.compiler.util;

import com.sun.org.apache.bcel.internal.generic.BranchHandle;
import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.CHECKCAST;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.DLOAD;
import com.sun.org.apache.bcel.internal.generic.DSTORE;
import com.sun.org.apache.bcel.internal.generic.GOTO;
import com.sun.org.apache.bcel.internal.generic.IFEQ;
import com.sun.org.apache.bcel.internal.generic.IFNE;
import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.Instruction;
import com.sun.org.apache.bcel.internal.generic.InstructionConstants;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import com.sun.org.apache.xalan.internal.xsltc.compiler.FlowList;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/util/RealType.class */
public final class RealType extends NumberType {
    protected RealType() {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public String toString() {
        return "real";
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public boolean identicalTo(Type other) {
        return this == other;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public String toSignature() {
        return PdfOps.D_TOKEN;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public com.sun.org.apache.bcel.internal.generic.Type toJCType() {
        return com.sun.org.apache.bcel.internal.generic.Type.DOUBLE;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public int distanceTo(Type type) {
        if (type == this) {
            return 0;
        }
        if (type == Type.Int) {
            return 1;
        }
        return Integer.MAX_VALUE;
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
        if (type == Type.Reference) {
            translateTo(classGen, methodGen, (ReferenceType) type);
        } else if (type == Type.Int) {
            translateTo(classGen, methodGen, (IntType) type);
        } else {
            ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), type.toString());
            classGen.getParser().reportError(2, err);
        }
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, StringType type) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        il.append(new INVOKESTATIC(cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "realToString", "(D)Ljava/lang/String;")));
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type) {
        InstructionList il = methodGen.getInstructionList();
        FlowList falsel = translateToDesynthesized(classGen, methodGen, type);
        il.append(ICONST_1);
        BranchHandle truec = il.append((BranchInstruction) new GOTO(null));
        falsel.backPatch(il.append(ICONST_0));
        truec.setTarget(il.append(NOP));
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, IntType type) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        il.append(new INVOKESTATIC(cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "realToInt", "(D)I")));
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public FlowList translateToDesynthesized(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type) {
        FlowList flowlist = new FlowList();
        classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        il.append(DUP2);
        LocalVariableGen local = methodGen.addLocalVariable("real_to_boolean_tmp", com.sun.org.apache.bcel.internal.generic.Type.DOUBLE, null, null);
        local.setStart(il.append(new DSTORE(local.getIndex())));
        il.append(DCONST_0);
        il.append(DCMPG);
        flowlist.add(il.append((BranchInstruction) new IFEQ(null)));
        il.append(new DLOAD(local.getIndex()));
        local.setEnd(il.append(new DLOAD(local.getIndex())));
        il.append(DCMPG);
        flowlist.add(il.append((BranchInstruction) new IFNE(null)));
        return flowlist;
    }

    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ReferenceType type) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        il.append(new NEW(cpg.addClass(Constants.DOUBLE_CLASS)));
        il.append(DUP_X2);
        il.append(DUP_X2);
        il.append(POP);
        il.append(new INVOKESPECIAL(cpg.addMethodref(Constants.DOUBLE_CLASS, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "(D)V")));
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Class clazz) {
        InstructionList il = methodGen.getInstructionList();
        if (clazz == Character.TYPE) {
            il.append(D2I);
            il.append(I2C);
            return;
        }
        if (clazz == Byte.TYPE) {
            il.append(D2I);
            il.append(I2B);
            return;
        }
        if (clazz == Short.TYPE) {
            il.append(D2I);
            il.append(I2S);
            return;
        }
        if (clazz == Integer.TYPE) {
            il.append(D2I);
            return;
        }
        if (clazz == Long.TYPE) {
            il.append(D2L);
            return;
        }
        if (clazz == Float.TYPE) {
            il.append(D2F);
            return;
        }
        if (clazz == Double.TYPE) {
            il.append(NOP);
        } else if (clazz.isAssignableFrom(Double.class)) {
            translateTo(classGen, methodGen, Type.Reference);
        } else {
            ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), clazz.getName());
            classGen.getParser().reportError(2, err);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public void translateFrom(ClassGenerator classGen, MethodGenerator methodGen, Class clazz) {
        InstructionList il = methodGen.getInstructionList();
        if (clazz == Character.TYPE || clazz == Byte.TYPE || clazz == Short.TYPE || clazz == Integer.TYPE) {
            il.append(I2D);
            return;
        }
        if (clazz == Long.TYPE) {
            il.append(L2D);
            return;
        }
        if (clazz == Float.TYPE) {
            il.append(F2D);
        } else if (clazz == Double.TYPE) {
            il.append(NOP);
        } else {
            ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), clazz.getName());
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
        il.append(new CHECKCAST(cpg.addClass(Constants.DOUBLE_CLASS)));
        il.append(new INVOKEVIRTUAL(cpg.addMethodref(Constants.DOUBLE_CLASS, Constants.DOUBLE_VALUE, Constants.DOUBLE_VALUE_SIG)));
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public Instruction ADD() {
        return InstructionConstants.DADD;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public Instruction SUB() {
        return InstructionConstants.DSUB;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public Instruction MUL() {
        return InstructionConstants.DMUL;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public Instruction DIV() {
        return InstructionConstants.DDIV;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public Instruction REM() {
        return InstructionConstants.DREM;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public Instruction NEG() {
        return InstructionConstants.DNEG;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public Instruction LOAD(int slot) {
        return new DLOAD(slot);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public Instruction STORE(int slot) {
        return new DSTORE(slot);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public Instruction POP() {
        return POP2;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public Instruction CMP(boolean less) {
        return less ? InstructionConstants.DCMPG : InstructionConstants.DCMPL;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public Instruction DUP() {
        return DUP2;
    }
}
