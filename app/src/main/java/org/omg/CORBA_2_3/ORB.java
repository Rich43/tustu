package org.omg.CORBA_2_3;

import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.Object;
import org.omg.CORBA.portable.ValueFactory;

/* loaded from: rt.jar:org/omg/CORBA_2_3/ORB.class */
public abstract class ORB extends org.omg.CORBA.ORB {
    public ValueFactory register_value_factory(String str, ValueFactory valueFactory) {
        throw new NO_IMPLEMENT();
    }

    public void unregister_value_factory(String str) {
        throw new NO_IMPLEMENT();
    }

    public ValueFactory lookup_value_factory(String str) {
        throw new NO_IMPLEMENT();
    }

    public Object get_value_def(String str) throws BAD_PARAM {
        throw new NO_IMPLEMENT();
    }

    public void set_delegate(Object obj) {
        throw new NO_IMPLEMENT();
    }
}
