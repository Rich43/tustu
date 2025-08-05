package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/BREAKPOINT.class */
public class BREAKPOINT extends Instruction {
    public BREAKPOINT() {
        super((short) 202, (short) 1);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitBREAKPOINT(this);
    }
}
