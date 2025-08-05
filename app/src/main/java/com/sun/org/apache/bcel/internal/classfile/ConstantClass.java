package com.sun.org.apache.bcel.internal.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/ConstantClass.class */
public final class ConstantClass extends Constant implements ConstantObject {
    private int name_index;

    public ConstantClass(ConstantClass c2) {
        this(c2.getNameIndex());
    }

    ConstantClass(DataInputStream file) throws IOException {
        this(file.readUnsignedShort());
    }

    public ConstantClass(int name_index) {
        super((byte) 7);
        this.name_index = name_index;
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Constant, com.sun.org.apache.bcel.internal.classfile.Node
    public void accept(Visitor v2) {
        v2.visitConstantClass(this);
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Constant
    public final void dump(DataOutputStream file) throws IOException {
        file.writeByte(this.tag);
        file.writeShort(this.name_index);
    }

    public final int getNameIndex() {
        return this.name_index;
    }

    public final void setNameIndex(int name_index) {
        this.name_index = name_index;
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.ConstantObject
    public Object getConstantValue(ConstantPool cp) throws ClassFormatException {
        Constant c2 = cp.getConstant(this.name_index, (byte) 1);
        return ((ConstantUtf8) c2).getBytes();
    }

    public String getBytes(ConstantPool cp) {
        return (String) getConstantValue(cp);
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Constant
    public final String toString() {
        return super.toString() + "(name_index = " + this.name_index + ")";
    }
}
