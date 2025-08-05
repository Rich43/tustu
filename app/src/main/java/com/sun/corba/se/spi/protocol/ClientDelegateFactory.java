package com.sun.corba.se.spi.protocol;

import com.sun.corba.se.spi.transport.CorbaContactInfoList;

/* loaded from: rt.jar:com/sun/corba/se/spi/protocol/ClientDelegateFactory.class */
public interface ClientDelegateFactory {
    CorbaClientDelegate create(CorbaContactInfoList corbaContactInfoList);
}
