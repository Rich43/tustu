package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/InstructionTargeter.class */
public interface InstructionTargeter {
    boolean containsTarget(InstructionHandle instructionHandle);

    void updateTarget(InstructionHandle instructionHandle, InstructionHandle instructionHandle2);
}
