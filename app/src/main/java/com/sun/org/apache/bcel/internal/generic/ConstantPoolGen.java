package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.classfile.Constant;
import com.sun.org.apache.bcel.internal.classfile.ConstantCP;
import com.sun.org.apache.bcel.internal.classfile.ConstantClass;
import com.sun.org.apache.bcel.internal.classfile.ConstantDouble;
import com.sun.org.apache.bcel.internal.classfile.ConstantFieldref;
import com.sun.org.apache.bcel.internal.classfile.ConstantFloat;
import com.sun.org.apache.bcel.internal.classfile.ConstantInteger;
import com.sun.org.apache.bcel.internal.classfile.ConstantInterfaceMethodref;
import com.sun.org.apache.bcel.internal.classfile.ConstantLong;
import com.sun.org.apache.bcel.internal.classfile.ConstantMethodref;
import com.sun.org.apache.bcel.internal.classfile.ConstantNameAndType;
import com.sun.org.apache.bcel.internal.classfile.ConstantPool;
import com.sun.org.apache.bcel.internal.classfile.ConstantString;
import com.sun.org.apache.bcel.internal.classfile.ConstantUtf8;
import java.io.Serializable;
import java.util.HashMap;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/ConstantPoolGen.class */
public class ConstantPoolGen implements Serializable {
    public static final int CONSTANT_POOL_SIZE = 65536;
    protected int size;
    protected Constant[] constants;
    protected int index;
    private static final String METHODREF_DELIM = ":";
    private static final String IMETHODREF_DELIM = "#";
    private static final String FIELDREF_DELIM = "&";
    private static final String NAT_DELIM = "%";
    private HashMap string_table;
    private HashMap class_table;
    private HashMap utf8_table;
    private HashMap n_a_t_table;
    private HashMap cp_table;

    /* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/ConstantPoolGen$Index.class */
    private static class Index implements Serializable {
        int index;

        Index(int i2) {
            this.index = i2;
        }
    }

    public ConstantPoolGen(Constant[] cs) {
        this.size = 1024;
        this.constants = new Constant[this.size];
        this.index = 1;
        this.string_table = new HashMap();
        this.class_table = new HashMap();
        this.utf8_table = new HashMap();
        this.n_a_t_table = new HashMap();
        this.cp_table = new HashMap();
        if (cs.length > this.size) {
            this.size = Math.min(cs.length, 65536);
            this.constants = new Constant[this.size];
        }
        System.arraycopy(cs, 0, this.constants, 0, cs.length);
        if (cs.length > 0) {
            this.index = cs.length;
        }
        for (int i2 = 1; i2 < this.index; i2++) {
            Constant c2 = this.constants[i2];
            if (c2 instanceof ConstantString) {
                ConstantString s2 = (ConstantString) c2;
                ConstantUtf8 u8 = (ConstantUtf8) this.constants[s2.getStringIndex()];
                this.string_table.put(u8.getBytes(), new Index(i2));
            } else if (c2 instanceof ConstantClass) {
                ConstantClass s3 = (ConstantClass) c2;
                ConstantUtf8 u82 = (ConstantUtf8) this.constants[s3.getNameIndex()];
                this.class_table.put(u82.getBytes(), new Index(i2));
            } else if (c2 instanceof ConstantNameAndType) {
                ConstantNameAndType n2 = (ConstantNameAndType) c2;
                ConstantUtf8 u83 = (ConstantUtf8) this.constants[n2.getNameIndex()];
                ConstantUtf8 u8_2 = (ConstantUtf8) this.constants[n2.getSignatureIndex()];
                this.n_a_t_table.put(u83.getBytes() + "%" + u8_2.getBytes(), new Index(i2));
            } else if (c2 instanceof ConstantUtf8) {
                ConstantUtf8 u2 = (ConstantUtf8) c2;
                this.utf8_table.put(u2.getBytes(), new Index(i2));
            } else if (c2 instanceof ConstantCP) {
                ConstantCP m2 = (ConstantCP) c2;
                ConstantClass clazz = (ConstantClass) this.constants[m2.getClassIndex()];
                ConstantNameAndType n3 = (ConstantNameAndType) this.constants[m2.getNameAndTypeIndex()];
                ConstantUtf8 u84 = (ConstantUtf8) this.constants[clazz.getNameIndex()];
                String class_name = u84.getBytes().replace('/', '.');
                ConstantUtf8 u85 = (ConstantUtf8) this.constants[n3.getNameIndex()];
                String method_name = u85.getBytes();
                ConstantUtf8 u86 = (ConstantUtf8) this.constants[n3.getSignatureIndex()];
                String signature = u86.getBytes();
                String delim = ":";
                if (c2 instanceof ConstantInterfaceMethodref) {
                    delim = "#";
                } else if (c2 instanceof ConstantFieldref) {
                    delim = FIELDREF_DELIM;
                }
                this.cp_table.put(class_name + delim + method_name + delim + signature, new Index(i2));
            }
        }
    }

    public ConstantPoolGen(ConstantPool cp) {
        this(cp.getConstantPool());
    }

    public ConstantPoolGen() {
        this.size = 1024;
        this.constants = new Constant[this.size];
        this.index = 1;
        this.string_table = new HashMap();
        this.class_table = new HashMap();
        this.utf8_table = new HashMap();
        this.n_a_t_table = new HashMap();
        this.cp_table = new HashMap();
    }

    protected void adjustSize() {
        if (this.index + 3 >= 65536) {
            throw new RuntimeException("The number of constants " + (this.index + 3) + " is over the size of the constant pool: 65535");
        }
        if (this.index + 3 >= this.size) {
            Constant[] cs = this.constants;
            this.size *= 2;
            this.size = Math.min(this.size, 65536);
            this.constants = new Constant[this.size];
            System.arraycopy(cs, 0, this.constants, 0, this.index);
        }
    }

    public int lookupString(String str) {
        Index index = (Index) this.string_table.get(str);
        if (index != null) {
            return index.index;
        }
        return -1;
    }

    public int addString(String str) {
        int ret = lookupString(str);
        if (ret != -1) {
            return ret;
        }
        int utf8 = addUtf8(str);
        adjustSize();
        ConstantString s2 = new ConstantString(utf8);
        int ret2 = this.index;
        Constant[] constantArr = this.constants;
        int i2 = this.index;
        this.index = i2 + 1;
        constantArr[i2] = s2;
        this.string_table.put(str, new Index(ret2));
        return ret2;
    }

    public int lookupClass(String str) {
        Index index = (Index) this.class_table.get(str.replace('.', '/'));
        if (index != null) {
            return index.index;
        }
        return -1;
    }

    private int addClass_(String clazz) {
        int ret = lookupClass(clazz);
        if (ret != -1) {
            return ret;
        }
        adjustSize();
        ConstantClass c2 = new ConstantClass(addUtf8(clazz));
        int ret2 = this.index;
        Constant[] constantArr = this.constants;
        int i2 = this.index;
        this.index = i2 + 1;
        constantArr[i2] = c2;
        this.class_table.put(clazz, new Index(ret2));
        return ret2;
    }

    public int addClass(String str) {
        return addClass_(str.replace('.', '/'));
    }

    public int addClass(ObjectType type) {
        return addClass(type.getClassName());
    }

    public int addArrayClass(ArrayType type) {
        return addClass_(type.getSignature());
    }

    public int lookupInteger(int n2) {
        for (int i2 = 1; i2 < this.index; i2++) {
            if (this.constants[i2] instanceof ConstantInteger) {
                ConstantInteger c2 = (ConstantInteger) this.constants[i2];
                if (c2.getBytes() == n2) {
                    return i2;
                }
            }
        }
        return -1;
    }

    public int addInteger(int n2) {
        int ret = lookupInteger(n2);
        if (ret != -1) {
            return ret;
        }
        adjustSize();
        int ret2 = this.index;
        Constant[] constantArr = this.constants;
        int i2 = this.index;
        this.index = i2 + 1;
        constantArr[i2] = new ConstantInteger(n2);
        return ret2;
    }

    public int lookupFloat(float n2) {
        int bits = Float.floatToIntBits(n2);
        for (int i2 = 1; i2 < this.index; i2++) {
            if (this.constants[i2] instanceof ConstantFloat) {
                ConstantFloat c2 = (ConstantFloat) this.constants[i2];
                if (Float.floatToIntBits(c2.getBytes()) == bits) {
                    return i2;
                }
            }
        }
        return -1;
    }

    public int addFloat(float n2) {
        int ret = lookupFloat(n2);
        if (ret != -1) {
            return ret;
        }
        adjustSize();
        int ret2 = this.index;
        Constant[] constantArr = this.constants;
        int i2 = this.index;
        this.index = i2 + 1;
        constantArr[i2] = new ConstantFloat(n2);
        return ret2;
    }

    public int lookupUtf8(String n2) {
        Index index = (Index) this.utf8_table.get(n2);
        if (index != null) {
            return index.index;
        }
        return -1;
    }

    public int addUtf8(String n2) {
        int ret = lookupUtf8(n2);
        if (ret != -1) {
            return ret;
        }
        adjustSize();
        int ret2 = this.index;
        Constant[] constantArr = this.constants;
        int i2 = this.index;
        this.index = i2 + 1;
        constantArr[i2] = new ConstantUtf8(n2);
        this.utf8_table.put(n2, new Index(ret2));
        return ret2;
    }

    public int lookupLong(long n2) {
        for (int i2 = 1; i2 < this.index; i2++) {
            if (this.constants[i2] instanceof ConstantLong) {
                ConstantLong c2 = (ConstantLong) this.constants[i2];
                if (c2.getBytes() == n2) {
                    return i2;
                }
            }
        }
        return -1;
    }

    public int addLong(long n2) {
        int ret = lookupLong(n2);
        if (ret != -1) {
            return ret;
        }
        adjustSize();
        int ret2 = this.index;
        this.constants[this.index] = new ConstantLong(n2);
        this.index += 2;
        return ret2;
    }

    public int lookupDouble(double n2) {
        long bits = Double.doubleToLongBits(n2);
        for (int i2 = 1; i2 < this.index; i2++) {
            if (this.constants[i2] instanceof ConstantDouble) {
                ConstantDouble c2 = (ConstantDouble) this.constants[i2];
                if (Double.doubleToLongBits(c2.getBytes()) == bits) {
                    return i2;
                }
            }
        }
        return -1;
    }

    public int addDouble(double n2) {
        int ret = lookupDouble(n2);
        if (ret != -1) {
            return ret;
        }
        adjustSize();
        int ret2 = this.index;
        this.constants[this.index] = new ConstantDouble(n2);
        this.index += 2;
        return ret2;
    }

    public int lookupNameAndType(String name, String signature) {
        Index index = (Index) this.n_a_t_table.get(name + "%" + signature);
        if (index != null) {
            return index.index;
        }
        return -1;
    }

    public int addNameAndType(String name, String signature) {
        int ret = lookupNameAndType(name, signature);
        if (ret != -1) {
            return ret;
        }
        adjustSize();
        int name_index = addUtf8(name);
        int signature_index = addUtf8(signature);
        int ret2 = this.index;
        Constant[] constantArr = this.constants;
        int i2 = this.index;
        this.index = i2 + 1;
        constantArr[i2] = new ConstantNameAndType(name_index, signature_index);
        this.n_a_t_table.put(name + "%" + signature, new Index(ret2));
        return ret2;
    }

    public int lookupMethodref(String class_name, String method_name, String signature) {
        Index index = (Index) this.cp_table.get(class_name + ":" + method_name + ":" + signature);
        if (index != null) {
            return index.index;
        }
        return -1;
    }

    public int lookupMethodref(MethodGen method) {
        return lookupMethodref(method.getClassName(), method.getName(), method.getSignature());
    }

    public int addMethodref(String class_name, String method_name, String signature) {
        int ret = lookupMethodref(class_name, method_name, signature);
        if (ret != -1) {
            return ret;
        }
        adjustSize();
        int name_and_type_index = addNameAndType(method_name, signature);
        int class_index = addClass(class_name);
        int ret2 = this.index;
        Constant[] constantArr = this.constants;
        int i2 = this.index;
        this.index = i2 + 1;
        constantArr[i2] = new ConstantMethodref(class_index, name_and_type_index);
        this.cp_table.put(class_name + ":" + method_name + ":" + signature, new Index(ret2));
        return ret2;
    }

    public int addMethodref(MethodGen method) {
        return addMethodref(method.getClassName(), method.getName(), method.getSignature());
    }

    public int lookupInterfaceMethodref(String class_name, String method_name, String signature) {
        Index index = (Index) this.cp_table.get(class_name + "#" + method_name + "#" + signature);
        if (index != null) {
            return index.index;
        }
        return -1;
    }

    public int lookupInterfaceMethodref(MethodGen method) {
        return lookupInterfaceMethodref(method.getClassName(), method.getName(), method.getSignature());
    }

    public int addInterfaceMethodref(String class_name, String method_name, String signature) {
        int ret = lookupInterfaceMethodref(class_name, method_name, signature);
        if (ret != -1) {
            return ret;
        }
        adjustSize();
        int class_index = addClass(class_name);
        int name_and_type_index = addNameAndType(method_name, signature);
        int ret2 = this.index;
        Constant[] constantArr = this.constants;
        int i2 = this.index;
        this.index = i2 + 1;
        constantArr[i2] = new ConstantInterfaceMethodref(class_index, name_and_type_index);
        this.cp_table.put(class_name + "#" + method_name + "#" + signature, new Index(ret2));
        return ret2;
    }

    public int addInterfaceMethodref(MethodGen method) {
        return addInterfaceMethodref(method.getClassName(), method.getName(), method.getSignature());
    }

    public int lookupFieldref(String class_name, String field_name, String signature) {
        Index index = (Index) this.cp_table.get(class_name + FIELDREF_DELIM + field_name + FIELDREF_DELIM + signature);
        if (index != null) {
            return index.index;
        }
        return -1;
    }

    public int addFieldref(String class_name, String field_name, String signature) {
        int ret = lookupFieldref(class_name, field_name, signature);
        if (ret != -1) {
            return ret;
        }
        adjustSize();
        int class_index = addClass(class_name);
        int name_and_type_index = addNameAndType(field_name, signature);
        int ret2 = this.index;
        Constant[] constantArr = this.constants;
        int i2 = this.index;
        this.index = i2 + 1;
        constantArr[i2] = new ConstantFieldref(class_index, name_and_type_index);
        this.cp_table.put(class_name + FIELDREF_DELIM + field_name + FIELDREF_DELIM + signature, new Index(ret2));
        return ret2;
    }

    public Constant getConstant(int i2) {
        return this.constants[i2];
    }

    public void setConstant(int i2, Constant c2) {
        this.constants[i2] = c2;
    }

    public ConstantPool getConstantPool() {
        return new ConstantPool(this.constants);
    }

    public int getSize() {
        return this.index;
    }

    public ConstantPool getFinalConstantPool() {
        Constant[] cs = new Constant[this.index];
        System.arraycopy(this.constants, 0, cs, 0, this.index);
        return new ConstantPool(cs);
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        for (int i2 = 1; i2 < this.index; i2++) {
            buf.append(i2 + ")" + ((Object) this.constants[i2]) + "\n");
        }
        return buf.toString();
    }

    public int addConstant(Constant c2, ConstantPoolGen cp) {
        Constant[] constants = cp.getConstantPool().getConstantPool();
        switch (c2.getTag()) {
            case 1:
                return addUtf8(((ConstantUtf8) c2).getBytes());
            case 2:
            default:
                throw new RuntimeException("Unknown constant type " + ((Object) c2));
            case 3:
                return addInteger(((ConstantInteger) c2).getBytes());
            case 4:
                return addFloat(((ConstantFloat) c2).getBytes());
            case 5:
                return addLong(((ConstantLong) c2).getBytes());
            case 6:
                return addDouble(((ConstantDouble) c2).getBytes());
            case 7:
                ConstantClass s2 = (ConstantClass) c2;
                ConstantUtf8 u8 = (ConstantUtf8) constants[s2.getNameIndex()];
                return addClass(u8.getBytes());
            case 8:
                ConstantString s3 = (ConstantString) c2;
                ConstantUtf8 u82 = (ConstantUtf8) constants[s3.getStringIndex()];
                return addString(u82.getBytes());
            case 9:
            case 10:
            case 11:
                ConstantCP m2 = (ConstantCP) c2;
                ConstantClass clazz = (ConstantClass) constants[m2.getClassIndex()];
                ConstantNameAndType n2 = (ConstantNameAndType) constants[m2.getNameAndTypeIndex()];
                ConstantUtf8 u83 = (ConstantUtf8) constants[clazz.getNameIndex()];
                String class_name = u83.getBytes().replace('/', '.');
                ConstantUtf8 u84 = (ConstantUtf8) constants[n2.getNameIndex()];
                String name = u84.getBytes();
                ConstantUtf8 u85 = (ConstantUtf8) constants[n2.getSignatureIndex()];
                String signature = u85.getBytes();
                switch (c2.getTag()) {
                    case 9:
                        return addFieldref(class_name, name, signature);
                    case 10:
                        return addMethodref(class_name, name, signature);
                    case 11:
                        return addInterfaceMethodref(class_name, name, signature);
                    default:
                        throw new RuntimeException("Unknown constant type " + ((Object) c2));
                }
            case 12:
                ConstantNameAndType n3 = (ConstantNameAndType) c2;
                ConstantUtf8 u86 = (ConstantUtf8) constants[n3.getNameIndex()];
                ConstantUtf8 u8_2 = (ConstantUtf8) constants[n3.getSignatureIndex()];
                return addNameAndType(u86.getBytes(), u8_2.getBytes());
        }
    }
}
