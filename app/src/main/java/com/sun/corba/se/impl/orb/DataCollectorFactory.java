package com.sun.corba.se.impl.orb;

import com.sun.corba.se.spi.orb.DataCollector;
import java.applet.Applet;
import java.net.URL;
import java.util.Properties;

/* loaded from: rt.jar:com/sun/corba/se/impl/orb/DataCollectorFactory.class */
public abstract class DataCollectorFactory {
    private DataCollectorFactory() {
    }

    public static DataCollector create(Applet applet, Properties properties, String str) {
        URL codeBase;
        String host = str;
        if (applet != null && (codeBase = applet.getCodeBase()) != null) {
            host = codeBase.getHost();
        }
        return new AppletDataCollector(applet, properties, str, host);
    }

    public static DataCollector create(String[] strArr, Properties properties, String str) {
        return new NormalDataCollector(strArr, properties, str, str);
    }

    public static DataCollector create(Properties properties, String str) {
        return new PropertyOnlyDataCollector(properties, str, str);
    }
}
