package com.sun.corba.se.spi.transport;

import com.sun.corba.se.pept.transport.ContactInfoList;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.protocol.LocalClientRequestDispatcher;

/* loaded from: rt.jar:com/sun/corba/se/spi/transport/CorbaContactInfoList.class */
public interface CorbaContactInfoList extends ContactInfoList {
    void setTargetIOR(IOR ior);

    IOR getTargetIOR();

    void setEffectiveTargetIOR(IOR ior);

    IOR getEffectiveTargetIOR();

    LocalClientRequestDispatcher getLocalClientRequestDispatcher();

    int hashCode();
}
