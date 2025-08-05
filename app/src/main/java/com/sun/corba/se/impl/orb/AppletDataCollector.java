package com.sun.corba.se.impl.orb;

import java.applet.Applet;
import java.util.Properties;

/* loaded from: rt.jar:com/sun/corba/se/impl/orb/AppletDataCollector.class */
public class AppletDataCollector extends DataCollectorBase {
    private Applet applet;

    AppletDataCollector(Applet applet, Properties properties, String str, String str2) {
        super(properties, str, str2);
        this.applet = applet;
    }

    @Override // com.sun.corba.se.impl.orb.DataCollectorBase, com.sun.corba.se.spi.orb.DataCollector
    public boolean isApplet() {
        return true;
    }

    @Override // com.sun.corba.se.impl.orb.DataCollectorBase
    protected void collect() {
        checkPropertyDefaults();
        findPropertiesFromFile();
        findPropertiesFromProperties();
        findPropertiesFromApplet(this.applet);
    }
}
