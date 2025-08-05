package com.sun.org.apache.bcel.internal.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/ConstantUtf8.class */
public final class ConstantUtf8 extends Constant {
    private String bytes;

    public ConstantUtf8(ConstantUtf8 c2) {
        this(c2.getBytes());
    }

    ConstantUtf8(DataInputStream file) throws IOException {
        super((byte) 1);
        this.bytes = file.readUTF();
    }

    public ConstantUtf8(String bytes) {
        super((byte) 1);
        if (bytes == null) {
            throw new IllegalArgumentException("bytes must not be null!");
        }
        this.bytes = bytes;
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Constant, com.sun.org.apache.bcel.internal.classfile.Node
    public void accept(Visitor v2) {
        v2.visitConstantUtf8(this);
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Constant
    public final void dump(DataOutputStream file) throws IOException {
        file.writeByte(this.tag);
        file.writeUTF(this.bytes);
    }

    public final String getBytes() {
        return this.bytes;
    }

    public final void setBytes(String bytes) {
        this.bytes = bytes;
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Constant
    public final String toString() {
        return super.toString() + "(\"" + Utility.replace(this.bytes, "\n", "\\n") + "\")";
    }
}
