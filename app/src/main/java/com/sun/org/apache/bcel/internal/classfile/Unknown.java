package com.sun.org.apache.bcel.internal.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/Unknown.class */
public final class Unknown extends Attribute {
    private byte[] bytes;
    private String name;
    private static HashMap unknown_attributes = new HashMap();

    static Unknown[] getUnknownAttributes() {
        Unknown[] unknowns = new Unknown[unknown_attributes.size()];
        Iterator entries = unknown_attributes.values().iterator();
        int i2 = 0;
        while (entries.hasNext()) {
            unknowns[i2] = (Unknown) entries.next();
            i2++;
        }
        unknown_attributes.clear();
        return unknowns;
    }

    public Unknown(Unknown c2) {
        this(c2.getNameIndex(), c2.getLength(), c2.getBytes(), c2.getConstantPool());
    }

    public Unknown(int name_index, int length, byte[] bytes, ConstantPool constant_pool) {
        super((byte) -1, name_index, length, constant_pool);
        this.bytes = bytes;
        this.name = ((ConstantUtf8) constant_pool.getConstant(name_index, (byte) 1)).getBytes();
        unknown_attributes.put(this.name, this);
    }

    Unknown(int name_index, int length, DataInputStream file, ConstantPool constant_pool) throws IOException {
        this(name_index, length, (byte[]) null, constant_pool);
        if (length > 0) {
            this.bytes = new byte[length];
            file.readFully(this.bytes);
        }
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute, com.sun.org.apache.bcel.internal.classfile.Node
    public void accept(Visitor v2) {
        v2.visitUnknown(this);
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public final void dump(DataOutputStream file) throws IOException {
        super.dump(file);
        if (this.length > 0) {
            file.write(this.bytes, 0, this.length);
        }
    }

    public final byte[] getBytes() {
        return this.bytes;
    }

    public final String getName() {
        return this.name;
    }

    public final void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public final String toString() {
        String hex;
        if (this.length == 0 || this.bytes == null) {
            return "(Unknown attribute " + this.name + ")";
        }
        if (this.length > 10) {
            byte[] tmp = new byte[10];
            System.arraycopy(this.bytes, 0, tmp, 0, 10);
            hex = Utility.toHexString(tmp) + "... (truncated)";
        } else {
            hex = Utility.toHexString(this.bytes);
        }
        return "(Unknown attribute " + this.name + ": " + hex + ")";
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public Attribute copy(ConstantPool constant_pool) {
        Unknown c2 = (Unknown) clone();
        if (this.bytes != null) {
            c2.bytes = (byte[]) this.bytes.clone();
        }
        c2.constant_pool = constant_pool;
        return c2;
    }
}
