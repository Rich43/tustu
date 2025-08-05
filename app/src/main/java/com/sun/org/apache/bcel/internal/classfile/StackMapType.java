package com.sun.org.apache.bcel.internal.classfile;

import com.sun.org.apache.bcel.internal.Constants;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/StackMapType.class */
public final class StackMapType implements Cloneable {
    private byte type;
    private int index;
    private ConstantPool constant_pool;

    StackMapType(DataInputStream file, ConstantPool constant_pool) throws IOException {
        this(file.readByte(), -1, constant_pool);
        if (hasIndex()) {
            setIndex(file.readShort());
        }
        setConstantPool(constant_pool);
    }

    public StackMapType(byte type, int index, ConstantPool constant_pool) {
        this.index = -1;
        setType(type);
        setIndex(index);
        setConstantPool(constant_pool);
    }

    public void setType(byte t2) {
        if (t2 < 0 || t2 > 8) {
            throw new RuntimeException("Illegal type for StackMapType: " + ((int) t2));
        }
        this.type = t2;
    }

    public byte getType() {
        return this.type;
    }

    public void setIndex(int t2) {
        this.index = t2;
    }

    public int getIndex() {
        return this.index;
    }

    public final void dump(DataOutputStream file) throws IOException {
        file.writeByte(this.type);
        if (hasIndex()) {
            file.writeShort(getIndex());
        }
    }

    public final boolean hasIndex() {
        return this.type == 7 || this.type == 8;
    }

    private String printIndex() {
        if (this.type == 7) {
            return ", class=" + this.constant_pool.constantToString(this.index, (byte) 7);
        }
        if (this.type == 8) {
            return ", offset=" + this.index;
        }
        return "";
    }

    public final String toString() {
        return "(type=" + Constants.ITEM_NAMES[this.type] + printIndex() + ")";
    }

    public StackMapType copy() {
        try {
            return (StackMapType) clone();
        } catch (CloneNotSupportedException e2) {
            return null;
        }
    }

    public final ConstantPool getConstantPool() {
        return this.constant_pool;
    }

    public final void setConstantPool(ConstantPool constant_pool) {
        this.constant_pool = constant_pool;
    }
}
