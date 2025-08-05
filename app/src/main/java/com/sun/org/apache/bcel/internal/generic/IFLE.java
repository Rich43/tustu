package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/IFLE.class */
public class IFLE extends IfInstruction {
    IFLE() {
    }

    public IFLE(InstructionHandle target) {
        super((short) 158, target);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.IfInstruction
    public IfInstruction negate() {
        return new IFGT(this.target);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitStackConsumer(this);
        v2.visitBranchInstruction(this);
        v2.visitIfInstruction(this);
        v2.visitIFLE(this);
    }
}
