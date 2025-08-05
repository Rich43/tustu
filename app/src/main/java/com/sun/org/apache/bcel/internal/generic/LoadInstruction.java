package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/LoadInstruction.class */
public abstract class LoadInstruction extends LocalVariableInstruction implements PushInstruction {
    LoadInstruction(short canon_tag, short c_tag) {
        super(canon_tag, c_tag);
    }

    protected LoadInstruction(short opcode, short c_tag, int n2) {
        super(opcode, c_tag, n2);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitStackProducer(this);
        v2.visitPushInstruction(this);
        v2.visitTypedInstruction(this);
        v2.visitLocalVariableInstruction(this);
        v2.visitLoadInstruction(this);
    }
}
