package com.sun.corba.se.impl.protocol;

/* compiled from: SpecialMethod.java */
/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/NotExistent.class */
class NotExistent extends NonExistent {
    NotExistent() {
    }

    @Override // com.sun.corba.se.impl.protocol.NonExistent, com.sun.corba.se.impl.protocol.SpecialMethod
    public String getName() {
        return "_not_existent";
    }
}
