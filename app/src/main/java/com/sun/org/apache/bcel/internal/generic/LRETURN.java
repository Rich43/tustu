package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/LRETURN.class */
public class LRETURN extends ReturnInstruction {
    public LRETURN() {
        super((short) 173);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitExceptionThrower(this);
        v2.visitTypedInstruction(this);
        v2.visitStackConsumer(this);
        v2.visitReturnInstruction(this);
        v2.visitLRETURN(this);
    }
}
