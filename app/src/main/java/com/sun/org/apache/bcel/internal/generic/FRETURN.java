package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/FRETURN.class */
public class FRETURN extends ReturnInstruction {
    public FRETURN() {
        super((short) 174);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitExceptionThrower(this);
        v2.visitTypedInstruction(this);
        v2.visitStackConsumer(this);
        v2.visitReturnInstruction(this);
        v2.visitFRETURN(this);
    }
}
