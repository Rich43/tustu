package com.sun.org.apache.xalan.internal.xsltc.compiler.util;

import com.sun.org.apache.bcel.internal.generic.Instruction;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/util/OutlineableChunkStart.class */
class OutlineableChunkStart extends MarkerInstruction {
    public static final Instruction OUTLINEABLECHUNKSTART = new OutlineableChunkStart();

    private OutlineableChunkStart() {
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public String getName() {
        return OutlineableChunkStart.class.getName();
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
