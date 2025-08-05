package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/DUP2_X2.class */
public class DUP2_X2 extends StackInstruction {
    public DUP2_X2() {
        super((short) 94);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitStackInstruction(this);
        v2.visitDUP2_X2(this);
    }
}
