package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.Constants;
import com.sun.org.apache.bcel.internal.classfile.ConstantPool;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/FieldInstruction.class */
public abstract class FieldInstruction extends FieldOrMethod implements TypedInstruction {
    FieldInstruction() {
    }

    protected FieldInstruction(short opcode, int index) {
        super(opcode, index);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.CPInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    public String toString(ConstantPool cp) {
        return Constants.OPCODE_NAMES[this.opcode] + " " + cp.constantToString(this.index, (byte) 9);
    }

    protected int getFieldSize(ConstantPoolGen cpg) {
        return getType(cpg).getSize();
    }

    @Override // com.sun.org.apache.bcel.internal.generic.CPInstruction, com.sun.org.apache.bcel.internal.generic.TypedInstruction
    public Type getType(ConstantPoolGen cpg) {
        return getFieldType(cpg);
    }

    public Type getFieldType(ConstantPoolGen cpg) {
        return Type.getType(getSignature(cpg));
    }

    public String getFieldName(ConstantPoolGen cpg) {
        return getName(cpg);
    }
}
