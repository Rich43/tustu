package com.sun.org.apache.bcel.internal.classfile;

import com.sun.org.apache.bcel.internal.generic.Type;
import java.io.DataInputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/Method.class */
public final class Method extends FieldOrMethod {
    public Method() {
    }

    public Method(Method c2) {
        super(c2);
    }

    Method(DataInputStream file, ConstantPool constant_pool) throws IOException, ClassFormatException {
        super(file, constant_pool);
    }

    public Method(int access_flags, int name_index, int signature_index, Attribute[] attributes, ConstantPool constant_pool) {
        super(access_flags, name_index, signature_index, attributes, constant_pool);
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Node
    public void accept(Visitor v2) {
        v2.visitMethod(this);
    }

    public final Code getCode() {
        for (int i2 = 0; i2 < this.attributes_count; i2++) {
            if (this.attributes[i2] instanceof Code) {
                return (Code) this.attributes[i2];
            }
        }
        return null;
    }

    public final ExceptionTable getExceptionTable() {
        for (int i2 = 0; i2 < this.attributes_count; i2++) {
            if (this.attributes[i2] instanceof ExceptionTable) {
                return (ExceptionTable) this.attributes[i2];
            }
        }
        return null;
    }

    public final LocalVariableTable getLocalVariableTable() {
        Code code = getCode();
        if (code != null) {
            return code.getLocalVariableTable();
        }
        return null;
    }

    public final LineNumberTable getLineNumberTable() {
        Code code = getCode();
        if (code != null) {
            return code.getLineNumberTable();
        }
        return null;
    }

    public final String toString() {
        String access = Utility.accessToString(this.access_flags);
        ConstantUtf8 c2 = (ConstantUtf8) this.constant_pool.getConstant(this.signature_index, (byte) 1);
        String signature = c2.getBytes();
        ConstantUtf8 c3 = (ConstantUtf8) this.constant_pool.getConstant(this.name_index, (byte) 1);
        String name = c3.getBytes();
        StringBuffer buf = new StringBuffer(Utility.methodSignatureToString(signature, name, access, true, getLocalVariableTable()));
        for (int i2 = 0; i2 < this.attributes_count; i2++) {
            Attribute a2 = this.attributes[i2];
            if (!(a2 instanceof Code) && !(a2 instanceof ExceptionTable)) {
                buf.append(" [" + a2.toString() + "]");
            }
        }
        ExceptionTable e2 = getExceptionTable();
        if (e2 != null) {
            String str = e2.toString();
            if (!str.equals("")) {
                buf.append("\n\t\tthrows " + str);
            }
        }
        return buf.toString();
    }

    public final Method copy(ConstantPool constant_pool) {
        return (Method) copy_(constant_pool);
    }

    public Type getReturnType() {
        return Type.getReturnType(getSignature());
    }

    public Type[] getArgumentTypes() {
        return Type.getArgumentTypes(getSignature());
    }
}
