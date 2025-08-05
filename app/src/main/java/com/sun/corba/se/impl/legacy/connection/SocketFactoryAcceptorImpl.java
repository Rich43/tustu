package com.sun.corba.se.impl.legacy.connection;

import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.impl.transport.SocketOrChannelAcceptorImpl;
import com.sun.corba.se.spi.orb.ORB;

/* loaded from: rt.jar:com/sun/corba/se/impl/legacy/connection/SocketFactoryAcceptorImpl.class */
public class SocketFactoryAcceptorImpl extends SocketOrChannelAcceptorImpl {
    public SocketFactoryAcceptorImpl(ORB orb, int i2, String str, String str2) {
        super(orb, i2, str, str2);
    }

    @Override // com.sun.corba.se.impl.transport.SocketOrChannelAcceptorImpl, com.sun.corba.se.pept.transport.Acceptor
    public boolean initialize() {
        if (this.initialized) {
            return false;
        }
        if (this.orb.transportDebugFlag) {
            dprint("initialize: " + ((Object) this));
        }
        try {
            this.serverSocket = this.orb.getORBData().getLegacySocketFactory().createServerSocket(this.type, this.port);
            internalInitialize();
            this.initialized = true;
            return true;
        } catch (Throwable th) {
            throw this.wrapper.createListenerFailed(th, Integer.toString(this.port));
        }
    }

    @Override // com.sun.corba.se.impl.transport.SocketOrChannelAcceptorImpl
    protected String toStringName() {
        return "SocketFactoryAcceptorImpl";
    }

    @Override // com.sun.corba.se.impl.transport.SocketOrChannelAcceptorImpl
    protected void dprint(String str) {
        ORBUtility.dprint(toStringName(), str);
    }
}
