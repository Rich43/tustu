package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.util.ByteSequence;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/GOTO_W.class */
public class GOTO_W extends GotoInstruction {
    GOTO_W() {
    }

    public GOTO_W(InstructionHandle target) {
        super((short) 200, target);
        this.length = (short) 5;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.BranchInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    public void dump(DataOutputStream out) throws IOException {
        this.index = getTargetOffset();
        out.writeByte(this.opcode);
        out.writeInt(this.index);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.BranchInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException {
        this.index = bytes.readInt();
        this.length = (short) 5;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitUnconditionalBranch(this);
        v2.visitBranchInstruction(this);
        v2.visitGotoInstruction(this);
        v2.visitGOTO_W(this);
    }
}
