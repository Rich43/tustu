package com.sun.org.apache.bcel.internal.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/LineNumber.class */
public final class LineNumber implements Cloneable, Node, Serializable {
    private int start_pc;
    private int line_number;

    public LineNumber(LineNumber c2) {
        this(c2.getStartPC(), c2.getLineNumber());
    }

    LineNumber(DataInputStream file) throws IOException {
        this(file.readUnsignedShort(), file.readUnsignedShort());
    }

    public LineNumber(int start_pc, int line_number) {
        this.start_pc = start_pc;
        this.line_number = line_number;
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Node
    public void accept(Visitor v2) {
        v2.visitLineNumber(this);
    }

    public final void dump(DataOutputStream file) throws IOException {
        file.writeShort(this.start_pc);
        file.writeShort(this.line_number);
    }

    public final int getLineNumber() {
        return this.line_number;
    }

    public final int getStartPC() {
        return this.start_pc;
    }

    public final void setLineNumber(int line_number) {
        this.line_number = line_number;
    }

    public final void setStartPC(int start_pc) {
        this.start_pc = start_pc;
    }

    public final String toString() {
        return "LineNumber(" + this.start_pc + ", " + this.line_number + ")";
    }

    public LineNumber copy() {
        try {
            return (LineNumber) clone();
        } catch (CloneNotSupportedException e2) {
            return null;
        }
    }
}
