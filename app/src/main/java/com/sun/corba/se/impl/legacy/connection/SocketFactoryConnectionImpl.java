package com.sun.corba.se.impl.legacy.connection;

import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.impl.transport.SocketOrChannelConnectionImpl;
import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.transport.CorbaContactInfo;
import com.sun.corba.se.spi.transport.SocketInfo;

/* loaded from: rt.jar:com/sun/corba/se/impl/legacy/connection/SocketFactoryConnectionImpl.class */
public class SocketFactoryConnectionImpl extends SocketOrChannelConnectionImpl {
    public SocketFactoryConnectionImpl(ORB orb, CorbaContactInfo corbaContactInfo, boolean z2, boolean z3) {
        super(orb, z2, z3);
        this.contactInfo = corbaContactInfo;
        boolean z4 = !z2;
        SocketInfo socketInfo = ((SocketFactoryContactInfoImpl) corbaContactInfo).socketInfo;
        try {
            this.socket = orb.getORBData().getLegacySocketFactory().createSocket(socketInfo);
            this.socketChannel = this.socket.getChannel();
            if (this.socketChannel != null) {
                this.socketChannel.configureBlocking(z4);
            } else {
                setUseSelectThreadToWait(false);
            }
            if (orb.transportDebugFlag) {
                dprint(".initialize: connection created: " + ((Object) this.socket));
            }
            this.state = 1;
        } catch (GetEndPointInfoAgainException e2) {
            throw this.wrapper.connectFailure(e2, socketInfo.getType(), socketInfo.getHost(), Integer.toString(socketInfo.getPort()));
        } catch (Exception e3) {
            throw this.wrapper.connectFailure(e3, socketInfo.getType(), socketInfo.getHost(), Integer.toString(socketInfo.getPort()));
        }
    }

    @Override // com.sun.corba.se.impl.transport.SocketOrChannelConnectionImpl
    public String toString() {
        String str;
        synchronized (this.stateEvent) {
            str = "SocketFactoryConnectionImpl[ " + (this.socketChannel == null ? this.socket.toString() : this.socketChannel.toString()) + " " + getStateString(this.state) + " " + shouldUseSelectThreadToWait() + " " + shouldUseWorkerThreadForEvent() + "]";
        }
        return str;
    }

    @Override // com.sun.corba.se.impl.transport.SocketOrChannelConnectionImpl, com.sun.corba.se.spi.transport.CorbaConnection
    public void dprint(String str) {
        ORBUtility.dprint("SocketFactoryConnectionImpl", str);
    }
}
