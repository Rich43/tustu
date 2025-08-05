package com.sun.org.omg.CORBA;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:com/sun/org/omg/CORBA/ExceptionDescription.class */
public final class ExceptionDescription implements IDLEntity {
    public String name;
    public String id;
    public String defined_in;
    public String version;
    public TypeCode type;

    public ExceptionDescription() {
        this.name = null;
        this.id = null;
        this.defined_in = null;
        this.version = null;
        this.type = null;
    }

    public ExceptionDescription(String str, String str2, String str3, String str4, TypeCode typeCode) {
        this.name = null;
        this.id = null;
        this.defined_in = null;
        this.version = null;
        this.type = null;
        this.name = str;
        this.id = str2;
        this.defined_in = str3;
        this.version = str4;
        this.type = typeCode;
    }
}
