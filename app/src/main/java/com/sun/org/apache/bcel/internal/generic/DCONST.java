package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/DCONST.class */
public class DCONST extends Instruction implements ConstantPushInstruction, TypedInstruction {
    private double value;

    DCONST() {
    }

    public DCONST(double f2) {
        super((short) 14, (short) 1);
        if (f2 == 0.0d) {
            this.opcode = (short) 14;
        } else if (f2 == 1.0d) {
            this.opcode = (short) 15;
        } else {
            throw new ClassGenException("DCONST can be used only for 0.0 and 1.0: " + f2);
        }
        this.value = f2;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.ConstantPushInstruction
    public Number getValue() {
        return new Double(this.value);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.TypedInstruction
    public Type getType(ConstantPoolGen cp) {
        return Type.DOUBLE;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitPushInstruction(this);
        v2.visitStackProducer(this);
        v2.visitTypedInstruction(this);
        v2.visitConstantPushInstruction(this);
        v2.visitDCONST(this);
    }
}
