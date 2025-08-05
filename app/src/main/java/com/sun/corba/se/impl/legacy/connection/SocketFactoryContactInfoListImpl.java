package com.sun.corba.se.impl.legacy.connection;

import com.sun.corba.se.impl.transport.CorbaContactInfoListImpl;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.orb.ORB;
import java.util.Iterator;

/* loaded from: rt.jar:com/sun/corba/se/impl/legacy/connection/SocketFactoryContactInfoListImpl.class */
public class SocketFactoryContactInfoListImpl extends CorbaContactInfoListImpl {
    public SocketFactoryContactInfoListImpl(ORB orb) {
        super(orb);
    }

    public SocketFactoryContactInfoListImpl(ORB orb, IOR ior) {
        super(orb, ior);
    }

    @Override // com.sun.corba.se.impl.transport.CorbaContactInfoListImpl, com.sun.corba.se.pept.transport.ContactInfoList
    public Iterator iterator() {
        return new SocketFactoryContactInfoListIteratorImpl(this.orb, this);
    }
}
