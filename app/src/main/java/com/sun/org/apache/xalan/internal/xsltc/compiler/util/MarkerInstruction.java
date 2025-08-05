package com.sun.org.apache.xalan.internal.xsltc.compiler.util;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.Instruction;
import com.sun.org.apache.bcel.internal.generic.Visitor;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/util/MarkerInstruction.class */
abstract class MarkerInstruction extends Instruction {
    public MarkerInstruction() {
        super((short) -1, (short) 0);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction, com.sun.org.apache.bcel.internal.generic.StackConsumer
    public final int consumeStack(ConstantPoolGen cpg) {
        return 0;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction, com.sun.org.apache.bcel.internal.generic.StackProducer
    public final int produceStack(ConstantPoolGen cpg) {
        return 0;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public Instruction copy() {
        return this;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public final void dump(DataOutputStream out) throws IOException {
    }
}
