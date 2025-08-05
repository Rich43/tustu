package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.util.ByteSequence;
import java.io.DataOutputStream;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/BranchInstruction.class */
public abstract class BranchInstruction extends Instruction implements InstructionTargeter {
    protected int index;
    protected InstructionHandle target;
    protected int position;

    BranchInstruction() {
    }

    protected BranchInstruction(short opcode, InstructionHandle target) {
        super(opcode, (short) 3);
        setTarget(target);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public void dump(DataOutputStream out) throws IOException {
        out.writeByte(this.opcode);
        this.index = getTargetOffset();
        if (Math.abs(this.index) >= 32767) {
            throw new ClassGenException("Branch target offset too large for short");
        }
        out.writeShort(this.index);
    }

    protected int getTargetOffset(InstructionHandle target) {
        if (target == null) {
            throw new ClassGenException("Target of " + super.toString(true) + " is invalid null handle");
        }
        int t2 = target.getPosition();
        if (t2 < 0) {
            throw new ClassGenException("Invalid branch target position offset for " + super.toString(true) + CallSiteDescriptor.TOKEN_DELIMITER + t2 + CallSiteDescriptor.TOKEN_DELIMITER + ((Object) target));
        }
        return t2 - this.position;
    }

    protected int getTargetOffset() {
        return getTargetOffset(this.target);
    }

    protected int updatePosition(int offset, int max_offset) {
        this.position += offset;
        return 0;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    public String toString(boolean verbose) {
        String s2 = super.toString(verbose);
        String t2 = FXMLLoader.NULL_KEYWORD;
        if (verbose) {
            if (this.target != null) {
                t2 = this.target.getInstruction() == this ? "<points to itself>" : this.target.getInstruction() == null ? "<null instruction!!!?>" : this.target.getInstruction().toString(false);
            }
        } else if (this.target != null) {
            this.index = getTargetOffset();
            t2 = "" + (this.index + this.position);
        }
        return s2 + " -> " + t2;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException {
        this.length = (short) 3;
        this.index = bytes.readShort();
    }

    public final int getIndex() {
        return this.index;
    }

    public InstructionHandle getTarget() {
        return this.target;
    }

    public final void setTarget(InstructionHandle target) {
        notifyTargetChanging(this.target, this);
        this.target = target;
        notifyTargetChanged(this.target, this);
    }

    static void notifyTargetChanging(InstructionHandle old_ih, InstructionTargeter t2) {
        if (old_ih != null) {
            old_ih.removeTargeter(t2);
        }
    }

    static void notifyTargetChanged(InstructionHandle new_ih, InstructionTargeter t2) {
        if (new_ih != null) {
            new_ih.addTargeter(t2);
        }
    }

    @Override // com.sun.org.apache.bcel.internal.generic.InstructionTargeter
    public void updateTarget(InstructionHandle old_ih, InstructionHandle new_ih) {
        if (this.target == old_ih) {
            setTarget(new_ih);
            return;
        }
        throw new ClassGenException("Not targeting " + ((Object) old_ih) + ", but " + ((Object) this.target));
    }

    @Override // com.sun.org.apache.bcel.internal.generic.InstructionTargeter
    public boolean containsTarget(InstructionHandle ih) {
        return this.target == ih;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.Instruction
    void dispose() {
        setTarget(null);
        this.index = -1;
        this.position = -1;
    }
}
