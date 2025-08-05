package com.sun.org.apache.bcel.internal.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/StackMap.class */
public final class StackMap extends Attribute implements Node {
    private int map_length;
    private StackMapEntry[] map;

    public StackMap(int name_index, int length, StackMapEntry[] map, ConstantPool constant_pool) {
        super((byte) 11, name_index, length, constant_pool);
        setStackMap(map);
    }

    StackMap(int name_index, int length, DataInputStream file, ConstantPool constant_pool) throws IOException {
        this(name_index, length, (StackMapEntry[]) null, constant_pool);
        this.map_length = file.readUnsignedShort();
        this.map = new StackMapEntry[this.map_length];
        for (int i2 = 0; i2 < this.map_length; i2++) {
            this.map[i2] = new StackMapEntry(file, constant_pool);
        }
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public final void dump(DataOutputStream file) throws IOException {
        super.dump(file);
        file.writeShort(this.map_length);
        for (int i2 = 0; i2 < this.map_length; i2++) {
            this.map[i2].dump(file);
        }
    }

    public final StackMapEntry[] getStackMap() {
        return this.map;
    }

    public final void setStackMap(StackMapEntry[] map) {
        this.map = map;
        this.map_length = map == null ? 0 : map.length;
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public final String toString() {
        StringBuffer buf = new StringBuffer("StackMap(");
        for (int i2 = 0; i2 < this.map_length; i2++) {
            buf.append(this.map[i2].toString());
            if (i2 < this.map_length - 1) {
                buf.append(", ");
            }
        }
        buf.append(')');
        return buf.toString();
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public Attribute copy(ConstantPool constant_pool) {
        StackMap c2 = (StackMap) clone();
        c2.map = new StackMapEntry[this.map_length];
        for (int i2 = 0; i2 < this.map_length; i2++) {
            c2.map[i2] = this.map[i2].copy();
        }
        c2.constant_pool = constant_pool;
        return c2;
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute, com.sun.org.apache.bcel.internal.classfile.Node
    public void accept(Visitor v2) {
        v2.visitStackMap(this);
    }

    public final int getMapLength() {
        return this.map_length;
    }
}
