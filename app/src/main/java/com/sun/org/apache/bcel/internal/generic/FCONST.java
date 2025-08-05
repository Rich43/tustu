package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/FCONST.class */
public class FCONST extends Instruction implements ConstantPushInstruction, TypedInstruction {
    private float value;

    FCONST() {
    }

    public FCONST(float f2) {
        super((short) 11, (short) 1);
        if (f2 == 0.0d) {
            this.opcode = (short) 11;
        } else if (f2 == 1.0d) {
            this.opcode = (short) 12;
        } else if (f2 == 2.0d) {
            this.opcode = (short) 13;
        } else {
            throw new ClassGenException("FCONST can be used only for 0.0, 1.0 and 2.0: " + f2);
        }
        this.value = f2;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.ConstantPushInstruction
    public Number getValue() {
        return new Float(this.value);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.TypedInstruction
    public Type getType(ConstantPoolGen cp) {
        return Type.FLOAT;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitPushInstruction(this);
        v2.visitStackProducer(this);
        v2.visitTypedInstruction(this);
        v2.visitConstantPushInstruction(this);
        v2.visitFCONST(this);
    }
}
