package com.sun.org.apache.bcel.internal.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/ConstantNameAndType.class */
public final class ConstantNameAndType extends Constant {
    private int name_index;
    private int signature_index;

    public ConstantNameAndType(ConstantNameAndType c2) {
        this(c2.getNameIndex(), c2.getSignatureIndex());
    }

    ConstantNameAndType(DataInputStream file) throws IOException {
        this(file.readUnsignedShort(), file.readUnsignedShort());
    }

    public ConstantNameAndType(int name_index, int signature_index) {
        super((byte) 12);
        this.name_index = name_index;
        this.signature_index = signature_index;
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Constant, com.sun.org.apache.bcel.internal.classfile.Node
    public void accept(Visitor v2) {
        v2.visitConstantNameAndType(this);
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Constant
    public final void dump(DataOutputStream file) throws IOException {
        file.writeByte(this.tag);
        file.writeShort(this.name_index);
        file.writeShort(this.signature_index);
    }

    public final int getNameIndex() {
        return this.name_index;
    }

    public final String getName(ConstantPool cp) {
        return cp.constantToString(getNameIndex(), (byte) 1);
    }

    public final int getSignatureIndex() {
        return this.signature_index;
    }

    public final String getSignature(ConstantPool cp) {
        return cp.constantToString(getSignatureIndex(), (byte) 1);
    }

    public final void setNameIndex(int name_index) {
        this.name_index = name_index;
    }

    public final void setSignatureIndex(int signature_index) {
        this.signature_index = signature_index;
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Constant
    public final String toString() {
        return super.toString() + "(name_index = " + this.name_index + ", signature_index = " + this.signature_index + ")";
    }
}
