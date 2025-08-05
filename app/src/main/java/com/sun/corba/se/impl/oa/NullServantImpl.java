package com.sun.corba.se.impl.oa;

import com.sun.corba.se.spi.oa.NullServant;
import org.omg.CORBA.SystemException;

/* loaded from: rt.jar:com/sun/corba/se/impl/oa/NullServantImpl.class */
public class NullServantImpl implements NullServant {
    private SystemException sysex;

    public NullServantImpl(SystemException systemException) {
        this.sysex = systemException;
    }

    @Override // com.sun.corba.se.spi.oa.NullServant
    public SystemException getException() {
        return this.sysex;
    }
}
