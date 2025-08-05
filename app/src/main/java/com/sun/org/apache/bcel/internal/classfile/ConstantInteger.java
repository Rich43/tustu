package com.sun.org.apache.bcel.internal.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/ConstantInteger.class */
public final class ConstantInteger extends Constant implements ConstantObject {
    private int bytes;

    public ConstantInteger(int bytes) {
        super((byte) 3);
        this.bytes = bytes;
    }

    public ConstantInteger(ConstantInteger c2) {
        this(c2.getBytes());
    }

    ConstantInteger(DataInputStream file) throws IOException {
        this(file.readInt());
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Constant, com.sun.org.apache.bcel.internal.classfile.Node
    public void accept(Visitor v2) {
        v2.visitConstantInteger(this);
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Constant
    public final void dump(DataOutputStream file) throws IOException {
        file.writeByte(this.tag);
        file.writeInt(this.bytes);
    }

    public final int getBytes() {
        return this.bytes;
    }

    public final void setBytes(int bytes) {
        this.bytes = bytes;
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Constant
    public final String toString() {
        return super.toString() + "(bytes = " + this.bytes + ")";
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.ConstantObject
    public Object getConstantValue(ConstantPool cp) {
        return new Integer(this.bytes);
    }
}
