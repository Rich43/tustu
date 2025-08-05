package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.util.ByteSequence;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/RET.class */
public class RET extends Instruction implements IndexedInstruction, TypedInstruction {
    private boolean wide;
    private int index;

    RET() {
    }

    public RET(int index) {
        super((short) 169, (short) 2);
        setIndex(index);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void dump(DataOutputStream out) throws IOException {
        if (this.wide) {
            out.writeByte(196);
        }
        out.writeByte(this.opcode);
        if (this.wide) {
            out.writeShort(this.index);
        } else {
            out.writeByte(this.index);
        }
    }

    private final void setWide() {
        boolean z2 = this.index > 255;
        this.wide = z2;
        if (z2) {
            this.length = (short) 4;
        } else {
            this.length = (short) 2;
        }
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException {
        this.wide = wide;
        if (wide) {
            this.index = bytes.readUnsignedShort();
            this.length = (short) 4;
        } else {
            this.index = bytes.readUnsignedByte();
            this.length = (short) 2;
        }
    }

    @Override // com.sun.org.apache.bcel.internal.generic.IndexedInstruction
    public final int getIndex() {
        return this.index;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.IndexedInstruction
    public final void setIndex(int n2) {
        if (n2 < 0) {
            throw new ClassGenException("Negative index value: " + n2);
        }
        this.index = n2;
        setWide();
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public String toString(boolean verbose) {
        return super.toString(verbose) + " " + this.index;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.TypedInstruction
    public Type getType(ConstantPoolGen cp) {
        return ReturnaddressType.NO_TARGET;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitRET(this);
    }
}
