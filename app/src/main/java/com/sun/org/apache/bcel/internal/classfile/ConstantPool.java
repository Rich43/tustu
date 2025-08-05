package com.sun.org.apache.bcel.internal.classfile;

import com.sun.org.apache.bcel.internal.Constants;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/ConstantPool.class */
public class ConstantPool implements Cloneable, Node, Serializable {
    private int constant_pool_count;
    private Constant[] constant_pool;

    public ConstantPool(Constant[] constant_pool) {
        setConstantPool(constant_pool);
    }

    ConstantPool(DataInputStream file) throws IOException, ClassFormatException {
        this.constant_pool_count = file.readUnsignedShort();
        this.constant_pool = new Constant[this.constant_pool_count];
        int i2 = 1;
        while (i2 < this.constant_pool_count) {
            this.constant_pool[i2] = Constant.readConstant(file);
            byte tag = this.constant_pool[i2].getTag();
            if (tag == 6 || tag == 5) {
                i2++;
            }
            i2++;
        }
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Node
    public void accept(Visitor v2) {
        v2.visitConstantPool(this);
    }

    public String constantToString(Constant c2) throws ClassFormatException {
        String str;
        byte tag = c2.getTag();
        switch (tag) {
            case 1:
                str = ((ConstantUtf8) c2).getBytes();
                break;
            case 2:
            default:
                throw new RuntimeException("Unknown constant type " + ((int) tag));
            case 3:
                str = "" + ((ConstantInteger) c2).getBytes();
                break;
            case 4:
                str = "" + ((ConstantFloat) c2).getBytes();
                break;
            case 5:
                str = "" + ((ConstantLong) c2).getBytes();
                break;
            case 6:
                str = "" + ((ConstantDouble) c2).getBytes();
                break;
            case 7:
                int i2 = ((ConstantClass) c2).getNameIndex();
                str = Utility.compactClassName(((ConstantUtf8) getConstant(i2, (byte) 1)).getBytes(), false);
                break;
            case 8:
                int i3 = ((ConstantString) c2).getStringIndex();
                str = PdfOps.DOUBLE_QUOTE__TOKEN + escape(((ConstantUtf8) getConstant(i3, (byte) 1)).getBytes()) + PdfOps.DOUBLE_QUOTE__TOKEN;
                break;
            case 9:
            case 10:
            case 11:
                str = constantToString(((ConstantCP) c2).getClassIndex(), (byte) 7) + "." + constantToString(((ConstantCP) c2).getNameAndTypeIndex(), (byte) 12);
                break;
            case 12:
                str = constantToString(((ConstantNameAndType) c2).getNameIndex(), (byte) 1) + " " + constantToString(((ConstantNameAndType) c2).getSignatureIndex(), (byte) 1);
                break;
        }
        return str;
    }

    private static final String escape(String str) {
        int len = str.length();
        StringBuffer buf = new StringBuffer(len + 5);
        char[] ch = str.toCharArray();
        for (int i2 = 0; i2 < len; i2++) {
            switch (ch[i2]) {
                case '\b':
                    buf.append("\\b");
                    break;
                case '\t':
                    buf.append("\\t");
                    break;
                case '\n':
                    buf.append("\\n");
                    break;
                case '\r':
                    buf.append("\\r");
                    break;
                case '\"':
                    buf.append("\\\"");
                    break;
                default:
                    buf.append(ch[i2]);
                    break;
            }
        }
        return buf.toString();
    }

    public String constantToString(int index, byte tag) throws ClassFormatException {
        Constant c2 = getConstant(index, tag);
        return constantToString(c2);
    }

    public void dump(DataOutputStream file) throws IOException {
        int size = this.constant_pool_count < 65535 ? this.constant_pool_count : 65535;
        file.writeShort(size);
        for (int i2 = 1; i2 < size; i2++) {
            if (this.constant_pool[i2] != null) {
                this.constant_pool[i2].dump(file);
            }
        }
    }

    public Constant getConstant(int index) {
        if (index >= this.constant_pool.length || index < 0) {
            throw new ClassFormatException("Invalid constant pool reference: " + index + ". Constant pool size is: " + this.constant_pool.length);
        }
        return this.constant_pool[index];
    }

    public Constant getConstant(int index, byte tag) throws ClassFormatException {
        Constant c2 = getConstant(index);
        if (c2 == null) {
            throw new ClassFormatException("Constant pool at index " + index + " is null.");
        }
        if (c2.getTag() == tag) {
            return c2;
        }
        throw new ClassFormatException("Expected class `" + Constants.CONSTANT_NAMES[tag] + "' at index " + index + " and got " + ((Object) c2));
    }

    public Constant[] getConstantPool() {
        return this.constant_pool;
    }

    public String getConstantString(int index, byte tag) throws ClassFormatException {
        int i2;
        Constant c2 = getConstant(index, tag);
        switch (tag) {
            case 7:
                i2 = ((ConstantClass) c2).getNameIndex();
                break;
            case 8:
                i2 = ((ConstantString) c2).getStringIndex();
                break;
            default:
                throw new RuntimeException("getConstantString called with illegal tag " + ((int) tag));
        }
        return ((ConstantUtf8) getConstant(i2, (byte) 1)).getBytes();
    }

    public int getLength() {
        return this.constant_pool_count;
    }

    public void setConstant(int index, Constant constant) {
        this.constant_pool[index] = constant;
    }

    public void setConstantPool(Constant[] constant_pool) {
        this.constant_pool = constant_pool;
        this.constant_pool_count = constant_pool == null ? 0 : constant_pool.length;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        for (int i2 = 1; i2 < this.constant_pool_count; i2++) {
            buf.append(i2 + ")" + ((Object) this.constant_pool[i2]) + "\n");
        }
        return buf.toString();
    }

    public ConstantPool copy() {
        ConstantPool c2 = null;
        try {
            c2 = (ConstantPool) clone();
        } catch (CloneNotSupportedException e2) {
        }
        c2.constant_pool = new Constant[this.constant_pool_count];
        for (int i2 = 1; i2 < this.constant_pool_count; i2++) {
            if (this.constant_pool[i2] != null) {
                c2.constant_pool[i2] = this.constant_pool[i2].copy();
            }
        }
        return c2;
    }
}
