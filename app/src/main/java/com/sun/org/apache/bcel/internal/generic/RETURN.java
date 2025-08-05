package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/RETURN.class */
public class RETURN extends ReturnInstruction {
    public RETURN() {
        super((short) 177);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitExceptionThrower(this);
        v2.visitTypedInstruction(this);
        v2.visitStackConsumer(this);
        v2.visitReturnInstruction(this);
        v2.visitRETURN(this);
    }
}
