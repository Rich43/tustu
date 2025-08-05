package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/ARETURN.class */
public class ARETURN extends ReturnInstruction {
    public ARETURN() {
        super((short) 176);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitExceptionThrower(this);
        v2.visitTypedInstruction(this);
        v2.visitStackConsumer(this);
        v2.visitReturnInstruction(this);
        v2.visitARETURN(this);
    }
}
