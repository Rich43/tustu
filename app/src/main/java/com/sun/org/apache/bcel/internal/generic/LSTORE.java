package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/LSTORE.class */
public class LSTORE extends StoreInstruction {
    LSTORE() {
        super((short) 55, (short) 63);
    }

    public LSTORE(int n2) {
        super((short) 55, (short) 63, n2);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.StoreInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        super.accept(v2);
        v2.visitLSTORE(this);
    }
}
