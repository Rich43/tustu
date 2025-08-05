package com.sun.corba.se.spi.transport;

import com.sun.corba.se.impl.protocol.CorbaClientDelegateImpl;
import com.sun.corba.se.impl.transport.CorbaContactInfoListImpl;
import com.sun.corba.se.impl.transport.ReadTCPTimeoutsImpl;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.protocol.ClientDelegateFactory;
import com.sun.corba.se.spi.protocol.CorbaClientDelegate;

/* loaded from: rt.jar:com/sun/corba/se/spi/transport/TransportDefault.class */
public abstract class TransportDefault {
    private TransportDefault() {
    }

    public static CorbaContactInfoListFactory makeCorbaContactInfoListFactory(final ORB orb) {
        return new CorbaContactInfoListFactory() { // from class: com.sun.corba.se.spi.transport.TransportDefault.1
            @Override // com.sun.corba.se.spi.transport.CorbaContactInfoListFactory
            public void setORB(ORB orb2) {
            }

            @Override // com.sun.corba.se.spi.transport.CorbaContactInfoListFactory
            public CorbaContactInfoList create(IOR ior) {
                return new CorbaContactInfoListImpl(orb, ior);
            }
        };
    }

    public static ClientDelegateFactory makeClientDelegateFactory(final ORB orb) {
        return new ClientDelegateFactory() { // from class: com.sun.corba.se.spi.transport.TransportDefault.2
            @Override // com.sun.corba.se.spi.protocol.ClientDelegateFactory
            public CorbaClientDelegate create(CorbaContactInfoList corbaContactInfoList) {
                return new CorbaClientDelegateImpl(orb, corbaContactInfoList);
            }
        };
    }

    public static IORTransformer makeIORTransformer(ORB orb) {
        return null;
    }

    public static ReadTimeoutsFactory makeReadTimeoutsFactory() {
        return new ReadTimeoutsFactory() { // from class: com.sun.corba.se.spi.transport.TransportDefault.3
            @Override // com.sun.corba.se.spi.transport.ReadTimeoutsFactory
            public ReadTimeouts create(int i2, int i3, int i4, int i5) {
                return new ReadTCPTimeoutsImpl(i2, i3, i4, i5);
            }
        };
    }
}
