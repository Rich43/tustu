package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.util.ByteSequence;
import java.io.DataOutputStream;
import java.io.IOException;
import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/Select.class */
public abstract class Select extends BranchInstruction implements VariableLengthInstruction, StackProducer {
    protected int[] match;
    protected int[] indices;
    protected InstructionHandle[] targets;
    protected int fixed_length;
    protected int match_length;
    protected int padding;

    Select() {
        this.padding = 0;
    }

    Select(short opcode, int[] match, InstructionHandle[] targets, InstructionHandle target) {
        super(opcode, target);
        this.padding = 0;
        this.targets = targets;
        for (InstructionHandle instructionHandle : targets) {
            BranchInstruction.notifyTargetChanged(instructionHandle, this);
        }
        this.match = match;
        int length = match.length;
        this.match_length = length;
        if (length != targets.length) {
            throw new ClassGenException("Match and target array have not the same length");
        }
        this.indices = new int[this.match_length];
    }

    @Override // com.sun.org.apache.bcel.internal.generic.BranchInstruction
    protected int updatePosition(int offset, int max_offset) {
        this.position += offset;
        short old_length = this.length;
        this.padding = (4 - ((this.position + 1) % 4)) % 4;
        this.length = (short) (this.fixed_length + this.padding);
        return this.length - old_length;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.BranchInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    public void dump(DataOutputStream out) throws IOException {
        out.writeByte(this.opcode);
        for (int i2 = 0; i2 < this.padding; i2++) {
            out.writeByte(0);
        }
        this.index = getTargetOffset();
        out.writeInt(this.index);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.BranchInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException {
        this.padding = (4 - (bytes.getIndex() % 4)) % 4;
        for (int i2 = 0; i2 < this.padding; i2++) {
            bytes.readByte();
        }
        this.index = bytes.readInt();
    }

    @Override // com.sun.org.apache.bcel.internal.generic.BranchInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    public String toString(boolean verbose) {
        StringBuilder buf = new StringBuilder(super.toString(verbose));
        if (verbose) {
            for (int i2 = 0; i2 < this.match_length; i2++) {
                String s2 = FXMLLoader.NULL_KEYWORD;
                if (this.targets[i2] != null) {
                    s2 = this.targets[i2].getInstruction().toString();
                }
                buf.append("(").append(this.match[i2]).append(", ").append(s2).append(" = {").append(this.indices[i2]).append("})");
            }
        } else {
            buf.append(" ...");
        }
        return buf.toString();
    }

    public final void setTarget(int i2, InstructionHandle target) {
        notifyTargetChanging(this.targets[i2], this);
        this.targets[i2] = target;
        notifyTargetChanged(this.targets[i2], this);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.BranchInstruction, com.sun.org.apache.bcel.internal.generic.InstructionTargeter
    public void updateTarget(InstructionHandle old_ih, InstructionHandle new_ih) {
        boolean targeted = false;
        if (this.target == old_ih) {
            targeted = true;
            setTarget(new_ih);
        }
        for (int i2 = 0; i2 < this.targets.length; i2++) {
            if (this.targets[i2] == old_ih) {
                targeted = true;
                setTarget(i2, new_ih);
            }
        }
        if (!targeted) {
            throw new ClassGenException("Not targeting " + ((Object) old_ih));
        }
    }

    @Override // com.sun.org.apache.bcel.internal.generic.BranchInstruction, com.sun.org.apache.bcel.internal.generic.InstructionTargeter
    public boolean containsTarget(InstructionHandle ih) {
        if (this.target == ih) {
            return true;
        }
        for (int i2 = 0; i2 < this.targets.length; i2++) {
            if (this.targets[i2] == ih) {
                return true;
            }
        }
        return false;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.BranchInstruction, com.sun.org.apache.bcel.internal.generic.Instruction
    void dispose() {
        super.dispose();
        for (int i2 = 0; i2 < this.targets.length; i2++) {
            this.targets[i2].removeTargeter(this);
        }
    }

    public int[] getMatchs() {
        return this.match;
    }

    public int[] getIndices() {
        return this.indices;
    }

    public InstructionHandle[] getTargets() {
        return this.targets;
    }
}
