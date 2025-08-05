package org.omg.PortableInterceptor;

import org.omg.CORBA.Any;

/* loaded from: rt.jar:org/omg/PortableInterceptor/CurrentOperations.class */
public interface CurrentOperations extends org.omg.CORBA.CurrentOperations {
    Any get_slot(int i2) throws InvalidSlot;

    void set_slot(int i2, Any any) throws InvalidSlot;
}
