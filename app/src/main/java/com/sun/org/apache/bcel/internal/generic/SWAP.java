package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/SWAP.class */
public class SWAP extends StackInstruction implements StackConsumer, StackProducer {
    public SWAP() {
        super((short) 95);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitStackConsumer(this);
        v2.visitStackProducer(this);
        v2.visitStackInstruction(this);
        v2.visitSWAP(this);
    }
}
