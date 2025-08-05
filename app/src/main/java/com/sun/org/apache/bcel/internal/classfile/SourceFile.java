package com.sun.org.apache.bcel.internal.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/SourceFile.class */
public final class SourceFile extends Attribute {
    private int sourcefile_index;

    public SourceFile(SourceFile c2) {
        this(c2.getNameIndex(), c2.getLength(), c2.getSourceFileIndex(), c2.getConstantPool());
    }

    SourceFile(int name_index, int length, DataInputStream file, ConstantPool constant_pool) throws IOException {
        this(name_index, length, file.readUnsignedShort(), constant_pool);
    }

    public SourceFile(int name_index, int length, int sourcefile_index, ConstantPool constant_pool) {
        super((byte) 0, name_index, length, constant_pool);
        this.sourcefile_index = sourcefile_index;
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute, com.sun.org.apache.bcel.internal.classfile.Node
    public void accept(Visitor v2) {
        v2.visitSourceFile(this);
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public final void dump(DataOutputStream file) throws IOException {
        super.dump(file);
        file.writeShort(this.sourcefile_index);
    }

    public final int getSourceFileIndex() {
        return this.sourcefile_index;
    }

    public final void setSourceFileIndex(int sourcefile_index) {
        this.sourcefile_index = sourcefile_index;
    }

    public final String getSourceFileName() {
        ConstantUtf8 c2 = (ConstantUtf8) this.constant_pool.getConstant(this.sourcefile_index, (byte) 1);
        return c2.getBytes();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public final String toString() {
        return "SourceFile(" + getSourceFileName() + ")";
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public Attribute copy(ConstantPool constant_pool) {
        return (SourceFile) clone();
    }
}
