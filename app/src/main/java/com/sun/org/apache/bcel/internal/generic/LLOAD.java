package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/LLOAD.class */
public class LLOAD extends LoadInstruction {
    LLOAD() {
        super((short) 22, (short) 30);
    }

    public LLOAD(int n2) {
        super((short) 22, (short) 30, n2);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.LoadInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        super.accept(v2);
        v2.visitLLOAD(this);
    }
}
