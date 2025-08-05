package com.sun.org.apache.bcel.internal.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/InnerClasses.class */
public final class InnerClasses extends Attribute {
    private InnerClass[] inner_classes;
    private int number_of_classes;

    public InnerClasses(InnerClasses c2) {
        this(c2.getNameIndex(), c2.getLength(), c2.getInnerClasses(), c2.getConstantPool());
    }

    public InnerClasses(int name_index, int length, InnerClass[] inner_classes, ConstantPool constant_pool) {
        super((byte) 6, name_index, length, constant_pool);
        setInnerClasses(inner_classes);
    }

    InnerClasses(int name_index, int length, DataInputStream file, ConstantPool constant_pool) throws IOException {
        this(name_index, length, (InnerClass[]) null, constant_pool);
        this.number_of_classes = file.readUnsignedShort();
        this.inner_classes = new InnerClass[this.number_of_classes];
        for (int i2 = 0; i2 < this.number_of_classes; i2++) {
            this.inner_classes[i2] = new InnerClass(file);
        }
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute, com.sun.org.apache.bcel.internal.classfile.Node
    public void accept(Visitor v2) {
        v2.visitInnerClasses(this);
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public final void dump(DataOutputStream file) throws IOException {
        super.dump(file);
        file.writeShort(this.number_of_classes);
        for (int i2 = 0; i2 < this.number_of_classes; i2++) {
            this.inner_classes[i2].dump(file);
        }
    }

    public final InnerClass[] getInnerClasses() {
        return this.inner_classes;
    }

    public final void setInnerClasses(InnerClass[] inner_classes) {
        this.inner_classes = inner_classes;
        this.number_of_classes = inner_classes == null ? 0 : inner_classes.length;
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public final String toString() {
        StringBuffer buf = new StringBuffer();
        for (int i2 = 0; i2 < this.number_of_classes; i2++) {
            buf.append(this.inner_classes[i2].toString(this.constant_pool) + "\n");
        }
        return buf.toString();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public Attribute copy(ConstantPool constant_pool) {
        InnerClasses c2 = (InnerClasses) clone();
        c2.inner_classes = new InnerClass[this.number_of_classes];
        for (int i2 = 0; i2 < this.number_of_classes; i2++) {
            c2.inner_classes[i2] = this.inner_classes[i2].copy();
        }
        c2.constant_pool = constant_pool;
        return c2;
    }
}
