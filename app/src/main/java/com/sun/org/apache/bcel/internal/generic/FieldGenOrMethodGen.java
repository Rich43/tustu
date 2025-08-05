package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.classfile.AccessFlags;
import com.sun.org.apache.bcel.internal.classfile.Attribute;
import java.util.ArrayList;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/FieldGenOrMethodGen.class */
public abstract class FieldGenOrMethodGen extends AccessFlags implements NamedAndTyped, Cloneable {
    protected String name;
    protected Type type;
    protected ConstantPoolGen cp;
    private ArrayList attribute_vec = new ArrayList();

    public abstract String getSignature();

    protected FieldGenOrMethodGen() {
    }

    @Override // com.sun.org.apache.bcel.internal.generic.NamedAndTyped
    public void setType(Type type) {
        if (type.getType() == 16) {
            throw new IllegalArgumentException("Type can not be " + ((Object) type));
        }
        this.type = type;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.NamedAndTyped
    public Type getType() {
        return this.type;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.NamedAndTyped
    public String getName() {
        return this.name;
    }

    @Override // com.sun.org.apache.bcel.internal.generic.NamedAndTyped
    public void setName(String name) {
        this.name = name;
    }

    public ConstantPoolGen getConstantPool() {
        return this.cp;
    }

    public void setConstantPool(ConstantPoolGen cp) {
        this.cp = cp;
    }

    public void addAttribute(Attribute a2) {
        this.attribute_vec.add(a2);
    }

    public void removeAttribute(Attribute a2) {
        this.attribute_vec.remove(a2);
    }

    public void removeAttributes() {
        this.attribute_vec.clear();
    }

    public Attribute[] getAttributes() {
        Attribute[] attributes = new Attribute[this.attribute_vec.size()];
        this.attribute_vec.toArray(attributes);
        return attributes;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            System.err.println(e2);
            return null;
        }
    }
}
