package org.omg.CORBA;

@Deprecated
/* loaded from: rt.jar:org/omg/CORBA/DynUnion.class */
public interface DynUnion extends Object, DynAny {
    boolean set_as_default();

    void set_as_default(boolean z2);

    DynAny discriminator();

    TCKind discriminator_kind();

    DynAny member();

    String member_name();

    void member_name(String str);

    TCKind member_kind();
}
