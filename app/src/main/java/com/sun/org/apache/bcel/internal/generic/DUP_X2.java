package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/DUP_X2.class */
public class DUP_X2 extends StackInstruction {
    public DUP_X2() {
        super((short) 91);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitStackInstruction(this);
        v2.visitDUP_X2(this);
    }
}
