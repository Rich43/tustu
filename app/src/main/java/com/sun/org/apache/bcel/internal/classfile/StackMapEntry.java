package com.sun.org.apache.bcel.internal.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/StackMapEntry.class */
public final class StackMapEntry implements Cloneable {
    private int byte_code_offset;
    private int number_of_locals;
    private StackMapType[] types_of_locals;
    private int number_of_stack_items;
    private StackMapType[] types_of_stack_items;
    private ConstantPool constant_pool;

    StackMapEntry(DataInputStream file, ConstantPool constant_pool) throws IOException {
        this(file.readShort(), file.readShort(), null, -1, null, constant_pool);
        this.types_of_locals = new StackMapType[this.number_of_locals];
        for (int i2 = 0; i2 < this.number_of_locals; i2++) {
            this.types_of_locals[i2] = new StackMapType(file, constant_pool);
        }
        this.number_of_stack_items = file.readShort();
        this.types_of_stack_items = new StackMapType[this.number_of_stack_items];
        for (int i3 = 0; i3 < this.number_of_stack_items; i3++) {
            this.types_of_stack_items[i3] = new StackMapType(file, constant_pool);
        }
    }

    public StackMapEntry(int byte_code_offset, int number_of_locals, StackMapType[] types_of_locals, int number_of_stack_items, StackMapType[] types_of_stack_items, ConstantPool constant_pool) {
        this.byte_code_offset = byte_code_offset;
        this.number_of_locals = number_of_locals;
        this.types_of_locals = types_of_locals;
        this.number_of_stack_items = number_of_stack_items;
        this.types_of_stack_items = types_of_stack_items;
        this.constant_pool = constant_pool;
    }

    public final void dump(DataOutputStream file) throws IOException {
        file.writeShort(this.byte_code_offset);
        file.writeShort(this.number_of_locals);
        for (int i2 = 0; i2 < this.number_of_locals; i2++) {
            this.types_of_locals[i2].dump(file);
        }
        file.writeShort(this.number_of_stack_items);
        for (int i3 = 0; i3 < this.number_of_stack_items; i3++) {
            this.types_of_stack_items[i3].dump(file);
        }
    }

    public final String toString() {
        StringBuffer buf = new StringBuffer("(offset=" + this.byte_code_offset);
        if (this.number_of_locals > 0) {
            buf.append(", locals={");
            for (int i2 = 0; i2 < this.number_of_locals; i2++) {
                buf.append((Object) this.types_of_locals[i2]);
                if (i2 < this.number_of_locals - 1) {
                    buf.append(", ");
                }
            }
            buf.append("}");
        }
        if (this.number_of_stack_items > 0) {
            buf.append(", stack items={");
            for (int i3 = 0; i3 < this.number_of_stack_items; i3++) {
                buf.append((Object) this.types_of_stack_items[i3]);
                if (i3 < this.number_of_stack_items - 1) {
                    buf.append(", ");
                }
            }
            buf.append("}");
        }
        buf.append(")");
        return buf.toString();
    }

    public void setByteCodeOffset(int b2) {
        this.byte_code_offset = b2;
    }

    public int getByteCodeOffset() {
        return this.byte_code_offset;
    }

    public void setNumberOfLocals(int n2) {
        this.number_of_locals = n2;
    }

    public int getNumberOfLocals() {
        return this.number_of_locals;
    }

    public void setTypesOfLocals(StackMapType[] t2) {
        this.types_of_locals = t2;
    }

    public StackMapType[] getTypesOfLocals() {
        return this.types_of_locals;
    }

    public void setNumberOfStackItems(int n2) {
        this.number_of_stack_items = n2;
    }

    public int getNumberOfStackItems() {
        return this.number_of_stack_items;
    }

    public void setTypesOfStackItems(StackMapType[] t2) {
        this.types_of_stack_items = t2;
    }

    public StackMapType[] getTypesOfStackItems() {
        return this.types_of_stack_items;
    }

    public StackMapEntry copy() {
        try {
            return (StackMapEntry) clone();
        } catch (CloneNotSupportedException e2) {
            return null;
        }
    }

    public void accept(Visitor v2) {
        v2.visitStackMapEntry(this);
    }

    public final ConstantPool getConstantPool() {
        return this.constant_pool;
    }

    public final void setConstantPool(ConstantPool constant_pool) {
        this.constant_pool = constant_pool;
    }
}
