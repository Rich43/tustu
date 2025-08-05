package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/IF_ICMPGT.class */
public class IF_ICMPGT extends IfInstruction {
    IF_ICMPGT() {
    }

    public IF_ICMPGT(InstructionHandle target) {
        super((short) 163, target);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.IfInstruction
    public IfInstruction negate() {
        return new IF_ICMPLE(this.target);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitStackConsumer(this);
        v2.visitBranchInstruction(this);
        v2.visitIfInstruction(this);
        v2.visitIF_ICMPGT(this);
    }
}
