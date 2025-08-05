package com.sun.org.omg.CORBA;

import org.omg.CORBA.StructMember;
import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:com/sun/org/omg/CORBA/Initializer.class */
public final class Initializer implements IDLEntity {
    public StructMember[] members;
    public String name;

    public Initializer() {
        this.members = null;
        this.name = null;
    }

    public Initializer(StructMember[] structMemberArr, String str) {
        this.members = null;
        this.name = null;
        this.members = structMemberArr;
        this.name = str;
    }
}
