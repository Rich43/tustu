package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.Constants;
import com.sun.org.apache.bcel.internal.ExceptionConstants;
import com.sun.org.apache.bcel.internal.util.ByteSequence;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/NEWARRAY.class */
public class NEWARRAY extends Instruction implements AllocationInstruction, ExceptionThrower, StackProducer {
    private byte type;

    NEWARRAY() {
    }

    public NEWARRAY(byte type) {
        super((short) 188, (short) 2);
        this.type = type;
    }

    public NEWARRAY(BasicType type) {
        this(type.getType());
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void dump(DataOutputStream out) throws IOException {
        out.writeByte(this.opcode);
        out.writeByte(this.type);
    }

    public final byte getTypecode() {
        return this.type;
    }

    public final Type getType() {
        return new ArrayType(BasicType.getType(this.type), 1);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public String toString(boolean verbose) {
        return super.toString(verbose) + " " + Constants.TYPE_NAMES[this.type];
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException {
        this.type = bytes.readByte();
        this.length = (short) 2;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.ExceptionThrower
    public Class[] getExceptions() {
        return new Class[]{ExceptionConstants.NEGATIVE_ARRAY_SIZE_EXCEPTION};
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitAllocationInstruction(this);
        v2.visitExceptionThrower(this);
        v2.visitStackProducer(this);
        v2.visitNEWARRAY(this);
    }
}
