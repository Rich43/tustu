package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.ExceptionConstants;
import com.sun.org.apache.bcel.internal.classfile.Constant;
import com.sun.org.apache.bcel.internal.classfile.ConstantFloat;
import com.sun.org.apache.bcel.internal.classfile.ConstantInteger;
import com.sun.org.apache.bcel.internal.classfile.ConstantString;
import com.sun.org.apache.bcel.internal.classfile.ConstantUtf8;
import com.sun.org.apache.bcel.internal.util.ByteSequence;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/LDC.class */
public class LDC extends CPInstruction implements PushInstruction, ExceptionThrower, TypedInstruction {
    LDC() {
    }

    public LDC(int index) {
        super((short) 19, index);
        setSize();
    }

    protected final void setSize() {
        if (this.index <= 255) {
            this.opcode = (short) 18;
            this.length = (short) 2;
        } else {
            this.opcode = (short) 19;
            this.length = (short) 3;
        }
    }

    @Override // com.sun.org.apache.bcel.internal.generic.CPInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    public void dump(DataOutputStream out) throws IOException {
        out.writeByte(this.opcode);
        if (this.length == 2) {
            out.writeByte(this.index);
        } else {
            out.writeShort(this.index);
        }
    }

    @Override // com.sun.org.apache.bcel.internal.generic.CPInstruction, com.sun.org.apache.bcel.internal.generic.IndexedInstruction
    public final void setIndex(int index) {
        super.setIndex(index);
        setSize();
    }

    @Override // com.sun.org.apache.bcel.internal.generic.CPInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException {
        this.length = (short) 2;
        this.index = bytes.readUnsignedByte();
    }

    public Object getValue(ConstantPoolGen cpg) {
        Constant c2 = cpg.getConstantPool().getConstant(this.index);
        switch (c2.getTag()) {
            case 3:
                return new Integer(((ConstantInteger) c2).getBytes());
            case 4:
                return new Float(((ConstantFloat) c2).getBytes());
            case 8:
                int i2 = ((ConstantString) c2).getStringIndex();
                return ((ConstantUtf8) cpg.getConstantPool().getConstant(i2)).getBytes();
            default:
                throw new RuntimeException("Unknown or invalid constant type at " + this.index);
        }
    }

    @Override // com.sun.org.apache.bcel.internal.generic.CPInstruction, com.sun.org.apache.bcel.internal.generic.TypedInstruction
    public Type getType(ConstantPoolGen cpg) {
        switch (cpg.getConstantPool().getConstant(this.index).getTag()) {
            case 3:
                return Type.INT;
            case 4:
                return Type.FLOAT;
            case 8:
                return Type.STRING;
            default:
                throw new RuntimeException("Unknown or invalid constant type at " + this.index);
        }
    }

    @Override // com.sun.org.apache.bcel.internal.generic.ExceptionThrower
    public Class[] getExceptions() {
        return ExceptionConstants.EXCS_STRING_RESOLUTION;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitStackProducer(this);
        v2.visitPushInstruction(this);
        v2.visitExceptionThrower(this);
        v2.visitTypedInstruction(this);
        v2.visitCPInstruction(this);
        v2.visitLDC(this);
    }
}
