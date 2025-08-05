package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.util.ByteSequence;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/IINC.class */
public class IINC extends LocalVariableInstruction {
    private boolean wide;

    /* renamed from: c, reason: collision with root package name */
    private int f11997c;

    IINC() {
    }

    public IINC(int n2, int c2) {
        this.opcode = (short) 132;
        this.length = (short) 3;
        setIndex(n2);
        setIncrement(c2);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.LocalVariableInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    public void dump(DataOutputStream out) throws IOException {
        if (this.wide) {
            out.writeByte(196);
        }
        out.writeByte(this.opcode);
        if (this.wide) {
            out.writeShort(this.f11998n);
            out.writeShort(this.f11997c);
        } else {
            out.writeByte(this.f11998n);
            out.writeByte(this.f11997c);
        }
    }

    private final void setWide() {
        boolean z2 = this.f11998n > 65535 || Math.abs(this.f11997c) > 127;
        this.wide = z2;
        if (z2) {
            this.length = (short) 6;
        } else {
            this.length = (short) 3;
        }
    }

    @Override // com.sun.org.apache.bcel.internal.generic.LocalVariableInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException {
        this.wide = wide;
        if (wide) {
            this.length = (short) 6;
            this.f11998n = bytes.readUnsignedShort();
            this.f11997c = bytes.readShort();
        } else {
            this.length = (short) 3;
            this.f11998n = bytes.readUnsignedByte();
            this.f11997c = bytes.readByte();
        }
    }

    @Override // com.sun.org.apache.bcel.internal.generic.LocalVariableInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    public String toString(boolean verbose) {
        return super.toString(verbose) + " " + this.f11997c;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.LocalVariableInstruction, com.sun.org.apache.bcel.internal.generic.IndexedInstruction
    public final void setIndex(int n2) {
        if (n2 < 0) {
            throw new ClassGenException("Negative index value: " + n2);
        }
        this.f11998n = n2;
        setWide();
    }

    public final int getIncrement() {
        return this.f11997c;
    }

    public final void setIncrement(int c2) {
        this.f11997c = c2;
        setWide();
    }

    @Override // com.sun.org.apache.bcel.internal.generic.LocalVariableInstruction, com.sun.org.apache.bcel.internal.generic.TypedInstruction
    public Type getType(ConstantPoolGen cp) {
        return Type.INT;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitLocalVariableInstruction(this);
        v2.visitIINC(this);
    }
}
