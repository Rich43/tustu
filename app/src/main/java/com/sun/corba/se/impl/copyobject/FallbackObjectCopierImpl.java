package com.sun.corba.se.impl.copyobject;

import com.sun.corba.se.spi.copyobject.ObjectCopier;
import com.sun.corba.se.spi.copyobject.ReflectiveCopyException;

/* loaded from: rt.jar:com/sun/corba/se/impl/copyobject/FallbackObjectCopierImpl.class */
public class FallbackObjectCopierImpl implements ObjectCopier {
    private ObjectCopier first;
    private ObjectCopier second;

    public FallbackObjectCopierImpl(ObjectCopier objectCopier, ObjectCopier objectCopier2) {
        this.first = objectCopier;
        this.second = objectCopier2;
    }

    @Override // com.sun.corba.se.spi.copyobject.ObjectCopier
    public Object copy(Object obj) throws ReflectiveCopyException {
        try {
            return this.first.copy(obj);
        } catch (ReflectiveCopyException e2) {
            return this.second.copy(obj);
        }
    }
}
