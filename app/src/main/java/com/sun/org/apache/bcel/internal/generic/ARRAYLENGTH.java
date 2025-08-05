package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.ExceptionConstants;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/ARRAYLENGTH.class */
public class ARRAYLENGTH extends Instruction implements ExceptionThrower, StackProducer {
    public ARRAYLENGTH() {
        super((short) 190, (short) 1);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.ExceptionThrower
    public Class[] getExceptions() {
        return new Class[]{ExceptionConstants.NULL_POINTER_EXCEPTION};
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitExceptionThrower(this);
        v2.visitStackProducer(this);
        v2.visitARRAYLENGTH(this);
    }
}
