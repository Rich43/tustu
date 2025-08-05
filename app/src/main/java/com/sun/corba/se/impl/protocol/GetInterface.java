package com.sun.corba.se.impl.protocol;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.oa.NullServant;
import com.sun.corba.se.spi.oa.ObjectAdapter;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.protocol.CorbaMessageMediator;

/* compiled from: SpecialMethod.java */
/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/GetInterface.class */
class GetInterface extends SpecialMethod {
    GetInterface() {
    }

    @Override // com.sun.corba.se.impl.protocol.SpecialMethod
    public boolean isNonExistentMethod() {
        return false;
    }

    @Override // com.sun.corba.se.impl.protocol.SpecialMethod
    public String getName() {
        return "_interface";
    }

    @Override // com.sun.corba.se.impl.protocol.SpecialMethod
    public CorbaMessageMediator invoke(Object obj, CorbaMessageMediator corbaMessageMediator, byte[] bArr, ObjectAdapter objectAdapter) {
        ORBUtilSystemException oRBUtilSystemException = ORBUtilSystemException.get((ORB) corbaMessageMediator.getBroker(), CORBALogDomains.OA_INVOCATION);
        if (obj == null || (obj instanceof NullServant)) {
            return corbaMessageMediator.getProtocolHandler().createSystemExceptionResponse(corbaMessageMediator, oRBUtilSystemException.badSkeleton(), null);
        }
        return corbaMessageMediator.getProtocolHandler().createSystemExceptionResponse(corbaMessageMediator, oRBUtilSystemException.getinterfaceNotImplemented(), null);
    }
}
