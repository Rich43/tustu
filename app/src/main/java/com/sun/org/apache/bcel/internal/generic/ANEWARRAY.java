package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.ExceptionConstants;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/ANEWARRAY.class */
public class ANEWARRAY extends CPInstruction implements LoadClass, AllocationInstruction, ExceptionThrower, StackProducer {
    ANEWARRAY() {
    }

    public ANEWARRAY(int index) {
        super((short) 189, index);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.ExceptionThrower
    public Class[] getExceptions() {
        Class[] cs = new Class[1 + ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length];
        System.arraycopy(ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION, 0, cs, 0, ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length);
        cs[ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length] = ExceptionConstants.NEGATIVE_ARRAY_SIZE_EXCEPTION;
        return cs;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitLoadClass(this);
        v2.visitAllocationInstruction(this);
        v2.visitExceptionThrower(this);
        v2.visitStackProducer(this);
        v2.visitTypedInstruction(this);
        v2.visitCPInstruction(this);
        v2.visitANEWARRAY(this);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.LoadClass
    public ObjectType getLoadClassType(ConstantPoolGen cpg) {
        Type t2 = getType(cpg);
        if (t2 instanceof ArrayType) {
            t2 = ((ArrayType) t2).getBasicType();
        }
        if (t2 instanceof ObjectType) {
            return (ObjectType) t2;
        }
        return null;
    }
}
