package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.Constants;
import com.sun.org.apache.bcel.internal.classfile.ClassFormatException;
import com.sun.org.apache.bcel.internal.classfile.Constant;
import com.sun.org.apache.bcel.internal.classfile.ConstantClass;
import com.sun.org.apache.bcel.internal.classfile.ConstantPool;
import com.sun.org.apache.bcel.internal.util.ByteSequence;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/CPInstruction.class */
public abstract class CPInstruction extends Instruction implements TypedInstruction, IndexedInstruction {
    protected int index;

    CPInstruction() {
    }

    protected CPInstruction(short opcode, int index) {
        super(opcode, (short) 3);
        setIndex(index);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void dump(DataOutputStream out) throws IOException {
        out.writeByte(this.opcode);
        out.writeShort(this.index);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public String toString(boolean verbose) {
        return super.toString(verbose) + " " + this.index;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public String toString(ConstantPool cp) throws ClassFormatException {
        Constant c2 = cp.getConstant(this.index);
        String str = cp.constantToString(c2);
        if (c2 instanceof ConstantClass) {
            str = str.replace('.', '/');
        }
        return Constants.OPCODE_NAMES[this.opcode] + " " + str;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException {
        setIndex(bytes.readUnsignedShort());
        this.length = (short) 3;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.IndexedInstruction
    public final int getIndex() {
        return this.index;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.IndexedInstruction
    public void setIndex(int index) {
        if (index < 0) {
            throw new ClassGenException("Negative index value: " + index);
        }
        this.index = index;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.TypedInstruction
    public Type getType(ConstantPoolGen cpg) throws ClassFormatException {
        ConstantPool cp = cpg.getConstantPool();
        String name = cp.getConstantString(this.index, (byte) 7);
        if (!name.startsWith("[")) {
            name = "L" + name + ";";
        }
        return Type.getType(name);
    }
}
