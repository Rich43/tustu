package com.sun.corba.se.spi.ior.iiop;

import com.sun.corba.se.spi.ior.TaggedProfileTemplate;

/* loaded from: rt.jar:com/sun/corba/se/spi/ior/iiop/IIOPProfileTemplate.class */
public interface IIOPProfileTemplate extends TaggedProfileTemplate {
    GIOPVersion getGIOPVersion();

    IIOPAddress getPrimaryAddress();
}
