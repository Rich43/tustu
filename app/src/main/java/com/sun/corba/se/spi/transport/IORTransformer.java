package com.sun.corba.se.spi.transport;

import com.sun.corba.se.spi.encoding.CorbaInputObject;
import com.sun.corba.se.spi.encoding.CorbaOutputObject;
import com.sun.corba.se.spi.ior.IOR;

/* loaded from: rt.jar:com/sun/corba/se/spi/transport/IORTransformer.class */
public interface IORTransformer {
    IOR unmarshal(CorbaInputObject corbaInputObject);

    void marshal(CorbaOutputObject corbaOutputObject, IOR ior);
}
