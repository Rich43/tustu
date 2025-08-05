package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/IAND.class */
public class IAND extends ArithmeticInstruction {
    public IAND() {
        super((short) 126);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitTypedInstruction(this);
        v2.visitStackProducer(this);
        v2.visitStackConsumer(this);
        v2.visitArithmeticInstruction(this);
        v2.visitIAND(this);
    }
}
