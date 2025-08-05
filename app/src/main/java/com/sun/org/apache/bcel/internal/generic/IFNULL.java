package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/IFNULL.class */
public class IFNULL extends IfInstruction {
    IFNULL() {
    }

    public IFNULL(InstructionHandle target) {
        super((short) 198, target);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.IfInstruction
    public IfInstruction negate() {
        return new IFNONNULL(this.target);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitStackConsumer(this);
        v2.visitBranchInstruction(this);
        v2.visitIfInstruction(this);
        v2.visitIFNULL(this);
    }
}
