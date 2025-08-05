package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.ExceptionConstants;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/GETFIELD.class */
public class GETFIELD extends FieldInstruction implements ExceptionThrower, StackConsumer, StackProducer {
    GETFIELD() {
    }

    public GETFIELD(int index) {
        super((short) 180, index);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction, com.sun.org.apache.bcel.internal.generic.StackProducer
    public int produceStack(ConstantPoolGen cpg) {
        return getFieldSize(cpg);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.ExceptionThrower
    public Class[] getExceptions() {
        Class[] cs = new Class[2 + ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length];
        System.arraycopy(ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION, 0, cs, 0, ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length);
        cs[ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length + 1] = ExceptionConstants.INCOMPATIBLE_CLASS_CHANGE_ERROR;
        cs[ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length] = ExceptionConstants.NULL_POINTER_EXCEPTION;
        return cs;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitExceptionThrower(this);
        v2.visitStackConsumer(this);
        v2.visitStackProducer(this);
        v2.visitTypedInstruction(this);
        v2.visitLoadClass(this);
        v2.visitCPInstruction(this);
        v2.visitFieldOrMethod(this);
        v2.visitFieldInstruction(this);
        v2.visitGETFIELD(this);
    }
}
