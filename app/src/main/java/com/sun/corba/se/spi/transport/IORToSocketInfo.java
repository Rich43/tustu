package com.sun.corba.se.spi.transport;

import com.sun.corba.se.spi.ior.IOR;
import java.util.List;

/* loaded from: rt.jar:com/sun/corba/se/spi/transport/IORToSocketInfo.class */
public interface IORToSocketInfo {
    List getSocketInfo(IOR ior);
}
