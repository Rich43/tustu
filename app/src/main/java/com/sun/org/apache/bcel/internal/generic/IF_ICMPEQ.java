package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/IF_ICMPEQ.class */
public class IF_ICMPEQ extends IfInstruction {
    IF_ICMPEQ() {
    }

    public IF_ICMPEQ(InstructionHandle target) {
        super((short) 159, target);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.IfInstruction
    public IfInstruction negate() {
        return new IF_ICMPNE(this.target);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitStackConsumer(this);
        v2.visitBranchInstruction(this);
        v2.visitIfInstruction(this);
        v2.visitIF_ICMPEQ(this);
    }
}
