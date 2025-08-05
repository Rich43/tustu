package com.sun.org.apache.bcel.internal.generic;

import java.util.Objects;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/ReturnaddressType.class */
public class ReturnaddressType extends Type {
    public static final ReturnaddressType NO_TARGET = new ReturnaddressType();
    private InstructionHandle returnTarget;

    private ReturnaddressType() {
        super((byte) 16, "<return address>");
    }

    public ReturnaddressType(InstructionHandle returnTarget) {
        super((byte) 16, "<return address targeting " + ((Object) returnTarget) + ">");
        this.returnTarget = returnTarget;
    }

    public int hashCode() {
        return Objects.hashCode(this.returnTarget);
    }

    public boolean equals(Object rat) {
        if (!(rat instanceof ReturnaddressType)) {
            return false;
        }
        return ((ReturnaddressType) rat).returnTarget.equals(this.returnTarget);
    }

    public InstructionHandle getTarget() {
        return this.returnTarget;
    }
}
