package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/ASTORE.class */
public class ASTORE extends StoreInstruction {
    ASTORE() {
        super((short) 58, (short) 75);
    }

    public ASTORE(int n2) {
        super((short) 58, (short) 75, n2);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.StoreInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        super.accept(v2);
        v2.visitASTORE(this);
    }
}
