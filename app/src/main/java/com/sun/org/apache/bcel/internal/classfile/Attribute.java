package com.sun.org.apache.bcel.internal.classfile;

import com.sun.org.apache.bcel.internal.Constants;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/Attribute.class */
public abstract class Attribute implements Cloneable, Node, Serializable {
    protected int name_index;
    protected int length;
    protected byte tag;
    protected ConstantPool constant_pool;
    private static HashMap readers = new HashMap();

    @Override // com.sun.org.apache.bcel.internal.classfile.Node
    public abstract void accept(Visitor visitor);

    public abstract Attribute copy(ConstantPool constantPool);

    protected Attribute(byte tag, int name_index, int length, ConstantPool constant_pool) {
        this.tag = tag;
        this.name_index = name_index;
        this.length = length;
        this.constant_pool = constant_pool;
    }

    public void dump(DataOutputStream file) throws IOException {
        file.writeShort(this.name_index);
        file.writeInt(this.length);
    }

    public static void addAttributeReader(String name, AttributeReader r2) {
        readers.put(name, r2);
    }

    public static void removeAttributeReader(String name) {
        readers.remove(name);
    }

    public static final Attribute readAttribute(DataInputStream file, ConstantPool constant_pool) throws IOException, ClassFormatException {
        byte tag = -1;
        int name_index = file.readUnsignedShort();
        ConstantUtf8 c2 = (ConstantUtf8) constant_pool.getConstant(name_index, (byte) 1);
        String name = c2.getBytes();
        int length = file.readInt();
        byte b2 = 0;
        while (true) {
            byte i2 = b2;
            if (i2 >= 13) {
                break;
            }
            if (!name.equals(Constants.ATTRIBUTE_NAMES[i2])) {
                b2 = (byte) (i2 + 1);
            } else {
                tag = i2;
                break;
            }
        }
        switch (tag) {
            case -1:
                AttributeReader r2 = (AttributeReader) readers.get(name);
                if (r2 != null) {
                    return r2.createAttribute(name_index, length, file, constant_pool);
                }
                return new Unknown(name_index, length, file, constant_pool);
            case 0:
                return new SourceFile(name_index, length, file, constant_pool);
            case 1:
                return new ConstantValue(name_index, length, file, constant_pool);
            case 2:
                return new Code(name_index, length, file, constant_pool);
            case 3:
                return new ExceptionTable(name_index, length, file, constant_pool);
            case 4:
                return new LineNumberTable(name_index, length, file, constant_pool);
            case 5:
                return new LocalVariableTable(name_index, length, file, constant_pool);
            case 6:
                return new InnerClasses(name_index, length, file, constant_pool);
            case 7:
                return new Synthetic(name_index, length, file, constant_pool);
            case 8:
                return new Deprecated(name_index, length, file, constant_pool);
            case 9:
                return new PMGClass(name_index, length, file, constant_pool);
            case 10:
                return new Signature(name_index, length, file, constant_pool);
            case 11:
                return new StackMap(name_index, length, file, constant_pool);
            case 12:
                return new LocalVariableTypeTable(name_index, length, file, constant_pool);
            default:
                throw new IllegalStateException("Ooops! default case reached.");
        }
    }

    public final int getLength() {
        return this.length;
    }

    public final void setLength(int length) {
        this.length = length;
    }

    public final void setNameIndex(int name_index) {
        this.name_index = name_index;
    }

    public final int getNameIndex() {
        return this.name_index;
    }

    public final byte getTag() {
        return this.tag;
    }

    public final ConstantPool getConstantPool() {
        return this.constant_pool;
    }

    public final void setConstantPool(ConstantPool constant_pool) {
        this.constant_pool = constant_pool;
    }

    public Object clone() {
        Object o2 = null;
        try {
            o2 = super.clone();
        } catch (CloneNotSupportedException e2) {
            e2.printStackTrace();
        }
        return o2;
    }

    public String toString() {
        return Constants.ATTRIBUTE_NAMES[this.tag];
    }
}
