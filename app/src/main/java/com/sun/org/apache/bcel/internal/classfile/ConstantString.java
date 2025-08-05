package com.sun.org.apache.bcel.internal.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/ConstantString.class */
public final class ConstantString extends Constant implements ConstantObject {
    private int string_index;

    public ConstantString(ConstantString c2) {
        this(c2.getStringIndex());
    }

    ConstantString(DataInputStream file) throws IOException {
        this(file.readUnsignedShort());
    }

    public ConstantString(int string_index) {
        super((byte) 8);
        this.string_index = string_index;
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Constant, com.sun.org.apache.bcel.internal.classfile.Node
    public void accept(Visitor v2) {
        v2.visitConstantString(this);
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Constant
    public final void dump(DataOutputStream file) throws IOException {
        file.writeByte(this.tag);
        file.writeShort(this.string_index);
    }

    public final int getStringIndex() {
        return this.string_index;
    }

    public final void setStringIndex(int string_index) {
        this.string_index = string_index;
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Constant
    public final String toString() {
        return super.toString() + "(string_index = " + this.string_index + ")";
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.ConstantObject
    public Object getConstantValue(ConstantPool cp) throws ClassFormatException {
        Constant c2 = cp.getConstant(this.string_index, (byte) 1);
        return ((ConstantUtf8) c2).getBytes();
    }

    public String getBytes(ConstantPool cp) {
        return (String) getConstantValue(cp);
    }
}
