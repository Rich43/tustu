package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/DUP_X1.class */
public class DUP_X1 extends StackInstruction {
    public DUP_X1() {
        super((short) 90);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitStackInstruction(this);
        v2.visitDUP_X1(this);
    }
}
