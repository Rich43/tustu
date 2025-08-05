package com.sun.cdc.io.j2me.btspp;

import com.intel.bluetooth.MicroeditionConnector;
import com.sun.cdc.io.ConnectionBaseInterface;
import java.io.IOException;
import javax.microedition.io.Connection;

/* loaded from: bluecove-2.1.1.jar:com/sun/cdc/io/j2me/btspp/Protocol.class */
public class Protocol implements ConnectionBaseInterface {
    public Connection openPrim(String name, int mode, boolean timeouts) throws IOException {
        return MicroeditionConnector.open(new StringBuffer().append("btspp:").append(name).toString(), mode, timeouts);
    }
}
