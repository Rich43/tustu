package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/FSTORE.class */
public class FSTORE extends StoreInstruction {
    FSTORE() {
        super((short) 56, (short) 67);
    }

    public FSTORE(int n2) {
        super((short) 56, (short) 67, n2);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.StoreInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        super.accept(v2);
        v2.visitFSTORE(this);
    }
}
