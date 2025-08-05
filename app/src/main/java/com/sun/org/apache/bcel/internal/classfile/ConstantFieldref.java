package com.sun.org.apache.bcel.internal.classfile;

import java.io.DataInputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/ConstantFieldref.class */
public final class ConstantFieldref extends ConstantCP {
    public ConstantFieldref(ConstantFieldref c2) {
        super((byte) 9, c2.getClassIndex(), c2.getNameAndTypeIndex());
    }

    ConstantFieldref(DataInputStream file) throws IOException {
        super((byte) 9, file);
    }

    public ConstantFieldref(int class_index, int name_and_type_index) {
        super((byte) 9, class_index, name_and_type_index);
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Constant, com.sun.org.apache.bcel.internal.classfile.Node
    public void accept(Visitor v2) {
        v2.visitConstantFieldref(this);
    }
}
