package com.intel.bluetooth.obex;

import java.io.IOException;
import javax.microedition.io.Connection;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/obex/BlueCoveOBEX.class */
public abstract class BlueCoveOBEX {
    private BlueCoveOBEX() {
    }

    public static int getPacketSize(Connection c2) {
        if (c2 instanceof OBEXSessionBase) {
            return ((OBEXSessionBase) c2).getPacketSize();
        }
        throw new IllegalArgumentException(new StringBuffer().append("Not a BlueCove OBEX Session ").append(c2.getClass().getName()).toString());
    }

    public static void setPacketSize(Connection c2, int mtu) throws IOException {
        if (c2 instanceof OBEXSessionBase) {
            ((OBEXSessionBase) c2).setPacketSize(mtu);
            return;
        }
        throw new IllegalArgumentException(new StringBuffer().append("Not a BlueCove OBEX Session ").append(c2.getClass().getName()).toString());
    }

    public static String obexResponseCodes(int responseCode) {
        return OBEXUtils.toStringObexResponseCodes(responseCode);
    }
}
