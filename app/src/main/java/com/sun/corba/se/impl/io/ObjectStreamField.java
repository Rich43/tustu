package com.sun.corba.se.impl.io;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.corba.Bridge;

/* loaded from: rt.jar:com/sun/corba/se/impl/io/ObjectStreamField.class */
public class ObjectStreamField implements Comparable {
    private static final Bridge bridge = (Bridge) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.corba.se.impl.io.ObjectStreamField.1
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public Object run2() {
            return Bridge.get();
        }
    });
    private String name;
    private char type;
    private Field field;
    private String typeString;
    private Class clazz;
    private String signature;
    private long fieldID;

    ObjectStreamField(String str, Class cls) {
        this.fieldID = -1L;
        this.name = str;
        this.clazz = cls;
        if (cls.isPrimitive()) {
            if (cls == Integer.TYPE) {
                this.type = 'I';
            } else if (cls == Byte.TYPE) {
                this.type = 'B';
            } else if (cls == Long.TYPE) {
                this.type = 'J';
            } else if (cls == Float.TYPE) {
                this.type = 'F';
            } else if (cls == Double.TYPE) {
                this.type = 'D';
            } else if (cls == Short.TYPE) {
                this.type = 'S';
            } else if (cls == Character.TYPE) {
                this.type = 'C';
            } else if (cls == Boolean.TYPE) {
                this.type = 'Z';
            }
        } else if (cls.isArray()) {
            this.type = '[';
            this.typeString = ObjectStreamClass.getSignature((Class<?>) cls);
        } else {
            this.type = 'L';
            this.typeString = ObjectStreamClass.getSignature((Class<?>) cls);
        }
        if (this.typeString != null) {
            this.signature = this.typeString;
        } else {
            this.signature = String.valueOf(this.type);
        }
    }

    ObjectStreamField(Field field) {
        this(field.getName(), field.getType());
        setField(field);
    }

    ObjectStreamField(String str, char c2, Field field, String str2) {
        this.fieldID = -1L;
        this.name = str;
        this.type = c2;
        setField(field);
        this.typeString = str2;
        if (this.typeString != null) {
            this.signature = this.typeString;
        } else {
            this.signature = String.valueOf(this.type);
        }
    }

    public String getName() {
        return this.name;
    }

    public Class getType() {
        if (this.clazz != null) {
            return this.clazz;
        }
        switch (this.type) {
            case 'B':
                this.clazz = Byte.TYPE;
                break;
            case 'C':
                this.clazz = Character.TYPE;
                break;
            case 'D':
                this.clazz = Double.TYPE;
                break;
            case 'F':
                this.clazz = Float.TYPE;
                break;
            case 'I':
                this.clazz = Integer.TYPE;
                break;
            case 'J':
                this.clazz = Long.TYPE;
                break;
            case 'L':
            case '[':
                this.clazz = Object.class;
                break;
            case 'S':
                this.clazz = Short.TYPE;
                break;
            case 'Z':
                this.clazz = Boolean.TYPE;
                break;
        }
        return this.clazz;
    }

    public char getTypeCode() {
        return this.type;
    }

    public String getTypeString() {
        return this.typeString;
    }

    Field getField() {
        return this.field;
    }

    void setField(Field field) {
        this.field = field;
        this.fieldID = bridge.objectFieldOffset(field);
    }

    ObjectStreamField() {
        this.fieldID = -1L;
    }

    public boolean isPrimitive() {
        return (this.type == '[' || this.type == 'L') ? false : true;
    }

    @Override // java.lang.Comparable
    public int compareTo(Object obj) {
        ObjectStreamField objectStreamField = (ObjectStreamField) obj;
        boolean z2 = this.typeString == null;
        if (z2 != (objectStreamField.typeString == null)) {
            return z2 ? -1 : 1;
        }
        return this.name.compareTo(objectStreamField.name);
    }

    public boolean typeEquals(ObjectStreamField objectStreamField) {
        if (objectStreamField == null || this.type != objectStreamField.type) {
            return false;
        }
        if (this.typeString == null && objectStreamField.typeString == null) {
            return true;
        }
        return ObjectStreamClass.compareClassNames(this.typeString, objectStreamField.typeString, '/');
    }

    public String getSignature() {
        return this.signature;
    }

    public String toString() {
        if (this.typeString != null) {
            return this.typeString + " " + this.name;
        }
        return this.type + " " + this.name;
    }

    public Class getClazz() {
        return this.clazz;
    }

    public long getFieldID() {
        return this.fieldID;
    }
}
