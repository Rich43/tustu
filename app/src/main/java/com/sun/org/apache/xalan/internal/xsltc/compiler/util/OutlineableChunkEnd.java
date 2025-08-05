package com.sun.org.apache.xalan.internal.xsltc.compiler.util;

import com.sun.org.apache.bcel.internal.generic.Instruction;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/util/OutlineableChunkEnd.class */
class OutlineableChunkEnd extends MarkerInstruction {
    public static final Instruction OUTLINEABLECHUNKEND = new OutlineableChunkEnd();

    private OutlineableChunkEnd() {
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public String getName() {
        return OutlineableChunkEnd.class.getName();
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public String toString() {
        return getName();
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public String toString(boolean verbose) {
        return getName();
    }
}
