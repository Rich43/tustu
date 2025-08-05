package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/ILOAD.class */
public class ILOAD extends LoadInstruction {
    ILOAD() {
        super((short) 21, (short) 26);
    }

    public ILOAD(int n2) {
        super((short) 21, (short) 26, n2);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.LoadInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        super.accept(v2);
        v2.visitILOAD(this);
    }
}
