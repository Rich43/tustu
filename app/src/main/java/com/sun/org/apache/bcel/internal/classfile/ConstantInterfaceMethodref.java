package com.sun.org.apache.bcel.internal.classfile;

import java.io.DataInputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/ConstantInterfaceMethodref.class */
public final class ConstantInterfaceMethodref extends ConstantCP {
    public ConstantInterfaceMethodref(ConstantInterfaceMethodref c2) {
        super((byte) 11, c2.getClassIndex(), c2.getNameAndTypeIndex());
    }

    ConstantInterfaceMethodref(DataInputStream file) throws IOException {
        super((byte) 11, file);
    }

    public ConstantInterfaceMethodref(int class_index, int name_and_type_index) {
        super((byte) 11, class_index, name_and_type_index);
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Constant, com.sun.org.apache.bcel.internal.classfile.Node
    public void accept(Visitor v2) {
        v2.visitConstantInterfaceMethodref(this);
    }
}
