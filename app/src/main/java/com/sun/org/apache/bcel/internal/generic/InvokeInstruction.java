package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.Constants;
import com.sun.org.apache.bcel.internal.classfile.Constant;
import com.sun.org.apache.bcel.internal.classfile.ConstantPool;
import java.util.StringTokenizer;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/InvokeInstruction.class */
public abstract class InvokeInstruction extends FieldOrMethod implements ExceptionThrower, TypedInstruction, StackConsumer, StackProducer {
    InvokeInstruction() {
    }

    protected InvokeInstruction(short opcode, int index) {
        super(opcode, index);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.CPInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    public String toString(ConstantPool cp) {
        Constant c2 = cp.getConstant(this.index);
        StringTokenizer tok = new StringTokenizer(cp.constantToString(c2));
        return Constants.OPCODE_NAMES[this.opcode] + " " + tok.nextToken().replace('.', '/') + tok.nextToken();
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction, com.sun.org.apache.bcel.internal.generic.StackConsumer
    public int consumeStack(ConstantPoolGen cpg) {
        int sum;
        String signature = getSignature(cpg);
        Type[] args = Type.getArgumentTypes(signature);
        if (this.opcode == 184) {
            sum = 0;
        } else {
            sum = 1;
        }
        for (Type type : args) {
            sum += type.getSize();
        }
        return sum;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction, com.sun.org.apache.bcel.internal.generic.StackProducer
    public int produceStack(ConstantPoolGen cpg) {
        return getReturnType(cpg).getSize();
    }

    @Override // com.sun.org.apache.bcel.internal.generic.CPInstruction, com.sun.org.apache.bcel.internal.generic.TypedInstruction
    public Type getType(ConstantPoolGen cpg) {
        return getReturnType(cpg);
    }

    public String getMethodName(ConstantPoolGen cpg) {
        return getName(cpg);
    }

    public Type getReturnType(ConstantPoolGen cpg) {
        return Type.getReturnType(getSignature(cpg));
    }

    public Type[] getArgumentTypes(ConstantPoolGen cpg) {
        return Type.getArgumentTypes(getSignature(cpg));
    }
}
