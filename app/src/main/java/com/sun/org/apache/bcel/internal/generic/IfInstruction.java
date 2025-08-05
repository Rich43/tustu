package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/IfInstruction.class */
public abstract class IfInstruction extends BranchInstruction implements StackConsumer {
    public abstract IfInstruction negate();

    IfInstruction() {
    }

    protected IfInstruction(short opcode, InstructionHandle target) {
        super(opcode, target);
    }
}
