package com.sun.corba.se.spi.protocol;

import com.sun.corba.se.impl.protocol.BootstrapServerRequestDispatcher;
import com.sun.corba.se.impl.protocol.CorbaClientRequestDispatcherImpl;
import com.sun.corba.se.impl.protocol.CorbaServerRequestDispatcherImpl;
import com.sun.corba.se.impl.protocol.FullServantCacheLocalCRDImpl;
import com.sun.corba.se.impl.protocol.INSServerRequestDispatcher;
import com.sun.corba.se.impl.protocol.InfoOnlyServantCacheLocalCRDImpl;
import com.sun.corba.se.impl.protocol.JIDLLocalCRDImpl;
import com.sun.corba.se.impl.protocol.MinimalServantCacheLocalCRDImpl;
import com.sun.corba.se.impl.protocol.POALocalCRDImpl;
import com.sun.corba.se.pept.protocol.ClientRequestDispatcher;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.orb.ORB;

/* loaded from: rt.jar:com/sun/corba/se/spi/protocol/RequestDispatcherDefault.class */
public final class RequestDispatcherDefault {
    private RequestDispatcherDefault() {
    }

    public static ClientRequestDispatcher makeClientRequestDispatcher() {
        return new CorbaClientRequestDispatcherImpl();
    }

    public static CorbaServerRequestDispatcher makeServerRequestDispatcher(ORB orb) {
        return new CorbaServerRequestDispatcherImpl(orb);
    }

    public static CorbaServerRequestDispatcher makeBootstrapServerRequestDispatcher(ORB orb) {
        return new BootstrapServerRequestDispatcher(orb);
    }

    public static CorbaServerRequestDispatcher makeINSServerRequestDispatcher(ORB orb) {
        return new INSServerRequestDispatcher(orb);
    }

    public static LocalClientRequestDispatcherFactory makeMinimalServantCacheLocalClientRequestDispatcherFactory(final ORB orb) {
        return new LocalClientRequestDispatcherFactory() { // from class: com.sun.corba.se.spi.protocol.RequestDispatcherDefault.1
            @Override // com.sun.corba.se.spi.protocol.LocalClientRequestDispatcherFactory
            public LocalClientRequestDispatcher create(int i2, IOR ior) {
                return new MinimalServantCacheLocalCRDImpl(orb, i2, ior);
            }
        };
    }

    public static LocalClientRequestDispatcherFactory makeInfoOnlyServantCacheLocalClientRequestDispatcherFactory(final ORB orb) {
        return new LocalClientRequestDispatcherFactory() { // from class: com.sun.corba.se.spi.protocol.RequestDispatcherDefault.2
            @Override // com.sun.corba.se.spi.protocol.LocalClientRequestDispatcherFactory
            public LocalClientRequestDispatcher create(int i2, IOR ior) {
                return new InfoOnlyServantCacheLocalCRDImpl(orb, i2, ior);
            }
        };
    }

    public static LocalClientRequestDispatcherFactory makeFullServantCacheLocalClientRequestDispatcherFactory(final ORB orb) {
        return new LocalClientRequestDispatcherFactory() { // from class: com.sun.corba.se.spi.protocol.RequestDispatcherDefault.3
            @Override // com.sun.corba.se.spi.protocol.LocalClientRequestDispatcherFactory
            public LocalClientRequestDispatcher create(int i2, IOR ior) {
                return new FullServantCacheLocalCRDImpl(orb, i2, ior);
            }
        };
    }

    public static LocalClientRequestDispatcherFactory makeJIDLLocalClientRequestDispatcherFactory(final ORB orb) {
        return new LocalClientRequestDispatcherFactory() { // from class: com.sun.corba.se.spi.protocol.RequestDispatcherDefault.4
            @Override // com.sun.corba.se.spi.protocol.LocalClientRequestDispatcherFactory
            public LocalClientRequestDispatcher create(int i2, IOR ior) {
                return new JIDLLocalCRDImpl(orb, i2, ior);
            }
        };
    }

    public static LocalClientRequestDispatcherFactory makePOALocalClientRequestDispatcherFactory(final ORB orb) {
        return new LocalClientRequestDispatcherFactory() { // from class: com.sun.corba.se.spi.protocol.RequestDispatcherDefault.5
            @Override // com.sun.corba.se.spi.protocol.LocalClientRequestDispatcherFactory
            public LocalClientRequestDispatcher create(int i2, IOR ior) {
                return new POALocalCRDImpl(orb, i2, ior);
            }
        };
    }
}
