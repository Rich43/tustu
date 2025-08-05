package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/POP.class */
public class POP extends StackInstruction implements PopInstruction {
    public POP() {
        super((short) 87);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitStackConsumer(this);
        v2.visitPopInstruction(this);
        v2.visitStackInstruction(this);
        v2.visitPOP(this);
    }
}
