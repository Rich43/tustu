package com.sun.corba.se.spi.legacy.interceptor;

import com.sun.corba.se.spi.oa.ObjectAdapter;

/* loaded from: rt.jar:com/sun/corba/se/spi/legacy/interceptor/IORInfoExt.class */
public interface IORInfoExt {
    int getServerPort(String str) throws UnknownType;

    ObjectAdapter getObjectAdapter();
}
