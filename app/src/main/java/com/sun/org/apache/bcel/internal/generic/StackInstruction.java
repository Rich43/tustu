package com.sun.org.apache.bcel.internal.generic;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/StackInstruction.class */
public abstract class StackInstruction extends Instruction {
    StackInstruction() {
    }

    protected StackInstruction(short opcode) {
        super(opcode, (short) 1);
    }

    public Type getType(ConstantPoolGen cp) {
        return Type.UNKNOWN;
    }
}
