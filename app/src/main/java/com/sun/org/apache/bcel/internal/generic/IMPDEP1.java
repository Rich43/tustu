package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/IMPDEP1.class */
public class IMPDEP1 extends Instruction {
    public IMPDEP1() {
        super((short) 254, (short) 1);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitIMPDEP1(this);
    }
}
