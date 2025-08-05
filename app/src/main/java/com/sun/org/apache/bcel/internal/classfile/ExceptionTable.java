package com.sun.org.apache.bcel.internal.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/ExceptionTable.class */
public final class ExceptionTable extends Attribute {
    private int number_of_exceptions;
    private int[] exception_index_table;

    public ExceptionTable(ExceptionTable c2) {
        this(c2.getNameIndex(), c2.getLength(), c2.getExceptionIndexTable(), c2.getConstantPool());
    }

    public ExceptionTable(int name_index, int length, int[] exception_index_table, ConstantPool constant_pool) {
        super((byte) 3, name_index, length, constant_pool);
        setExceptionIndexTable(exception_index_table);
    }

    ExceptionTable(int name_index, int length, DataInputStream file, ConstantPool constant_pool) throws IOException {
        this(name_index, length, (int[]) null, constant_pool);
        this.number_of_exceptions = file.readUnsignedShort();
        this.exception_index_table = new int[this.number_of_exceptions];
        for (int i2 = 0; i2 < this.number_of_exceptions; i2++) {
            this.exception_index_table[i2] = file.readUnsignedShort();
        }
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute, com.sun.org.apache.bcel.internal.classfile.Node
    public void accept(Visitor v2) {
        v2.visitExceptionTable(this);
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public final void dump(DataOutputStream file) throws IOException {
        super.dump(file);
        file.writeShort(this.number_of_exceptions);
        for (int i2 = 0; i2 < this.number_of_exceptions; i2++) {
            file.writeShort(this.exception_index_table[i2]);
        }
    }

    public final int[] getExceptionIndexTable() {
        return this.exception_index_table;
    }

    public final int getNumberOfExceptions() {
        return this.number_of_exceptions;
    }

    public final String[] getExceptionNames() {
        String[] names = new String[this.number_of_exceptions];
        for (int i2 = 0; i2 < this.number_of_exceptions; i2++) {
            names[i2] = this.constant_pool.getConstantString(this.exception_index_table[i2], (byte) 7).replace('/', '.');
        }
        return names;
    }

    public final void setExceptionIndexTable(int[] exception_index_table) {
        this.exception_index_table = exception_index_table;
        this.number_of_exceptions = exception_index_table == null ? 0 : exception_index_table.length;
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public final String toString() {
        StringBuffer buf = new StringBuffer("");
        for (int i2 = 0; i2 < this.number_of_exceptions; i2++) {
            String str = this.constant_pool.getConstantString(this.exception_index_table[i2], (byte) 7);
            buf.append(Utility.compactClassName(str, false));
            if (i2 < this.number_of_exceptions - 1) {
                buf.append(", ");
            }
        }
        return buf.toString();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public Attribute copy(ConstantPool constant_pool) {
        ExceptionTable c2 = (ExceptionTable) clone();
        c2.exception_index_table = (int[]) this.exception_index_table.clone();
        c2.constant_pool = constant_pool;
        return c2;
    }
}
