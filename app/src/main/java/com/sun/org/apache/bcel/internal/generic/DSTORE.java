package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/DSTORE.class */
public class DSTORE extends StoreInstruction {
    DSTORE() {
        super((short) 57, (short) 71);
    }

    public DSTORE(int n2) {
        super((short) 57, (short) 71, n2);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.StoreInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        super.accept(v2);
        v2.visitDSTORE(this);
    }
}
