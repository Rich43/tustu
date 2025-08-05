package org.omg.CORBA;

import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:org/omg/CORBA/UnionMember.class */
public final class UnionMember implements IDLEntity {
    public String name;
    public Any label;
    public TypeCode type;
    public IDLType type_def;

    public UnionMember() {
    }

    public UnionMember(String str, Any any, TypeCode typeCode, IDLType iDLType) {
        this.name = str;
        this.label = any;
        this.type = typeCode;
        this.type_def = iDLType;
    }
}
