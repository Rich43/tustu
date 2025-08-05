package com.sun.org.omg.CORBA;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:com/sun/org/omg/CORBA/AttributeDescription.class */
public final class AttributeDescription implements IDLEntity {
    public String name;
    public String id;
    public String defined_in;
    public String version;
    public TypeCode type;
    public AttributeMode mode;

    public AttributeDescription() {
        this.name = null;
        this.id = null;
        this.defined_in = null;
        this.version = null;
        this.type = null;
        this.mode = null;
    }

    public AttributeDescription(String str, String str2, String str3, String str4, TypeCode typeCode, AttributeMode attributeMode) {
        this.name = null;
        this.id = null;
        this.defined_in = null;
        this.version = null;
        this.type = null;
        this.mode = null;
        this.name = str;
        this.id = str2;
        this.defined_in = str3;
        this.version = str4;
        this.type = typeCode;
        this.mode = attributeMode;
    }
}
