package com.sun.corba.se.impl.transport;

import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.iiop.AlternateIIOPAddressComponent;
import com.sun.corba.se.spi.ior.iiop.IIOPAddress;
import com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate;
import com.sun.corba.se.spi.transport.IORToSocketInfo;
import com.sun.corba.se.spi.transport.SocketInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: rt.jar:com/sun/corba/se/impl/transport/DefaultIORToSocketInfoImpl.class */
public class DefaultIORToSocketInfoImpl implements IORToSocketInfo {
    @Override // com.sun.corba.se.spi.transport.IORToSocketInfo
    public List getSocketInfo(IOR ior) {
        ArrayList arrayList = new ArrayList();
        IIOPProfileTemplate iIOPProfileTemplate = (IIOPProfileTemplate) ior.getProfile().getTaggedProfileTemplate();
        IIOPAddress primaryAddress = iIOPProfileTemplate.getPrimaryAddress();
        arrayList.add(createSocketInfo(primaryAddress.getHost().toLowerCase(), primaryAddress.getPort()));
        Iterator itIteratorById = iIOPProfileTemplate.iteratorById(3);
        while (itIteratorById.hasNext()) {
            AlternateIIOPAddressComponent alternateIIOPAddressComponent = (AlternateIIOPAddressComponent) itIteratorById.next();
            arrayList.add(createSocketInfo(alternateIIOPAddressComponent.getAddress().getHost().toLowerCase(), alternateIIOPAddressComponent.getAddress().getPort()));
        }
        return arrayList;
    }

    private SocketInfo createSocketInfo(final String str, final int i2) {
        return new SocketInfo() { // from class: com.sun.corba.se.impl.transport.DefaultIORToSocketInfoImpl.1
            @Override // com.sun.corba.se.spi.transport.SocketInfo, com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo
            public String getType() {
                return "IIOP_CLEAR_TEXT";
            }

            @Override // com.sun.corba.se.spi.transport.SocketInfo
            public String getHost() {
                return str;
            }

            @Override // com.sun.corba.se.spi.transport.SocketInfo, com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo
            public int getPort() {
                return i2;
            }
        };
    }
}
