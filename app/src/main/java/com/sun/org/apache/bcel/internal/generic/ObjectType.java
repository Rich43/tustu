package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.Repository;
import com.sun.org.apache.bcel.internal.classfile.JavaClass;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/ObjectType.class */
public final class ObjectType extends ReferenceType {
    private String class_name;

    public ObjectType(String class_name) {
        super((byte) 14, "L" + class_name.replace('.', '/') + ";");
        this.class_name = class_name.replace('/', '.');
    }

    public String getClassName() {
        return this.class_name;
    }

    public int hashCode() {
        return this.class_name.hashCode();
    }

    public boolean equals(Object type) {
        if (type instanceof ObjectType) {
            return ((ObjectType) type).class_name.equals(this.class_name);
        }
        return false;
    }

    public boolean referencesClass() {
        JavaClass jc = Repository.lookupClass(this.class_name);
        if (jc == null) {
            return false;
        }
        return jc.isClass();
    }

    public boolean referencesInterface() {
        JavaClass jc = Repository.lookupClass(this.class_name);
        return (jc == null || jc.isClass()) ? false : true;
    }

    public boolean subclassOf(ObjectType superclass) {
        if (referencesInterface() || superclass.referencesInterface()) {
            return false;
        }
        return Repository.instanceOf(this.class_name, superclass.class_name);
    }

    public boolean accessibleTo(ObjectType accessor) {
        JavaClass jc = Repository.lookupClass(this.class_name);
        if (jc.isPublic()) {
            return true;
        }
        JavaClass acc = Repository.lookupClass(accessor.class_name);
        return acc.getPackageName().equals(jc.getPackageName());
    }
}
