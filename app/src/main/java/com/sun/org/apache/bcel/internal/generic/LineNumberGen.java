package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.classfile.LineNumber;
import java.io.Serializable;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/LineNumberGen.class */
public class LineNumberGen implements InstructionTargeter, Cloneable, Serializable {
    private InstructionHandle ih;
    private int src_line;

    public LineNumberGen(InstructionHandle ih, int src_line) {
        setInstruction(ih);
        setSourceLine(src_line);
    }

    @Override // com.sun.org.apache.bcel.internal.generic.InstructionTargeter
    public boolean containsTarget(InstructionHandle ih) {
        return this.ih == ih;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.InstructionTargeter
    public void updateTarget(InstructionHandle old_ih, InstructionHandle new_ih) {
        if (old_ih != this.ih) {
            throw new ClassGenException("Not targeting " + ((Object) old_ih) + ", but " + ((Object) this.ih) + "}");
        }
        setInstruction(new_ih);
    }

    public LineNumber getLineNumber() {
        return new LineNumber(this.ih.getPosition(), this.src_line);
    }

    public final void setInstruction(InstructionHandle ih) {
        BranchInstruction.notifyTargetChanging(this.ih, this);
        this.ih = ih;
        BranchInstruction.notifyTargetChanged(this.ih, this);
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            System.err.println(e2);
            return null;
        }
    }

    public InstructionHandle getInstruction() {
        return this.ih;
    }

    public void setSourceLine(int src_line) {
        this.src_line = src_line;
    }

    public int getSourceLine() {
        return this.src_line;
    }
}
