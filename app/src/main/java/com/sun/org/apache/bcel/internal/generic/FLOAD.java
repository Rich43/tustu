package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/FLOAD.class */
public class FLOAD extends LoadInstruction {
    FLOAD() {
        super((short) 23, (short) 34);
    }

    public FLOAD(int n2) {
        super((short) 23, (short) 34, n2);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.LoadInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        super.accept(v2);
        v2.visitFLOAD(this);
    }
}
