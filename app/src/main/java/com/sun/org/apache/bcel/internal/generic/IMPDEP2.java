package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/IMPDEP2.class */
public class IMPDEP2 extends Instruction {
    public IMPDEP2() {
        super((short) 255, (short) 1);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitIMPDEP2(this);
    }
}
