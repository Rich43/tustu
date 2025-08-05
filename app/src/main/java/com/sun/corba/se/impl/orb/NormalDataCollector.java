package com.sun.corba.se.impl.orb;

import java.util.Properties;

/* loaded from: rt.jar:com/sun/corba/se/impl/orb/NormalDataCollector.class */
public class NormalDataCollector extends DataCollectorBase {
    private String[] args;

    public NormalDataCollector(String[] strArr, Properties properties, String str, String str2) {
        super(properties, str, str2);
        this.args = strArr;
    }

    @Override // com.sun.corba.se.impl.orb.DataCollectorBase, com.sun.corba.se.spi.orb.DataCollector
    public boolean isApplet() {
        return false;
    }

    @Override // com.sun.corba.se.impl.orb.DataCollectorBase
    protected void collect() {
        checkPropertyDefaults();
        findPropertiesFromFile();
        findPropertiesFromSystem();
        findPropertiesFromProperties();
        findPropertiesFromArgs(this.args);
    }
}
