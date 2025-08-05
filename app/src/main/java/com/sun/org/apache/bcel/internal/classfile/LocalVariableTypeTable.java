package com.sun.org.apache.bcel.internal.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/LocalVariableTypeTable.class */
public class LocalVariableTypeTable extends Attribute {
    private static final long serialVersionUID = -1082157891095177114L;
    private int local_variable_type_table_length;
    private LocalVariable[] local_variable_type_table;

    public LocalVariableTypeTable(LocalVariableTypeTable c2) {
        this(c2.getNameIndex(), c2.getLength(), c2.getLocalVariableTypeTable(), c2.getConstantPool());
    }

    public LocalVariableTypeTable(int name_index, int length, LocalVariable[] local_variable_table, ConstantPool constant_pool) {
        super((byte) 12, name_index, length, constant_pool);
        setLocalVariableTable(local_variable_table);
    }

    LocalVariableTypeTable(int nameIdx, int len, DataInputStream dis, ConstantPool cpool) throws IOException {
        this(nameIdx, len, (LocalVariable[]) null, cpool);
        this.local_variable_type_table_length = dis.readUnsignedShort();
        this.local_variable_type_table = new LocalVariable[this.local_variable_type_table_length];
        for (int i2 = 0; i2 < this.local_variable_type_table_length; i2++) {
            this.local_variable_type_table[i2] = new LocalVariable(dis, cpool);
        }
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute, com.sun.org.apache.bcel.internal.classfile.Node
    public void accept(Visitor v2) {
        v2.visitLocalVariableTypeTable(this);
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public final void dump(DataOutputStream file) throws IOException {
        super.dump(file);
        file.writeShort(this.local_variable_type_table_length);
        for (int i2 = 0; i2 < this.local_variable_type_table_length; i2++) {
            this.local_variable_type_table[i2].dump(file);
        }
    }

    public final LocalVariable[] getLocalVariableTypeTable() {
        return this.local_variable_type_table;
    }

    public final LocalVariable getLocalVariable(int index) {
        for (int i2 = 0; i2 < this.local_variable_type_table_length; i2++) {
            if (this.local_variable_type_table[i2].getIndex() == index) {
                return this.local_variable_type_table[i2];
            }
        }
        return null;
    }

    public final void setLocalVariableTable(LocalVariable[] local_variable_table) {
        this.local_variable_type_table = local_variable_table;
        this.local_variable_type_table_length = local_variable_table == null ? 0 : local_variable_table.length;
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public final String toString() {
        StringBuilder buf = new StringBuilder();
        for (int i2 = 0; i2 < this.local_variable_type_table_length; i2++) {
            buf.append(this.local_variable_type_table[i2].toString());
            if (i2 < this.local_variable_type_table_length - 1) {
                buf.append('\n');
            }
        }
        return buf.toString();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public Attribute copy(ConstantPool constant_pool) {
        LocalVariableTypeTable c2 = (LocalVariableTypeTable) clone();
        c2.local_variable_type_table = new LocalVariable[this.local_variable_type_table_length];
        for (int i2 = 0; i2 < this.local_variable_type_table_length; i2++) {
            c2.local_variable_type_table[i2] = this.local_variable_type_table[i2].copy();
        }
        c2.constant_pool = constant_pool;
        return c2;
    }

    public final int getTableLength() {
        return this.local_variable_type_table_length;
    }
}
