package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.ExceptionConstants;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/MONITORENTER.class */
public class MONITORENTER extends Instruction implements ExceptionThrower, StackConsumer {
    public MONITORENTER() {
        super((short) 194, (short) 1);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.ExceptionThrower
    public Class[] getExceptions() {
        return new Class[]{ExceptionConstants.NULL_POINTER_EXCEPTION};
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitExceptionThrower(this);
        v2.visitStackConsumer(this);
        v2.visitMONITORENTER(this);
    }
}
