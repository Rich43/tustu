package com.sun.jna;

import com.sun.jna.Structure;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/Union.class */
public abstract class Union extends Structure {
    private Structure.StructField activeField;
    Structure.StructField biggestField;
    static Class class$com$sun$jna$Structure;
    static Class class$java$lang$String;
    static Class class$com$sun$jna$WString;

    protected Union() {
    }

    protected Union(Pointer p2) {
        super(p2);
    }

    protected Union(Pointer p2, int alignType) {
        super(p2, alignType);
    }

    protected Union(TypeMapper mapper) {
        super(mapper);
    }

    protected Union(Pointer p2, int alignType, TypeMapper mapper) {
        super(p2, alignType, mapper);
    }

    public void setType(Class type) {
        ensureAllocated();
        for (Structure.StructField f2 : fields().values()) {
            if (f2.type == type) {
                this.activeField = f2;
                return;
            }
        }
        throw new IllegalArgumentException(new StringBuffer().append("No field of type ").append((Object) type).append(" in ").append((Object) this).toString());
    }

    public void setType(String fieldName) {
        ensureAllocated();
        Structure.StructField f2 = (Structure.StructField) fields().get(fieldName);
        if (f2 != null) {
            this.activeField = f2;
            return;
        }
        throw new IllegalArgumentException(new StringBuffer().append("No field named ").append(fieldName).append(" in ").append((Object) this).toString());
    }

    @Override // com.sun.jna.Structure
    public Object readField(String fieldName) {
        ensureAllocated();
        setType(fieldName);
        return super.readField(fieldName);
    }

    @Override // com.sun.jna.Structure
    public void writeField(String fieldName) throws Throwable {
        ensureAllocated();
        setType(fieldName);
        super.writeField(fieldName);
    }

    @Override // com.sun.jna.Structure
    public void writeField(String fieldName, Object value) throws Throwable {
        ensureAllocated();
        setType(fieldName);
        super.writeField(fieldName, value);
    }

    public Object getTypedValue(Class type) {
        ensureAllocated();
        for (Structure.StructField f2 : fields().values()) {
            if (f2.type == type) {
                this.activeField = f2;
                read();
                return getField(this.activeField);
            }
        }
        throw new IllegalArgumentException(new StringBuffer().append("No field of type ").append((Object) type).append(" in ").append((Object) this).toString());
    }

    public Object setTypedValue(Object object) {
        ensureAllocated();
        Structure.StructField f2 = findField(object.getClass());
        if (f2 != null) {
            this.activeField = f2;
            setField(f2, object);
            return this;
        }
        throw new IllegalArgumentException(new StringBuffer().append("No field of type ").append((Object) object.getClass()).append(" in ").append((Object) this).toString());
    }

    private Structure.StructField findField(Class type) {
        for (Structure.StructField f2 : fields().values()) {
            if (f2.type.isAssignableFrom(type)) {
                return f2;
            }
        }
        return null;
    }

    @Override // com.sun.jna.Structure
    void writeField(Structure.StructField field) throws Throwable {
        if (field == this.activeField) {
            super.writeField(field);
        }
    }

    @Override // com.sun.jna.Structure
    Object readField(Structure.StructField field) throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        if (field != this.activeField) {
            if (class$com$sun$jna$Structure == null) {
                clsClass$ = class$("com.sun.jna.Structure");
                class$com$sun$jna$Structure = clsClass$;
            } else {
                clsClass$ = class$com$sun$jna$Structure;
            }
            if (clsClass$.isAssignableFrom(field.type)) {
                return null;
            }
            if (class$java$lang$String == null) {
                clsClass$2 = class$("java.lang.String");
                class$java$lang$String = clsClass$2;
            } else {
                clsClass$2 = class$java$lang$String;
            }
            if (clsClass$2.isAssignableFrom(field.type)) {
                return null;
            }
            if (class$com$sun$jna$WString == null) {
                clsClass$3 = class$("com.sun.jna.WString");
                class$com$sun$jna$WString = clsClass$3;
            } else {
                clsClass$3 = class$com$sun$jna$WString;
            }
            if (clsClass$3.isAssignableFrom(field.type)) {
                return null;
            }
        }
        return super.readField(field);
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    @Override // com.sun.jna.Structure
    int calculateSize(boolean force) throws Throwable {
        Class clsClass$;
        int size = super.calculateSize(force);
        if (size != -1) {
            int fsize = 0;
            for (Structure.StructField f2 : fields().values()) {
                f2.offset = 0;
                if (f2.size <= fsize) {
                    if (f2.size == fsize) {
                        if (class$com$sun$jna$Structure == null) {
                            clsClass$ = class$("com.sun.jna.Structure");
                            class$com$sun$jna$Structure = clsClass$;
                        } else {
                            clsClass$ = class$com$sun$jna$Structure;
                        }
                        if (clsClass$.isAssignableFrom(f2.type)) {
                        }
                    }
                }
                fsize = f2.size;
                this.biggestField = f2;
            }
            size = calculateAlignedSize(fsize);
            if (size > 0 && (this instanceof Structure.ByValue)) {
                getTypeInfo();
            }
        }
        return size;
    }

    @Override // com.sun.jna.Structure
    protected int getNativeAlignment(Class type, Object value, boolean isFirstElement) {
        return super.getNativeAlignment(type, value, true);
    }

    @Override // com.sun.jna.Structure
    Pointer getTypeInfo() {
        if (this.biggestField == null) {
            return null;
        }
        return super.getTypeInfo();
    }
}
