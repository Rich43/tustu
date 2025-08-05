package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/IF_ICMPNE.class */
public class IF_ICMPNE extends IfInstruction {
    IF_ICMPNE() {
    }

    public IF_ICMPNE(InstructionHandle target) {
        super((short) 160, target);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.IfInstruction
    public IfInstruction negate() {
        return new IF_ICMPEQ(this.target);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitStackConsumer(this);
        v2.visitBranchInstruction(this);
        v2.visitIfInstruction(this);
        v2.visitIF_ICMPNE(this);
    }
}
