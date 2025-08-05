package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/DUP2_X1.class */
public class DUP2_X1 extends StackInstruction {
    public DUP2_X1() {
        super((short) 93);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitStackInstruction(this);
        v2.visitDUP2_X1(this);
    }
}
