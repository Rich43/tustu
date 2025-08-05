package com.sun.org.apache.bcel.internal.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/Code.class */
public final class Code extends Attribute {
    private int max_stack;
    private int max_locals;
    private int code_length;
    private byte[] code;
    private int exception_table_length;
    private CodeException[] exception_table;
    private int attributes_count;
    private Attribute[] attributes;

    public Code(Code c2) {
        this(c2.getNameIndex(), c2.getLength(), c2.getMaxStack(), c2.getMaxLocals(), c2.getCode(), c2.getExceptionTable(), c2.getAttributes(), c2.getConstantPool());
    }

    Code(int name_index, int length, DataInputStream file, ConstantPool constant_pool) throws IOException {
        this(name_index, length, file.readUnsignedShort(), file.readUnsignedShort(), (byte[]) null, (CodeException[]) null, (Attribute[]) null, constant_pool);
        this.code_length = file.readInt();
        this.code = new byte[this.code_length];
        file.readFully(this.code);
        this.exception_table_length = file.readUnsignedShort();
        this.exception_table = new CodeException[this.exception_table_length];
        for (int i2 = 0; i2 < this.exception_table_length; i2++) {
            this.exception_table[i2] = new CodeException(file);
        }
        this.attributes_count = file.readUnsignedShort();
        this.attributes = new Attribute[this.attributes_count];
        for (int i3 = 0; i3 < this.attributes_count; i3++) {
            this.attributes[i3] = Attribute.readAttribute(file, constant_pool);
        }
        this.length = length;
    }

    public Code(int name_index, int length, int max_stack, int max_locals, byte[] code, CodeException[] exception_table, Attribute[] attributes, ConstantPool constant_pool) {
        super((byte) 2, name_index, length, constant_pool);
        this.max_stack = max_stack;
        this.max_locals = max_locals;
        setCode(code);
        setExceptionTable(exception_table);
        setAttributes(attributes);
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute, com.sun.org.apache.bcel.internal.classfile.Node
    public void accept(Visitor v2) {
        v2.visitCode(this);
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public final void dump(DataOutputStream file) throws IOException {
        super.dump(file);
        file.writeShort(this.max_stack);
        file.writeShort(this.max_locals);
        file.writeInt(this.code_length);
        file.write(this.code, 0, this.code_length);
        file.writeShort(this.exception_table_length);
        for (int i2 = 0; i2 < this.exception_table_length; i2++) {
            this.exception_table[i2].dump(file);
        }
        file.writeShort(this.attributes_count);
        for (int i3 = 0; i3 < this.attributes_count; i3++) {
            this.attributes[i3].dump(file);
        }
    }

    public final Attribute[] getAttributes() {
        return this.attributes;
    }

    public LineNumberTable getLineNumberTable() {
        for (int i2 = 0; i2 < this.attributes_count; i2++) {
            if (this.attributes[i2] instanceof LineNumberTable) {
                return (LineNumberTable) this.attributes[i2];
            }
        }
        return null;
    }

    public LocalVariableTable getLocalVariableTable() {
        for (int i2 = 0; i2 < this.attributes_count; i2++) {
            if (this.attributes[i2] instanceof LocalVariableTable) {
                return (LocalVariableTable) this.attributes[i2];
            }
        }
        return null;
    }

    public final byte[] getCode() {
        return this.code;
    }

    public final CodeException[] getExceptionTable() {
        return this.exception_table;
    }

    public final int getMaxLocals() {
        return this.max_locals;
    }

    public final int getMaxStack() {
        return this.max_stack;
    }

    private final int getInternalLength() {
        return 8 + this.code_length + 2 + (8 * this.exception_table_length) + 2;
    }

    private final int calculateLength() {
        int len = 0;
        for (int i2 = 0; i2 < this.attributes_count; i2++) {
            len += this.attributes[i2].length + 6;
        }
        return len + getInternalLength();
    }

    public final void setAttributes(Attribute[] attributes) {
        this.attributes = attributes;
        this.attributes_count = attributes == null ? 0 : attributes.length;
        this.length = calculateLength();
    }

    public final void setCode(byte[] code) {
        this.code = code;
        this.code_length = code == null ? 0 : code.length;
    }

    public final void setExceptionTable(CodeException[] exception_table) {
        this.exception_table = exception_table;
        this.exception_table_length = exception_table == null ? 0 : exception_table.length;
    }

    public final void setMaxLocals(int max_locals) {
        this.max_locals = max_locals;
    }

    public final void setMaxStack(int max_stack) {
        this.max_stack = max_stack;
    }

    public final String toString(boolean verbose) {
        StringBuffer buf = new StringBuffer("Code(max_stack = " + this.max_stack + ", max_locals = " + this.max_locals + ", code_length = " + this.code_length + ")\n" + Utility.codeToString(this.code, this.constant_pool, 0, -1, verbose));
        if (this.exception_table_length > 0) {
            buf.append("\nException handler(s) = \nFrom\tTo\tHandler\tType\n");
            for (int i2 = 0; i2 < this.exception_table_length; i2++) {
                buf.append(this.exception_table[i2].toString(this.constant_pool, verbose) + "\n");
            }
        }
        if (this.attributes_count > 0) {
            buf.append("\nAttribute(s) = \n");
            for (int i3 = 0; i3 < this.attributes_count; i3++) {
                buf.append(this.attributes[i3].toString() + "\n");
            }
        }
        return buf.toString();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public final String toString() {
        return toString(true);
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public Attribute copy(ConstantPool constant_pool) {
        Code c2 = (Code) clone();
        c2.code = (byte[]) this.code.clone();
        c2.constant_pool = constant_pool;
        c2.exception_table = new CodeException[this.exception_table_length];
        for (int i2 = 0; i2 < this.exception_table_length; i2++) {
            c2.exception_table[i2] = this.exception_table[i2].copy();
        }
        c2.attributes = new Attribute[this.attributes_count];
        for (int i3 = 0; i3 < this.attributes_count; i3++) {
            c2.attributes[i3] = this.attributes[i3].copy(constant_pool);
        }
        return c2;
    }
}
