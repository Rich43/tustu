package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.classfile.Attribute;
import com.sun.org.apache.bcel.internal.classfile.ConstantObject;
import com.sun.org.apache.bcel.internal.classfile.ConstantPool;
import com.sun.org.apache.bcel.internal.classfile.ConstantValue;
import com.sun.org.apache.bcel.internal.classfile.Field;
import com.sun.org.apache.bcel.internal.classfile.Utility;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/FieldGen.class */
public class FieldGen extends FieldGenOrMethodGen {
    private Object value;
    private ArrayList observers;

    public FieldGen(int access_flags, Type type, String name, ConstantPoolGen cp) {
        this.value = null;
        setAccessFlags(access_flags);
        setType(type);
        setName(name);
        setConstantPool(cp);
    }

    public FieldGen(Field field, ConstantPoolGen cp) {
        this(field.getAccessFlags(), Type.getType(field.getSignature()), field.getName(), cp);
        Attribute[] attrs = field.getAttributes();
        for (int i2 = 0; i2 < attrs.length; i2++) {
            if (attrs[i2] instanceof ConstantValue) {
                setValue(((ConstantValue) attrs[i2]).getConstantValueIndex());
            } else {
                addAttribute(attrs[i2]);
            }
        }
    }

    private void setValue(int index) {
        ConstantPool cp = this.cp.getConstantPool();
        this.value = ((ConstantObject) cp.getConstant(index)).getConstantValue(cp);
    }

    public void setInitValue(String str) {
        checkType(new ObjectType("java.lang.String"));
        if (str != null) {
            this.value = str;
        }
    }

    public void setInitValue(long l2) {
        checkType(Type.LONG);
        if (l2 != 0) {
            this.value = new Long(l2);
        }
    }

    public void setInitValue(int i2) {
        checkType(Type.INT);
        if (i2 != 0) {
            this.value = new Integer(i2);
        }
    }

    public void setInitValue(short s2) {
        checkType(Type.SHORT);
        if (s2 != 0) {
            this.value = new Integer(s2);
        }
    }

    public void setInitValue(char c2) {
        checkType(Type.CHAR);
        if (c2 != 0) {
            this.value = new Integer(c2);
        }
    }

    public void setInitValue(byte b2) {
        checkType(Type.BYTE);
        if (b2 != 0) {
            this.value = new Integer(b2);
        }
    }

    public void setInitValue(boolean b2) {
        checkType(Type.BOOLEAN);
        if (b2) {
            this.value = new Integer(1);
        }
    }

    public void setInitValue(float f2) {
        checkType(Type.FLOAT);
        if (f2 != 0.0d) {
            this.value = new Float(f2);
        }
    }

    public void setInitValue(double d2) {
        checkType(Type.DOUBLE);
        if (d2 != 0.0d) {
            this.value = new Double(d2);
        }
    }

    public void cancelInitValue() {
        this.value = null;
    }

    private void checkType(Type atype) {
        if (this.type == null) {
            throw new ClassGenException("You haven't defined the type of the field yet");
        }
        if (!isFinal()) {
            throw new ClassGenException("Only final fields may have an initial value!");
        }
        if (!this.type.equals(atype)) {
            throw new ClassGenException("Types are not compatible: " + ((Object) this.type) + " vs. " + ((Object) atype));
        }
    }

    public Field getField() {
        String signature = getSignature();
        int name_index = this.cp.addUtf8(this.name);
        int signature_index = this.cp.addUtf8(signature);
        if (this.value != null) {
            checkType(this.type);
            int index = addConstant();
            addAttribute(new ConstantValue(this.cp.addUtf8("ConstantValue"), 2, index, this.cp.getConstantPool()));
        }
        return new Field(this.access_flags, name_index, signature_index, getAttributes(), this.cp.getConstantPool());
    }

    private int addConstant() {
        switch (this.type.getType()) {
            case 4:
            case 5:
            case 8:
            case 9:
            case 10:
                return this.cp.addInteger(((Integer) this.value).intValue());
            case 6:
                return this.cp.addFloat(((Float) this.value).floatValue());
            case 7:
                return this.cp.addDouble(((Double) this.value).doubleValue());
            case 11:
                return this.cp.addLong(((Long) this.value).longValue());
            case 12:
            case 13:
            default:
                throw new RuntimeException("Oops: Unhandled : " + ((int) this.type.getType()));
            case 14:
                return this.cp.addString((String) this.value);
        }
    }

    @Override // com.sun.org.apache.bcel.internal.generic.FieldGenOrMethodGen
    public String getSignature() {
        return this.type.getSignature();
    }

    public void addObserver(FieldObserver o2) {
        if (this.observers == null) {
            this.observers = new ArrayList();
        }
        this.observers.add(o2);
    }

    public void removeObserver(FieldObserver o2) {
        if (this.observers != null) {
            this.observers.remove(o2);
        }
    }

    public void update() {
        if (this.observers != null) {
            Iterator e2 = this.observers.iterator();
            while (e2.hasNext()) {
                ((FieldObserver) e2.next()).notify(this);
            }
        }
    }

    public String getInitValue() {
        if (this.value != null) {
            return this.value.toString();
        }
        return null;
    }

    public final String toString() {
        String access = Utility.accessToString(this.access_flags);
        String access2 = access.equals("") ? "" : access + " ";
        String signature = this.type.toString();
        String name = getName();
        StringBuffer buf = new StringBuffer(access2 + signature + " " + name);
        String value = getInitValue();
        if (value != null) {
            buf.append(" = " + value);
        }
        return buf.toString();
    }

    public FieldGen copy(ConstantPoolGen cp) {
        FieldGen fg = (FieldGen) clone();
        fg.setConstantPool(cp);
        return fg;
    }
}
