package com.sun.corba.se.impl.orb;

import java.util.Properties;

/* loaded from: rt.jar:com/sun/corba/se/impl/orb/PropertyOnlyDataCollector.class */
public class PropertyOnlyDataCollector extends DataCollectorBase {
    public PropertyOnlyDataCollector(Properties properties, String str, String str2) {
        super(properties, str, str2);
    }

    @Override // com.sun.corba.se.impl.orb.DataCollectorBase, com.sun.corba.se.spi.orb.DataCollector
    public boolean isApplet() {
        return false;
    }

    @Override // com.sun.corba.se.impl.orb.DataCollectorBase
    protected void collect() {
        checkPropertyDefaults();
        findPropertiesFromProperties();
    }
}
