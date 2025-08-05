package com.sun.org.apache.bcel.internal.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/LineNumberTable.class */
public final class LineNumberTable extends Attribute {
    private int line_number_table_length;
    private LineNumber[] line_number_table;

    public LineNumberTable(LineNumberTable c2) {
        this(c2.getNameIndex(), c2.getLength(), c2.getLineNumberTable(), c2.getConstantPool());
    }

    public LineNumberTable(int name_index, int length, LineNumber[] line_number_table, ConstantPool constant_pool) {
        super((byte) 4, name_index, length, constant_pool);
        setLineNumberTable(line_number_table);
    }

    LineNumberTable(int name_index, int length, DataInputStream file, ConstantPool constant_pool) throws IOException {
        this(name_index, length, (LineNumber[]) null, constant_pool);
        this.line_number_table_length = file.readUnsignedShort();
        this.line_number_table = new LineNumber[this.line_number_table_length];
        for (int i2 = 0; i2 < this.line_number_table_length; i2++) {
            this.line_number_table[i2] = new LineNumber(file);
        }
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute, com.sun.org.apache.bcel.internal.classfile.Node
    public void accept(Visitor v2) {
        v2.visitLineNumberTable(this);
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public final void dump(DataOutputStream file) throws IOException {
        super.dump(file);
        file.writeShort(this.line_number_table_length);
        for (int i2 = 0; i2 < this.line_number_table_length; i2++) {
            this.line_number_table[i2].dump(file);
        }
    }

    public final LineNumber[] getLineNumberTable() {
        return this.line_number_table;
    }

    public final void setLineNumberTable(LineNumber[] line_number_table) {
        this.line_number_table = line_number_table;
        this.line_number_table_length = line_number_table == null ? 0 : line_number_table.length;
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public final String toString() {
        StringBuffer buf = new StringBuffer();
        StringBuffer line = new StringBuffer();
        for (int i2 = 0; i2 < this.line_number_table_length; i2++) {
            line.append(this.line_number_table[i2].toString());
            if (i2 < this.line_number_table_length - 1) {
                line.append(", ");
            }
            if (line.length() > 72) {
                line.append('\n');
                buf.append(line);
                line.setLength(0);
            }
        }
        buf.append(line);
        return buf.toString();
    }

    public int getSourceLine(int pos) {
        int l2 = 0;
        int r2 = this.line_number_table_length - 1;
        if (r2 < 0) {
            return -1;
        }
        int min_index = -1;
        int min = -1;
        do {
            int i2 = (l2 + r2) / 2;
            int j2 = this.line_number_table[i2].getStartPC();
            if (j2 == pos) {
                return this.line_number_table[i2].getLineNumber();
            }
            if (pos < j2) {
                r2 = i2 - 1;
            } else {
                l2 = i2 + 1;
            }
            if (j2 < pos && j2 > min) {
                min = j2;
                min_index = i2;
            }
        } while (l2 <= r2);
        if (min_index < 0) {
            return -1;
        }
        return this.line_number_table[min_index].getLineNumber();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public Attribute copy(ConstantPool constant_pool) {
        LineNumberTable c2 = (LineNumberTable) clone();
        c2.line_number_table = new LineNumber[this.line_number_table_length];
        for (int i2 = 0; i2 < this.line_number_table_length; i2++) {
            c2.line_number_table[i2] = this.line_number_table[i2].copy();
        }
        c2.constant_pool = constant_pool;
        return c2;
    }

    public final int getTableLength() {
        return this.line_number_table_length;
    }
}
