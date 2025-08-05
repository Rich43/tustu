package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/LCONST.class */
public class LCONST extends Instruction implements ConstantPushInstruction, TypedInstruction {
    private long value;

    LCONST() {
    }

    public LCONST(long l2) {
        super((short) 9, (short) 1);
        if (l2 == 0) {
            this.opcode = (short) 9;
        } else if (l2 == 1) {
            this.opcode = (short) 10;
        } else {
            throw new ClassGenException("LCONST can be used only for 0 and 1: " + l2);
        }
        this.value = l2;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.ConstantPushInstruction
    public Number getValue() {
        return new Long(this.value);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.TypedInstruction
    public Type getType(ConstantPoolGen cp) {
        return Type.LONG;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitPushInstruction(this);
        v2.visitStackProducer(this);
        v2.visitTypedInstruction(this);
        v2.visitConstantPushInstruction(this);
        v2.visitLCONST(this);
    }
}
