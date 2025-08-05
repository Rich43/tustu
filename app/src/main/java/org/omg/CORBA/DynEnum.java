package org.omg.CORBA;

@Deprecated
/* loaded from: rt.jar:org/omg/CORBA/DynEnum.class */
public interface DynEnum extends Object, DynAny {
    String value_as_string();

    void value_as_string(String str);

    int value_as_ulong();

    void value_as_ulong(int i2);
}
