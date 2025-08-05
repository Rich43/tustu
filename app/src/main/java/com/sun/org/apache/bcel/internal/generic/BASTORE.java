package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/BASTORE.class */
public class BASTORE extends ArrayInstruction implements StackConsumer {
    public BASTORE() {
        super((short) 84);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitStackConsumer(this);
        v2.visitExceptionThrower(this);
        v2.visitTypedInstruction(this);
        v2.visitArrayInstruction(this);
        v2.visitBASTORE(this);
    }
}
