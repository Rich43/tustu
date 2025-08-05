package com.sun.org.apache.bcel.internal.classfile;

import com.sun.org.apache.bcel.internal.generic.Type;
import java.io.DataInputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/Field.class */
public final class Field extends FieldOrMethod {
    public Field(Field c2) {
        super(c2);
    }

    Field(DataInputStream file, ConstantPool constant_pool) throws IOException, ClassFormatException {
        super(file, constant_pool);
    }

    public Field(int access_flags, int name_index, int signature_index, Attribute[] attributes, ConstantPool constant_pool) {
        super(access_flags, name_index, signature_index, attributes, constant_pool);
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Node
    public void accept(Visitor v2) {
        v2.visitField(this);
    }

    public final ConstantValue getConstantValue() {
        for (int i2 = 0; i2 < this.attributes_count; i2++) {
            if (this.attributes[i2].getTag() == 1) {
                return (ConstantValue) this.attributes[i2];
            }
        }
        return null;
    }

    public final String toString() {
        String access = Utility.accessToString(this.access_flags);
        String access2 = access.equals("") ? "" : access + " ";
        String signature = Utility.signatureToString(getSignature());
        String name = getName();
        StringBuffer buf = new StringBuffer(access2 + signature + " " + name);
        ConstantValue cv = getConstantValue();
        if (cv != null) {
            buf.append(" = " + ((Object) cv));
        }
        for (int i2 = 0; i2 < this.attributes_count; i2++) {
            Attribute a2 = this.attributes[i2];
            if (!(a2 instanceof ConstantValue)) {
                buf.append(" [" + a2.toString() + "]");
            }
        }
        return buf.toString();
    }

    public final Field copy(ConstantPool constant_pool) {
        return (Field) copy_(constant_pool);
    }

    public Type getType() {
        return Type.getReturnType(getSignature());
    }
}
