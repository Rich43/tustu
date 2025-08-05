package com.sun.corba.se.impl.protocol;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.pept.protocol.MessageMediator;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.ObjectKey;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
import com.sun.corba.se.spi.protocol.CorbaServerRequestDispatcher;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/INSServerRequestDispatcher.class */
public class INSServerRequestDispatcher implements CorbaServerRequestDispatcher {
    private ORB orb;
    private ORBUtilSystemException wrapper;

    public INSServerRequestDispatcher(ORB orb) {
        this.orb = null;
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_PROTOCOL);
    }

    @Override // com.sun.corba.se.spi.protocol.CorbaServerRequestDispatcher
    public IOR locate(ObjectKey objectKey) {
        return getINSReference(new String(objectKey.getBytes(this.orb)));
    }

    @Override // com.sun.corba.se.pept.protocol.ServerRequestDispatcher
    public void dispatch(MessageMediator messageMediator) {
        CorbaMessageMediator corbaMessageMediator = (CorbaMessageMediator) messageMediator;
        corbaMessageMediator.getProtocolHandler().createLocationForward(corbaMessageMediator, getINSReference(new String(corbaMessageMediator.getObjectKey().getBytes(this.orb))), null);
    }

    private IOR getINSReference(String str) {
        IOR ior = ORBUtility.getIOR(this.orb.getLocalResolver().resolve(str));
        if (ior != null) {
            return ior;
        }
        throw this.wrapper.servantNotFound();
    }
}
