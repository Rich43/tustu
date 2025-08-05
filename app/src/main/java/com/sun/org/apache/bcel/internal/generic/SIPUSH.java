package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.util.ByteSequence;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/SIPUSH.class */
public class SIPUSH extends Instruction implements ConstantPushInstruction {

    /* renamed from: b, reason: collision with root package name */
    private short f11999b;

    SIPUSH() {
    }

    public SIPUSH(short b2) {
        super((short) 17, (short) 3);
        this.f11999b = b2;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void dump(DataOutputStream out) throws IOException {
        super.dump(out);
        out.writeShort(this.f11999b);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public String toString(boolean verbose) {
        return super.toString(verbose) + " " + ((int) this.f11999b);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException {
        this.length = (short) 3;
        this.f11999b = bytes.readShort();
    }

    @Override // com.sun.org.apache.bcel.internal.generic.ConstantPushInstruction
    public Number getValue() {
        return new Integer(this.f11999b);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.TypedInstruction
    public Type getType(ConstantPoolGen cp) {
        return Type.SHORT;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitPushInstruction(this);
        v2.visitStackProducer(this);
        v2.visitTypedInstruction(this);
        v2.visitConstantPushInstruction(this);
        v2.visitSIPUSH(this);
    }
}
