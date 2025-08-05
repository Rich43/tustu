package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/ISTORE.class */
public class ISTORE extends StoreInstruction {
    ISTORE() {
        super((short) 54, (short) 59);
    }

    public ISTORE(int n2) {
        super((short) 54, (short) 59, n2);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.StoreInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        super.accept(v2);
        v2.visitISTORE(this);
    }
}
