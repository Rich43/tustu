package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.ExceptionConstants;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/ReturnInstruction.class */
public abstract class ReturnInstruction extends Instruction implements ExceptionThrower, TypedInstruction, StackConsumer {
    ReturnInstruction() {
    }

    protected ReturnInstruction(short opcode) {
        super(opcode, (short) 1);
    }

    public Type getType() {
        switch (this.opcode) {
            case 172:
                return Type.INT;
            case 173:
                return Type.LONG;
            case 174:
                return Type.FLOAT;
            case 175:
                return Type.DOUBLE;
            case 176:
                return Type.OBJECT;
            case 177:
                return Type.VOID;
            default:
                throw new ClassGenException("Unknown type " + ((int) this.opcode));
        }
    }

    @Override // com.sun.org.apache.bcel.internal.generic.ExceptionThrower
    public Class[] getExceptions() {
        return new Class[]{ExceptionConstants.ILLEGAL_MONITOR_STATE};
    }

    @Override // com.sun.org.apache.bcel.internal.generic.TypedInstruction
    public Type getType(ConstantPoolGen cp) {
        return getType();
    }
}
