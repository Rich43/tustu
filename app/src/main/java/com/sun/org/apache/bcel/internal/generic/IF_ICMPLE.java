package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/IF_ICMPLE.class */
public class IF_ICMPLE extends IfInstruction {
    IF_ICMPLE() {
    }

    public IF_ICMPLE(InstructionHandle target) {
        super((short) 164, target);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.IfInstruction
    public IfInstruction negate() {
        return new IF_ICMPGT(this.target);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitStackConsumer(this);
        v2.visitBranchInstruction(this);
        v2.visitIfInstruction(this);
        v2.visitIF_ICMPLE(this);
    }
}
