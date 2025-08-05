package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/ICONST.class */
public class ICONST extends Instruction implements ConstantPushInstruction, TypedInstruction {
    private int value;

    ICONST() {
    }

    public ICONST(int i2) {
        super((short) 3, (short) 1);
        if (i2 >= -1 && i2 <= 5) {
            this.opcode = (short) (3 + i2);
            this.value = i2;
            return;
        }
        throw new ClassGenException("ICONST can be used only for value between -1 and 5: " + i2);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.ConstantPushInstruction
    public Number getValue() {
        return new Integer(this.value);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.TypedInstruction
    public Type getType(ConstantPoolGen cp) {
        return Type.INT;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitPushInstruction(this);
        v2.visitStackProducer(this);
        v2.visitTypedInstruction(this);
        v2.visitConstantPushInstruction(this);
        v2.visitICONST(this);
    }
}
