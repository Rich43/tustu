package com.sun.org.apache.bcel.internal.generic;

import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/GOTO.class */
public class GOTO extends GotoInstruction implements VariableLengthInstruction {
    GOTO() {
    }

    public GOTO(InstructionHandle target) {
        super((short) 167, target);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.BranchInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    public void dump(DataOutputStream out) throws IOException {
        this.index = getTargetOffset();
        if (this.opcode == 167) {
            super.dump(out);
            return;
        }
        this.index = getTargetOffset();
        out.writeByte(this.opcode);
        out.writeInt(this.index);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.BranchInstruction
    protected int updatePosition(int offset, int max_offset) {
        int i2 = getTargetOffset();
        this.position += offset;
        if (Math.abs(i2) >= Short.MAX_VALUE - max_offset) {
            this.opcode = (short) 200;
            this.length = (short) 5;
            return 2;
        }
        return 0;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitVariableLengthInstruction(this);
        v2.visitUnconditionalBranch(this);
        v2.visitBranchInstruction(this);
        v2.visitGotoInstruction(this);
        v2.visitGOTO(this);
    }
}
