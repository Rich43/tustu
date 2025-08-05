package com.sun.org.omg.CORBA;

import org.omg.CORBA.IDLType;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:com/sun/org/omg/CORBA/ParameterDescription.class */
public final class ParameterDescription implements IDLEntity {
    public String name;
    public TypeCode type;
    public IDLType type_def;
    public ParameterMode mode;

    public ParameterDescription() {
        this.name = null;
        this.type = null;
        this.type_def = null;
        this.mode = null;
    }

    public ParameterDescription(String str, TypeCode typeCode, IDLType iDLType, ParameterMode parameterMode) {
        this.name = null;
        this.type = null;
        this.type_def = null;
        this.mode = null;
        this.name = str;
        this.type = typeCode;
        this.type_def = iDLType;
        this.mode = parameterMode;
    }
}
