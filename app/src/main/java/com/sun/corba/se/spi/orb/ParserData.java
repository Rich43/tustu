package com.sun.corba.se.spi.orb;

import java.util.Properties;

/* loaded from: rt.jar:com/sun/corba/se/spi/orb/ParserData.class */
public interface ParserData {
    String getPropertyName();

    Operation getOperation();

    String getFieldName();

    Object getDefaultValue();

    Object getTestValue();

    void addToParser(PropertyParser propertyParser);

    void addToProperties(Properties properties);
}
