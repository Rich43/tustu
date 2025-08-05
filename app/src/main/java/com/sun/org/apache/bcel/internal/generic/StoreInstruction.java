package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/StoreInstruction.class */
public abstract class StoreInstruction extends LocalVariableInstruction implements PopInstruction {
    StoreInstruction(short canon_tag, short c_tag) {
        super(canon_tag, c_tag);
    }

    protected StoreInstruction(short opcode, short c_tag, int n2) {
        super(opcode, c_tag, n2);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitStackConsumer(this);
        v2.visitPopInstruction(this);
        v2.visitTypedInstruction(this);
        v2.visitLocalVariableInstruction(this);
        v2.visitStoreInstruction(this);
    }
}
