package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/LXOR.class */
public class LXOR extends ArithmeticInstruction {
    public LXOR() {
        super((short) 131);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitTypedInstruction(this);
        v2.visitStackProducer(this);
        v2.visitStackConsumer(this);
        v2.visitArithmeticInstruction(this);
        v2.visitLXOR(this);
    }
}
