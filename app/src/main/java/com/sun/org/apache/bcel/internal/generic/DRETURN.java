package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/DRETURN.class */
public class DRETURN extends ReturnInstruction {
    public DRETURN() {
        super((short) 175);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitExceptionThrower(this);
        v2.visitTypedInstruction(this);
        v2.visitStackConsumer(this);
        v2.visitReturnInstruction(this);
        v2.visitDRETURN(this);
    }
}
