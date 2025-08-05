package com.sun.org.apache.bcel.internal.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/FieldOrMethod.class */
public abstract class FieldOrMethod extends AccessFlags implements Cloneable, Node {
    protected int name_index;
    protected int signature_index;
    protected int attributes_count;
    protected Attribute[] attributes;
    protected ConstantPool constant_pool;

    FieldOrMethod() {
    }

    protected FieldOrMethod(FieldOrMethod c2) {
        this(c2.getAccessFlags(), c2.getNameIndex(), c2.getSignatureIndex(), c2.getAttributes(), c2.getConstantPool());
    }

    protected FieldOrMethod(DataInputStream file, ConstantPool constant_pool) throws IOException, ClassFormatException {
        this(file.readUnsignedShort(), file.readUnsignedShort(), file.readUnsignedShort(), null, constant_pool);
        this.attributes_count = file.readUnsignedShort();
        this.attributes = new Attribute[this.attributes_count];
        for (int i2 = 0; i2 < this.attributes_count; i2++) {
            this.attributes[i2] = Attribute.readAttribute(file, constant_pool);
        }
    }

    protected FieldOrMethod(int access_flags, int name_index, int signature_index, Attribute[] attributes, ConstantPool constant_pool) {
        this.access_flags = access_flags;
        this.name_index = name_index;
        this.signature_index = signature_index;
        this.constant_pool = constant_pool;
        setAttributes(attributes);
    }

    public final void dump(DataOutputStream file) throws IOException {
        file.writeShort(this.access_flags);
        file.writeShort(this.name_index);
        file.writeShort(this.signature_index);
        file.writeShort(this.attributes_count);
        for (int i2 = 0; i2 < this.attributes_count; i2++) {
            this.attributes[i2].dump(file);
        }
    }

    public final Attribute[] getAttributes() {
        return this.attributes;
    }

    public final void setAttributes(Attribute[] attributes) {
        this.attributes = attributes;
        this.attributes_count = attributes == null ? 0 : attributes.length;
    }

    public final ConstantPool getConstantPool() {
        return this.constant_pool;
    }

    public final void setConstantPool(ConstantPool constant_pool) {
        this.constant_pool = constant_pool;
    }

    public final int getNameIndex() {
        return this.name_index;
    }

    public final void setNameIndex(int name_index) {
        this.name_index = name_index;
    }

    public final int getSignatureIndex() {
        return this.signature_index;
    }

    public final void setSignatureIndex(int signature_index) {
        this.signature_index = signature_index;
    }

    public final String getName() {
        ConstantUtf8 c2 = (ConstantUtf8) this.constant_pool.getConstant(this.name_index, (byte) 1);
        return c2.getBytes();
    }

    public final String getSignature() {
        ConstantUtf8 c2 = (ConstantUtf8) this.constant_pool.getConstant(this.signature_index, (byte) 1);
        return c2.getBytes();
    }

    protected FieldOrMethod copy_(ConstantPool constant_pool) {
        FieldOrMethod c2 = null;
        try {
            c2 = (FieldOrMethod) clone();
        } catch (CloneNotSupportedException e2) {
        }
        c2.constant_pool = constant_pool;
        c2.attributes = new Attribute[this.attributes_count];
        for (int i2 = 0; i2 < this.attributes_count; i2++) {
            c2.attributes[i2] = this.attributes[i2].copy(constant_pool);
        }
        return c2;
    }
}
