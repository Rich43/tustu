package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.util.ByteSequence;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/TABLESWITCH.class */
public class TABLESWITCH extends Select {
    TABLESWITCH() {
    }

    public TABLESWITCH(int[] match, InstructionHandle[] targets, InstructionHandle target) {
        super((short) 170, match, targets, target);
        this.length = (short) (13 + (this.match_length * 4));
        this.fixed_length = this.length;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Select, com.sun.org.apache.bcel.internal.generic.BranchInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    public void dump(DataOutputStream out) throws IOException {
        super.dump(out);
        int low = this.match_length > 0 ? this.match[0] : 0;
        out.writeInt(low);
        int high = this.match_length > 0 ? this.match[this.match_length - 1] : 0;
        out.writeInt(high);
        for (int i2 = 0; i2 < this.match_length; i2++) {
            int targetOffset = getTargetOffset(this.targets[i2]);
            this.indices[i2] = targetOffset;
            out.writeInt(targetOffset);
        }
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Select, com.sun.org.apache.bcel.internal.generic.BranchInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException {
        super.initFromFile(bytes, wide);
        int low = bytes.readInt();
        int high = bytes.readInt();
        this.match_length = (high - low) + 1;
        this.fixed_length = (short) (13 + (this.match_length * 4));
        this.length = (short) (this.fixed_length + this.padding);
        this.match = new int[this.match_length];
        this.indices = new int[this.match_length];
        this.targets = new InstructionHandle[this.match_length];
        for (int i2 = low; i2 <= high; i2++) {
            this.match[i2 - low] = i2;
        }
        for (int i3 = 0; i3 < this.match_length; i3++) {
            this.indices[i3] = bytes.readInt();
        }
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void accept(Visitor v2) {
        v2.visitVariableLengthInstruction(this);
        v2.visitStackProducer(this);
        v2.visitBranchInstruction(this);
        v2.visitSelect(this);
        v2.visitTABLESWITCH(this);
    }
}
