package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.ExceptionConstants;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/LDIV.class */
public class LDIV extends ArithmeticInstruction implements ExceptionThrower {
    public LDIV() {
        super((short) 109);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.ExceptionThrower
    public Class[] getExceptions() {
        return new Class[]{ExceptionConstants.ARITHMETIC_EXCEPTION};
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitExceptionThrower(this);
        v2.visitTypedInstruction(this);
        v2.visitStackProducer(this);
        v2.visitStackConsumer(this);
        v2.visitArithmeticInstruction(this);
        v2.visitLDIV(this);
    }
}
