package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.util.ByteSequence;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/LocalVariableInstruction.class */
public abstract class LocalVariableInstruction extends Instruction implements TypedInstruction, IndexedInstruction {

    /* renamed from: n, reason: collision with root package name */
    protected int f11998n;
    private short c_tag;
    private short canon_tag;

    private final boolean wide() {
        return this.f11998n > 255;
    }

    LocalVariableInstruction(short canon_tag, short c_tag) {
        this.f11998n = -1;
        this.c_tag = (short) -1;
        this.canon_tag = (short) -1;
        this.canon_tag = canon_tag;
        this.c_tag = c_tag;
    }

    LocalVariableInstruction() {
        this.f11998n = -1;
        this.c_tag = (short) -1;
        this.canon_tag = (short) -1;
    }

    protected LocalVariableInstruction(short opcode, short c_tag, int n2) {
        super(opcode, (short) 2);
        this.f11998n = -1;
        this.c_tag = (short) -1;
        this.canon_tag = (short) -1;
        this.c_tag = c_tag;
        this.canon_tag = opcode;
        setIndex(n2);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void dump(DataOutputStream out) throws IOException {
        if (wide()) {
            out.writeByte(196);
        }
        out.writeByte(this.opcode);
        if (this.length > 1) {
            if (wide()) {
                out.writeShort(this.f11998n);
            } else {
                out.writeByte(this.f11998n);
            }
        }
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public String toString(boolean verbose) {
        if ((this.opcode >= 26 && this.opcode <= 45) || (this.opcode >= 59 && this.opcode <= 78)) {
            return super.toString(verbose);
        }
        return super.toString(verbose) + " " + this.f11998n;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException {
        if (wide) {
            this.f11998n = bytes.readUnsignedShort();
            this.length = (short) 4;
            return;
        }
        if ((this.opcode >= 21 && this.opcode <= 25) || (this.opcode >= 54 && this.opcode <= 58)) {
            this.f11998n = bytes.readUnsignedByte();
            this.length = (short) 2;
        } else if (this.opcode <= 45) {
            this.f11998n = (this.opcode - 26) % 4;
            this.length = (short) 1;
        } else {
            this.f11998n = (this.opcode - 59) % 4;
            this.length = (short) 1;
        }
    }

    @Override // com.sun.org.apache.bcel.internal.generic.IndexedInstruction
    public final int getIndex() {
        return this.f11998n;
    }

    public void setIndex(int n2) {
        if (n2 < 0 || n2 > 65535) {
            throw new ClassGenException("Illegal value: " + n2);
        }
        this.f11998n = n2;
        if (n2 >= 0 && n2 <= 3) {
            this.opcode = (short) (this.c_tag + n2);
            this.length = (short) 1;
            return;
        }
        this.opcode = this.canon_tag;
        if (wide()) {
            this.length = (short) 4;
        } else {
            this.length = (short) 2;
        }
    }

    public short getCanonicalTag() {
        return this.canon_tag;
    }

    public Type getType(ConstantPoolGen cp) {
        switch (this.canon_tag) {
            case 21:
            case 54:
                return Type.INT;
            case 22:
            case 55:
                return Type.LONG;
            case 23:
            case 56:
                return Type.FLOAT;
            case 24:
            case 57:
                return Type.DOUBLE;
            case 25:
            case 58:
                return Type.OBJECT;
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            default:
                throw new ClassGenException("Oops: unknown case in switch" + ((int) this.canon_tag));
        }
    }
}
