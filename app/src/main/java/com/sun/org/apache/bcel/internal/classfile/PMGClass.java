package com.sun.org.apache.bcel.internal.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/PMGClass.class */
public final class PMGClass extends Attribute {
    private int pmg_class_index;
    private int pmg_index;

    public PMGClass(PMGClass c2) {
        this(c2.getNameIndex(), c2.getLength(), c2.getPMGIndex(), c2.getPMGClassIndex(), c2.getConstantPool());
    }

    PMGClass(int name_index, int length, DataInputStream file, ConstantPool constant_pool) throws IOException {
        this(name_index, length, file.readUnsignedShort(), file.readUnsignedShort(), constant_pool);
    }

    public PMGClass(int name_index, int length, int pmg_index, int pmg_class_index, ConstantPool constant_pool) {
        super((byte) 9, name_index, length, constant_pool);
        this.pmg_index = pmg_index;
        this.pmg_class_index = pmg_class_index;
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute, com.sun.org.apache.bcel.internal.classfile.Node
    public void accept(Visitor v2) {
        System.err.println("Visiting non-standard PMGClass object");
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public final void dump(DataOutputStream file) throws IOException {
        super.dump(file);
        file.writeShort(this.pmg_index);
        file.writeShort(this.pmg_class_index);
    }

    public final int getPMGClassIndex() {
        return this.pmg_class_index;
    }

    public final void setPMGClassIndex(int pmg_class_index) {
        this.pmg_class_index = pmg_class_index;
    }

    public final int getPMGIndex() {
        return this.pmg_index;
    }

    public final void setPMGIndex(int pmg_index) {
        this.pmg_index = pmg_index;
    }

    public final String getPMGName() {
        ConstantUtf8 c2 = (ConstantUtf8) this.constant_pool.getConstant(this.pmg_index, (byte) 1);
        return c2.getBytes();
    }

    public final String getPMGClassName() {
        ConstantUtf8 c2 = (ConstantUtf8) this.constant_pool.getConstant(this.pmg_class_index, (byte) 1);
        return c2.getBytes();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public final String toString() {
        return "PMGClass(" + getPMGName() + ", " + getPMGClassName() + ")";
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public Attribute copy(ConstantPool constant_pool) {
        return (PMGClass) clone();
    }
}
