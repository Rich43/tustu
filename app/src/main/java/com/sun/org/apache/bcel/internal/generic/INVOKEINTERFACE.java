package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.ExceptionConstants;
import com.sun.org.apache.bcel.internal.classfile.ConstantPool;
import com.sun.org.apache.bcel.internal.util.ByteSequence;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/INVOKEINTERFACE.class */
public final class INVOKEINTERFACE extends InvokeInstruction {
    private int nargs;

    INVOKEINTERFACE() {
    }

    public INVOKEINTERFACE(int index, int nargs) {
        super((short) 185, index);
        this.length = (short) 5;
        if (nargs < 1) {
            throw new ClassGenException("Number of arguments must be > 0 " + nargs);
        }
        this.nargs = nargs;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.CPInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    public void dump(DataOutputStream out) throws IOException {
        out.writeByte(this.opcode);
        out.writeShort(this.index);
        out.writeByte(this.nargs);
        out.writeByte(0);
    }

    public int getCount() {
        return this.nargs;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.CPInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException {
        super.initFromFile(bytes, wide);
        this.length = (short) 5;
        this.nargs = bytes.readUnsignedByte();
        bytes.readByte();
    }

    @Override // com.sun.org.apache.bcel.internal.generic.InvokeInstruction, com.sun.org.apache.bcel.internal.generic.CPInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    public String toString(ConstantPool cp) {
        return super.toString(cp) + " " + this.nargs;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.InvokeInstruction, com.sun.org.apache.bcel.internal.generic.Instruction, com.sun.org.apache.bcel.internal.generic.StackConsumer
    public int consumeStack(ConstantPoolGen cpg) {
        return this.nargs;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.ExceptionThrower
    public Class[] getExceptions() {
        Class[] cs = new Class[4 + ExceptionConstants.EXCS_INTERFACE_METHOD_RESOLUTION.length];
        System.arraycopy(ExceptionConstants.EXCS_INTERFACE_METHOD_RESOLUTION, 0, cs, 0, ExceptionConstants.EXCS_INTERFACE_METHOD_RESOLUTION.length);
        cs[ExceptionConstants.EXCS_INTERFACE_METHOD_RESOLUTION.length + 3] = ExceptionConstants.INCOMPATIBLE_CLASS_CHANGE_ERROR;
        cs[ExceptionConstants.EXCS_INTERFACE_METHOD_RESOLUTION.length + 2] = ExceptionConstants.ILLEGAL_ACCESS_ERROR;
        cs[ExceptionConstants.EXCS_INTERFACE_METHOD_RESOLUTION.length + 1] = ExceptionConstants.ABSTRACT_METHOD_ERROR;
        cs[ExceptionConstants.EXCS_INTERFACE_METHOD_RESOLUTION.length] = ExceptionConstants.UNSATISFIED_LINK_ERROR;
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
        v2.visitINVOKEINTERFACE(this);
    }
}
