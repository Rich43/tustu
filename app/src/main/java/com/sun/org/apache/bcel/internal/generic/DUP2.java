package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/DUP2.class */
public class DUP2 extends StackInstruction implements PushInstruction {
    public DUP2() {
        super((short) 92);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitStackProducer(this);
        v2.visitPushInstruction(this);
        v2.visitStackInstruction(this);
        v2.visitDUP2(this);
    }
}
