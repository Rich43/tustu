package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/FASTORE.class */
public class FASTORE extends ArrayInstruction implements StackConsumer {
    public FASTORE() {
        super((short) 81);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitStackConsumer(this);
        v2.visitExceptionThrower(this);
        v2.visitTypedInstruction(this);
        v2.visitArrayInstruction(this);
        v2.visitFASTORE(this);
    }
}
