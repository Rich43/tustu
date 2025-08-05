package org.omg.CORBA;

import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:org/omg/CORBA/StructMember.class */
public final class StructMember implements IDLEntity {
    public String name;
    public TypeCode type;
    public IDLType type_def;

    public StructMember() {
    }

    public StructMember(String str, TypeCode typeCode, IDLType iDLType) {
        this.name = str;
        this.type = typeCode;
        this.type_def = iDLType;
    }
}
