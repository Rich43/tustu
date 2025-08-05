package com.sun.corba.se.spi.transport;

import com.sun.corba.se.pept.transport.ContactInfo;
import java.util.List;

/* loaded from: rt.jar:com/sun/corba/se/spi/transport/IIOPPrimaryToContactInfo.class */
public interface IIOPPrimaryToContactInfo {
    void reset(ContactInfo contactInfo);

    boolean hasNext(ContactInfo contactInfo, ContactInfo contactInfo2, List list);

    ContactInfo next(ContactInfo contactInfo, ContactInfo contactInfo2, List list);
}
