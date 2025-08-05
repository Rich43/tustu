package org.omg.CORBA;

import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:org/omg/CORBA/ValueMember.class */
public final class ValueMember implements IDLEntity {
    public String name;
    public String id;
    public String defined_in;
    public String version;
    public TypeCode type;
    public IDLType type_def;
    public short access;

    public ValueMember() {
    }

    public ValueMember(String str, String str2, String str3, String str4, TypeCode typeCode, IDLType iDLType, short s2) {
        this.name = str;
        this.id = str2;
        this.defined_in = str3;
        this.version = str4;
        this.type = typeCode;
        this.type_def = iDLType;
        this.access = s2;
    }
}
