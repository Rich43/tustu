package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/FALOAD.class */
public class FALOAD extends ArrayInstruction implements StackProducer {
    public FALOAD() {
        super((short) 48);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitStackProducer(this);
        v2.visitExceptionThrower(this);
        v2.visitTypedInstruction(this);
        v2.visitArrayInstruction(this);
        v2.visitFALOAD(this);
    }
}
