package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.Constants;
import com.sun.org.apache.bcel.internal.Repository;
import com.sun.org.apache.bcel.internal.classfile.JavaClass;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/ReferenceType.class */
public abstract class ReferenceType extends Type {
    protected ReferenceType(byte t2, String s2) {
        super(t2, s2);
    }

    ReferenceType() {
        super((byte) 14, "<null object>");
    }

    public boolean isCastableTo(Type t2) {
        if (equals(Type.NULL)) {
            return true;
        }
        return isAssignmentCompatibleWith(t2);
    }

    public boolean isAssignmentCompatibleWith(Type t2) {
        if (!(t2 instanceof ReferenceType)) {
            return false;
        }
        ReferenceType T2 = (ReferenceType) t2;
        if (equals(Type.NULL)) {
            return true;
        }
        if ((this instanceof ObjectType) && ((ObjectType) this).referencesClass()) {
            if ((T2 instanceof ObjectType) && ((ObjectType) T2).referencesClass() && (equals(T2) || Repository.instanceOf(((ObjectType) this).getClassName(), ((ObjectType) T2).getClassName()))) {
                return true;
            }
            if ((T2 instanceof ObjectType) && ((ObjectType) T2).referencesInterface() && Repository.implementationOf(((ObjectType) this).getClassName(), ((ObjectType) T2).getClassName())) {
                return true;
            }
        }
        if ((this instanceof ObjectType) && ((ObjectType) this).referencesInterface()) {
            if ((T2 instanceof ObjectType) && ((ObjectType) T2).referencesClass() && T2.equals(Type.OBJECT)) {
                return true;
            }
            if ((T2 instanceof ObjectType) && ((ObjectType) T2).referencesInterface() && (equals(T2) || Repository.implementationOf(((ObjectType) this).getClassName(), ((ObjectType) T2).getClassName()))) {
                return true;
            }
        }
        if (this instanceof ArrayType) {
            if ((T2 instanceof ObjectType) && ((ObjectType) T2).referencesClass() && T2.equals(Type.OBJECT)) {
                return true;
            }
            if (T2 instanceof ArrayType) {
                Type sc = ((ArrayType) this).getElementType();
                Type tc = ((ArrayType) this).getElementType();
                if ((sc instanceof BasicType) && (tc instanceof BasicType) && sc.equals(tc)) {
                    return true;
                }
                if ((tc instanceof ReferenceType) && (sc instanceof ReferenceType) && ((ReferenceType) sc).isAssignmentCompatibleWith((ReferenceType) tc)) {
                    return true;
                }
            }
            if ((T2 instanceof ObjectType) && ((ObjectType) T2).referencesInterface()) {
                for (int ii = 0; ii < Constants.INTERFACES_IMPLEMENTED_BY_ARRAYS.length; ii++) {
                    if (T2.equals(new ObjectType(Constants.INTERFACES_IMPLEMENTED_BY_ARRAYS[ii]))) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public ReferenceType getFirstCommonSuperclass(ReferenceType t2) {
        if (equals(Type.NULL)) {
            return t2;
        }
        if (!t2.equals(Type.NULL) && !equals(t2)) {
            if ((this instanceof ArrayType) && (t2 instanceof ArrayType)) {
                ArrayType arrType1 = (ArrayType) this;
                ArrayType arrType2 = (ArrayType) t2;
                if (arrType1.getDimensions() == arrType2.getDimensions() && (arrType1.getBasicType() instanceof ObjectType) && (arrType2.getBasicType() instanceof ObjectType)) {
                    return new ArrayType(((ObjectType) arrType1.getBasicType()).getFirstCommonSuperclass((ObjectType) arrType2.getBasicType()), arrType1.getDimensions());
                }
            }
            if ((this instanceof ArrayType) || (t2 instanceof ArrayType)) {
                return Type.OBJECT;
            }
            if (((this instanceof ObjectType) && ((ObjectType) this).referencesInterface()) || ((t2 instanceof ObjectType) && ((ObjectType) t2).referencesInterface())) {
                return Type.OBJECT;
            }
            ObjectType thiz = (ObjectType) this;
            ObjectType other = (ObjectType) t2;
            JavaClass[] thiz_sups = Repository.getSuperClasses(thiz.getClassName());
            JavaClass[] other_sups = Repository.getSuperClasses(other.getClassName());
            if (thiz_sups == null || other_sups == null) {
                return null;
            }
            JavaClass[] this_sups = new JavaClass[thiz_sups.length + 1];
            JavaClass[] t_sups = new JavaClass[other_sups.length + 1];
            System.arraycopy(thiz_sups, 0, this_sups, 1, thiz_sups.length);
            System.arraycopy(other_sups, 0, t_sups, 1, other_sups.length);
            this_sups[0] = Repository.lookupClass(thiz.getClassName());
            t_sups[0] = Repository.lookupClass(other.getClassName());
            for (JavaClass javaClass : t_sups) {
                for (int j2 = 0; j2 < this_sups.length; j2++) {
                    if (this_sups[j2].equals(javaClass)) {
                        return new ObjectType(this_sups[j2].getClassName());
                    }
                }
            }
            return null;
        }
        return this;
    }

    public ReferenceType firstCommonSuperclass(ReferenceType t2) {
        if (equals(Type.NULL)) {
            return t2;
        }
        if (!t2.equals(Type.NULL) && !equals(t2)) {
            if ((this instanceof ArrayType) || (t2 instanceof ArrayType)) {
                return Type.OBJECT;
            }
            if (((this instanceof ObjectType) && ((ObjectType) this).referencesInterface()) || ((t2 instanceof ObjectType) && ((ObjectType) t2).referencesInterface())) {
                return Type.OBJECT;
            }
            ObjectType thiz = (ObjectType) this;
            ObjectType other = (ObjectType) t2;
            JavaClass[] thiz_sups = Repository.getSuperClasses(thiz.getClassName());
            JavaClass[] other_sups = Repository.getSuperClasses(other.getClassName());
            if (thiz_sups == null || other_sups == null) {
                return null;
            }
            JavaClass[] this_sups = new JavaClass[thiz_sups.length + 1];
            JavaClass[] t_sups = new JavaClass[other_sups.length + 1];
            System.arraycopy(thiz_sups, 0, this_sups, 1, thiz_sups.length);
            System.arraycopy(other_sups, 0, t_sups, 1, other_sups.length);
            this_sups[0] = Repository.lookupClass(thiz.getClassName());
            t_sups[0] = Repository.lookupClass(other.getClassName());
            for (JavaClass javaClass : t_sups) {
                for (int j2 = 0; j2 < this_sups.length; j2++) {
                    if (this_sups[j2].equals(javaClass)) {
                        return new ObjectType(this_sups[j2].getClassName());
                    }
                }
            }
            return null;
        }
        return this;
    }
}
