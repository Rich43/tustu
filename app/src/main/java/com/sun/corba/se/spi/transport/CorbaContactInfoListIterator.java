package com.sun.corba.se.spi.transport;

import com.sun.corba.se.pept.transport.ContactInfoListIterator;
import com.sun.corba.se.spi.ior.IOR;

/* loaded from: rt.jar:com/sun/corba/se/spi/transport/CorbaContactInfoListIterator.class */
public interface CorbaContactInfoListIterator extends ContactInfoListIterator {
    void reportAddrDispositionRetry(CorbaContactInfo corbaContactInfo, short s2);

    void reportRedirect(CorbaContactInfo corbaContactInfo, IOR ior);
}
