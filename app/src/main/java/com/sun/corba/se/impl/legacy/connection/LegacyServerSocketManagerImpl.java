package com.sun.corba.se.impl.legacy.connection;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo;
import com.sun.corba.se.spi.legacy.connection.LegacyServerSocketManager;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import java.util.Collection;
import java.util.Iterator;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.INTERNAL;

/* loaded from: rt.jar:com/sun/corba/se/impl/legacy/connection/LegacyServerSocketManagerImpl.class */
public class LegacyServerSocketManagerImpl implements LegacyServerSocketManager {
    protected ORB orb;
    private ORBUtilSystemException wrapper;

    public LegacyServerSocketManagerImpl(ORB orb) {
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_TRANSPORT);
    }

    @Override // com.sun.corba.se.spi.legacy.connection.LegacyServerSocketManager
    public int legacyGetTransientServerPort(String str) {
        return legacyGetServerPort(str, false);
    }

    @Override // com.sun.corba.se.spi.legacy.connection.LegacyServerSocketManager
    public synchronized int legacyGetPersistentServerPort(String str) {
        if (this.orb.getORBData().getServerIsORBActivated()) {
            return legacyGetServerPort(str, true);
        }
        if (this.orb.getORBData().getPersistentPortInitialized()) {
            return this.orb.getORBData().getPersistentServerPort();
        }
        throw this.wrapper.persistentServerportNotSet(CompletionStatus.COMPLETED_MAYBE);
    }

    @Override // com.sun.corba.se.spi.legacy.connection.LegacyServerSocketManager
    public synchronized int legacyGetTransientOrPersistentServerPort(String str) {
        return legacyGetServerPort(str, this.orb.getORBData().getServerIsORBActivated());
    }

    @Override // com.sun.corba.se.spi.legacy.connection.LegacyServerSocketManager
    public synchronized LegacyServerSocketEndPointInfo legacyGetEndpoint(String str) {
        Iterator acceptorIterator = getAcceptorIterator();
        while (acceptorIterator.hasNext()) {
            LegacyServerSocketEndPointInfo legacyServerSocketEndPointInfoCast = cast(acceptorIterator.next());
            if (legacyServerSocketEndPointInfoCast != null && str.equals(legacyServerSocketEndPointInfoCast.getName())) {
                return legacyServerSocketEndPointInfoCast;
            }
        }
        throw new INTERNAL("No acceptor for: " + str);
    }

    @Override // com.sun.corba.se.spi.legacy.connection.LegacyServerSocketManager
    public boolean legacyIsLocalServerPort(int i2) {
        Iterator acceptorIterator = getAcceptorIterator();
        while (acceptorIterator.hasNext()) {
            LegacyServerSocketEndPointInfo legacyServerSocketEndPointInfoCast = cast(acceptorIterator.next());
            if (legacyServerSocketEndPointInfoCast != null && legacyServerSocketEndPointInfoCast.getPort() == i2) {
                return true;
            }
        }
        return false;
    }

    private int legacyGetServerPort(String str, boolean z2) {
        Iterator acceptorIterator = getAcceptorIterator();
        while (acceptorIterator.hasNext()) {
            LegacyServerSocketEndPointInfo legacyServerSocketEndPointInfoCast = cast(acceptorIterator.next());
            if (legacyServerSocketEndPointInfoCast != null && legacyServerSocketEndPointInfoCast.getType().equals(str)) {
                if (z2) {
                    return legacyServerSocketEndPointInfoCast.getLocatorPort();
                }
                return legacyServerSocketEndPointInfoCast.getPort();
            }
        }
        return -1;
    }

    private Iterator getAcceptorIterator() {
        Collection acceptors = this.orb.getCorbaTransportManager().getAcceptors(null, null);
        if (acceptors != null) {
            return acceptors.iterator();
        }
        throw this.wrapper.getServerPortCalledBeforeEndpointsInitialized();
    }

    private LegacyServerSocketEndPointInfo cast(Object obj) {
        if (obj instanceof LegacyServerSocketEndPointInfo) {
            return (LegacyServerSocketEndPointInfo) obj;
        }
        return null;
    }

    protected void dprint(String str) {
        ORBUtility.dprint("LegacyServerSocketManagerImpl", str);
    }
}
