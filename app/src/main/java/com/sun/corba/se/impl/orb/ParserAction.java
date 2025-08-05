package com.sun.corba.se.impl.orb;

import java.util.Properties;

/* loaded from: rt.jar:com/sun/corba/se/impl/orb/ParserAction.class */
public interface ParserAction {
    String getPropertyName();

    boolean isPrefix();

    String getFieldName();

    Object apply(Properties properties);
}
