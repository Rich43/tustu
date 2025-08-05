package com.sun.corba.se.spi.transport;

import com.sun.corba.se.pept.transport.ContactInfo;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.iiop.IIOPProfile;

/* loaded from: rt.jar:com/sun/corba/se/spi/transport/CorbaContactInfo.class */
public interface CorbaContactInfo extends ContactInfo {
    IOR getTargetIOR();

    IOR getEffectiveTargetIOR();

    IIOPProfile getEffectiveProfile();

    void setAddressingDisposition(short s2);

    short getAddressingDisposition();

    String getMonitoringName();
}
