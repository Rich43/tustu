package com.sun.org.apache.bcel.internal.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/ConstantDouble.class */
public final class ConstantDouble extends Constant implements ConstantObject {
    private double bytes;

    public ConstantDouble(double bytes) {
        super((byte) 6);
        this.bytes = bytes;
    }

    public ConstantDouble(ConstantDouble c2) {
        this(c2.getBytes());
    }

    ConstantDouble(DataInputStream file) throws IOException {
        this(file.readDouble());
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Constant, com.sun.org.apache.bcel.internal.classfile.Node
    public void accept(Visitor v2) {
        v2.visitConstantDouble(this);
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Constant
    public final void dump(DataOutputStream file) throws IOException {
        file.writeByte(this.tag);
        file.writeDouble(this.bytes);
    }

    public final double getBytes() {
        return this.bytes;
    }

    public final void setBytes(double bytes) {
        this.bytes = bytes;
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Constant
    public final String toString() {
        return super.toString() + "(bytes = " + this.bytes + ")";
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.ConstantObject
    public Object getConstantValue(ConstantPool cp) {
        return new Double(this.bytes);
    }
}
