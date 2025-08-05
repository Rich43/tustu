package com.sun.org.apache.bcel.internal.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/ConstantValue.class */
public final class ConstantValue extends Attribute {
    private int constantvalue_index;

    public ConstantValue(ConstantValue c2) {
        this(c2.getNameIndex(), c2.getLength(), c2.getConstantValueIndex(), c2.getConstantPool());
    }

    ConstantValue(int name_index, int length, DataInputStream file, ConstantPool constant_pool) throws IOException {
        this(name_index, length, file.readUnsignedShort(), constant_pool);
    }

    public ConstantValue(int name_index, int length, int constantvalue_index, ConstantPool constant_pool) {
        super((byte) 1, name_index, length, constant_pool);
        this.constantvalue_index = constantvalue_index;
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute, com.sun.org.apache.bcel.internal.classfile.Node
    public void accept(Visitor v2) {
        v2.visitConstantValue(this);
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public final void dump(DataOutputStream file) throws IOException {
        super.dump(file);
        file.writeShort(this.constantvalue_index);
    }

    public final int getConstantValueIndex() {
        return this.constantvalue_index;
    }

    public final void setConstantValueIndex(int constantvalue_index) {
        this.constantvalue_index = constantvalue_index;
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public final String toString() throws ClassFormatException {
        String buf;
        Constant c2 = this.constant_pool.getConstant(this.constantvalue_index);
        switch (c2.getTag()) {
            case 3:
                buf = "" + ((ConstantInteger) c2).getBytes();
                break;
            case 4:
                buf = "" + ((ConstantFloat) c2).getBytes();
                break;
            case 5:
                buf = "" + ((ConstantLong) c2).getBytes();
                break;
            case 6:
                buf = "" + ((ConstantDouble) c2).getBytes();
                break;
            case 7:
            default:
                throw new IllegalStateException("Type of ConstValue invalid: " + ((Object) c2));
            case 8:
                int i2 = ((ConstantString) c2).getStringIndex();
                buf = PdfOps.DOUBLE_QUOTE__TOKEN + Utility.convertString(((ConstantUtf8) this.constant_pool.getConstant(i2, (byte) 1)).getBytes()) + PdfOps.DOUBLE_QUOTE__TOKEN;
                break;
        }
        return buf;
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public Attribute copy(ConstantPool constant_pool) {
        ConstantValue c2 = (ConstantValue) clone();
        c2.constant_pool = constant_pool;
        return c2;
    }
}
