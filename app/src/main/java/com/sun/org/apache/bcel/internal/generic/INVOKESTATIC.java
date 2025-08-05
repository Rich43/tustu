package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.ExceptionConstants;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/INVOKESTATIC.class */
public class INVOKESTATIC extends InvokeInstruction {
    INVOKESTATIC() {
    }

    public INVOKESTATIC(int index) {
        super((short) 184, index);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.ExceptionThrower
    public Class[] getExceptions() {
        Class[] cs = new Class[2 + ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length];
        System.arraycopy(ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION, 0, cs, 0, ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length);
        cs[ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length] = ExceptionConstants.UNSATISFIED_LINK_ERROR;
        cs[ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length + 1] = ExceptionConstants.INCOMPATIBLE_CLASS_CHANGE_ERROR;
        return cs;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitExceptionThrower(this);
        v2.visitTypedInstruction(this);
        v2.visitStackConsumer(this);
        v2.visitStackProducer(this);
        v2.visitLoadClass(this);
        v2.visitCPInstruction(this);
        v2.visitFieldOrMethod(this);
        v2.visitInvokeInstruction(this);
        v2.visitINVOKESTATIC(this);
    }
}
