package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.util.ByteSequence;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/LOOKUPSWITCH.class */
public class LOOKUPSWITCH extends Select {
    LOOKUPSWITCH() {
    }

    public LOOKUPSWITCH(int[] match, InstructionHandle[] targets, InstructionHandle target) {
        super((short) 171, match, targets, target);
        this.length = (short) (9 + (this.match_length * 8));
        this.fixed_length = this.length;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Select, com.sun.org.apache.bcel.internal.generic.BranchInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    public void dump(DataOutputStream out) throws IOException {
        super.dump(out);
        out.writeInt(this.match_length);
        for (int i2 = 0; i2 < this.match_length; i2++) {
            out.writeInt(this.match[i2]);
            int targetOffset = getTargetOffset(this.targets[i2]);
            this.indices[i2] = targetOffset;
            out.writeInt(targetOffset);
        }
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Select, com.sun.org.apache.bcel.internal.generic.BranchInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException {
        super.initFromFile(bytes, wide);
        this.match_length = bytes.readInt();
        this.fixed_length = (short) (9 + (this.match_length * 8));
        this.length = (short) (this.fixed_length + this.padding);
        this.match = new int[this.match_length];
        this.indices = new int[this.match_length];
        this.targets = new InstructionHandle[this.match_length];
        for (int i2 = 0; i2 < this.match_length; i2++) {
            this.match[i2] = bytes.readInt();
            this.indices[i2] = bytes.readInt();
        }
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitVariableLengthInstruction(this);
        v2.visitStackProducer(this);
        v2.visitBranchInstruction(this);
        v2.visitSelect(this);
        v2.visitLOOKUPSWITCH(this);
    }
}
