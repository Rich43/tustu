package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/ALOAD.class */
public class ALOAD extends LoadInstruction {
    ALOAD() {
        super((short) 25, (short) 42);
    }

    public ALOAD(int n2) {
        super((short) 25, (short) 42, n2);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.LoadInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        super.accept(v2);
        v2.visitALOAD(this);
    }
}
