package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/L2D.class */
public class L2D extends ConversionInstruction {
    public L2D() {
        super((short) 138);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitTypedInstruction(this);
        v2.visitStackProducer(this);
        v2.visitStackConsumer(this);
        v2.visitConversionInstruction(this);
        v2.visitL2D(this);
    }
}
