package com.sun.corba.se.impl.corba;

/* loaded from: rt.jar:com/sun/corba/se/impl/corba/TypeCodeFactory.class */
public interface TypeCodeFactory {
    void setTypeCode(String str, TypeCodeImpl typeCodeImpl);

    TypeCodeImpl getTypeCode(String str);

    void setTypeCodeForClass(Class cls, TypeCodeImpl typeCodeImpl);

    TypeCodeImpl getTypeCodeForClass(Class cls);
}
