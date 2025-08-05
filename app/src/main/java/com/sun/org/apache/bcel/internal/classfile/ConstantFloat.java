package com.sun.org.apache.bcel.internal.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/ConstantFloat.class */
public final class ConstantFloat extends Constant implements ConstantObject {
    private float bytes;

    public ConstantFloat(float bytes) {
        super((byte) 4);
        this.bytes = bytes;
    }

    public ConstantFloat(ConstantFloat c2) {
        this(c2.getBytes());
    }

    ConstantFloat(DataInputStream file) throws IOException {
        this(file.readFloat());
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Constant, com.sun.org.apache.bcel.internal.classfile.Node
    public void accept(Visitor v2) {
        v2.visitConstantFloat(this);
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Constant
    public final void dump(DataOutputStream file) throws IOException {
        file.writeByte(this.tag);
        file.writeFloat(this.bytes);
    }

    public final float getBytes() {
        return this.bytes;
    }

    public final void setBytes(float bytes) {
        this.bytes = bytes;
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Constant
    public final String toString() {
        return super.toString() + "(bytes = " + this.bytes + ")";
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.ConstantObject
    public Object getConstantValue(ConstantPool cp) {
        return new Float(this.bytes);
    }
}
