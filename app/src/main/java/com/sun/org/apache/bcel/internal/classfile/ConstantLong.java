package com.sun.org.apache.bcel.internal.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/ConstantLong.class */
public final class ConstantLong extends Constant implements ConstantObject {
    private long bytes;

    public ConstantLong(long bytes) {
        super((byte) 5);
        this.bytes = bytes;
    }

    public ConstantLong(ConstantLong c2) {
        this(c2.getBytes());
    }

    ConstantLong(DataInputStream file) throws IOException {
        this(file.readLong());
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Constant, com.sun.org.apache.bcel.internal.classfile.Node
    public void accept(Visitor v2) {
        v2.visitConstantLong(this);
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Constant
    public final void dump(DataOutputStream file) throws IOException {
        file.writeByte(this.tag);
        file.writeLong(this.bytes);
    }

    public final long getBytes() {
        return this.bytes;
    }

    public final void setBytes(long bytes) {
        this.bytes = bytes;
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Constant
    public final String toString() {
        return super.toString() + "(bytes = " + this.bytes + ")";
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.ConstantObject
    public Object getConstantValue(ConstantPool cp) {
        return new Long(this.bytes);
    }
}
