package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/DNEG.class */
public class DNEG extends ArithmeticInstruction {
    public DNEG() {
        super((short) 119);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitTypedInstruction(this);
        v2.visitStackProducer(this);
        v2.visitStackConsumer(this);
        v2.visitArithmeticInstruction(this);
        v2.visitDNEG(this);
    }
}
