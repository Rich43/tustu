package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/GotoInstruction.class */
public abstract class GotoInstruction extends BranchInstruction implements UnconditionalBranch {
    GotoInstruction(short opcode, InstructionHandle target) {
        super(opcode, target);
    }

    GotoInstruction() {
    }
}
