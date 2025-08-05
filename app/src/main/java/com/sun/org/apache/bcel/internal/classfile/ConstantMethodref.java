package com.sun.org.apache.bcel.internal.classfile;

import java.io.DataInputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/ConstantMethodref.class */
public final class ConstantMethodref extends ConstantCP {
    public ConstantMethodref(ConstantMethodref c2) {
        super((byte) 10, c2.getClassIndex(), c2.getNameAndTypeIndex());
    }

    ConstantMethodref(DataInputStream file) throws IOException {
        super((byte) 10, file);
    }

    public ConstantMethodref(int class_index, int name_and_type_index) {
        super((byte) 10, class_index, name_and_type_index);
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Constant, com.sun.org.apache.bcel.internal.classfile.Node
    public void accept(Visitor v2) {
        v2.visitConstantMethodref(this);
    }
}
