package com.sun.corba.se.impl.naming.namingutil;

import java.util.List;

/* loaded from: rt.jar:com/sun/corba/se/impl/naming/namingutil/INSURL.class */
public interface INSURL {
    boolean getRIRFlag();

    List getEndpointInfo();

    String getKeyString();

    String getStringifiedName();

    boolean isCorbanameURL();

    void dPrint();
}
