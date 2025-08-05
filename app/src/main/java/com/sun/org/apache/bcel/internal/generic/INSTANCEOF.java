package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.ExceptionConstants;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/INSTANCEOF.class */
public class INSTANCEOF extends CPInstruction implements LoadClass, ExceptionThrower, StackProducer, StackConsumer {
    INSTANCEOF() {
    }

    public INSTANCEOF(int index) {
        super((short) 193, index);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.ExceptionThrower
    public Class[] getExceptions() {
        return ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION;
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

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitLoadClass(this);
        v2.visitExceptionThrower(this);
        v2.visitStackProducer(this);
        v2.visitStackConsumer(this);
        v2.visitTypedInstruction(this);
        v2.visitCPInstruction(this);
        v2.visitINSTANCEOF(this);
    }
}
