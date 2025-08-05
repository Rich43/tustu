package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/TargetLostException.class */
public final class TargetLostException extends Exception {
    private InstructionHandle[] targets;

    TargetLostException(InstructionHandle[] t2, String mesg) {
        super(mesg);
        this.targets = t2;
    }

    public InstructionHandle[] getTargets() {
        return this.targets;
    }
}
