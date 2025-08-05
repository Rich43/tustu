package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/IF_ACMPNE.class */
public class IF_ACMPNE extends IfInstruction {
    IF_ACMPNE() {
    }

    public IF_ACMPNE(InstructionHandle target) {
        super((short) 166, target);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.IfInstruction
    public IfInstruction negate() {
        return new IF_ACMPEQ(this.target);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitStackConsumer(this);
        v2.visitBranchInstruction(this);
        v2.visitIfInstruction(this);
        v2.visitIF_ACMPNE(this);
    }
}
