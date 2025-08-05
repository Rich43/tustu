package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.classfile.Constant;
import com.sun.org.apache.bcel.internal.classfile.ConstantDouble;
import com.sun.org.apache.bcel.internal.classfile.ConstantLong;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/LDC2_W.class */
public class LDC2_W extends CPInstruction implements PushInstruction, TypedInstruction {
    LDC2_W() {
    }

    public LDC2_W(int index) {
        super((short) 20, index);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.CPInstruction, com.sun.org.apache.bcel.internal.generic.TypedInstruction
    public Type getType(ConstantPoolGen cpg) {
        switch (cpg.getConstantPool().getConstant(this.index).getTag()) {
            case 5:
                return Type.LONG;
            case 6:
                return Type.DOUBLE;
            default:
                throw new RuntimeException("Unknown constant type " + ((int) this.opcode));
        }
    }

    public Number getValue(ConstantPoolGen cpg) {
        Constant c2 = cpg.getConstantPool().getConstant(this.index);
        switch (c2.getTag()) {
            case 5:
                return new Long(((ConstantLong) c2).getBytes());
            case 6:
                return new Double(((ConstantDouble) c2).getBytes());
            default:
                throw new RuntimeException("Unknown or invalid constant type at " + this.index);
        }
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitStackProducer(this);
        v2.visitPushInstruction(this);
        v2.visitTypedInstruction(this);
        v2.visitCPInstruction(this);
        v2.visitLDC2_W(this);
    }
}
