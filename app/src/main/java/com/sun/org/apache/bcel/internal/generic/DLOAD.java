package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/DLOAD.class */
public class DLOAD extends LoadInstruction {
    DLOAD() {
        super((short) 24, (short) 38);
    }

    public DLOAD(int n2) {
        super((short) 24, (short) 38, n2);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.LoadInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        super.accept(v2);
        v2.visitDLOAD(this);
    }
}
