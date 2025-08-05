package org.apache.commons.net.telnet;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/telnet/SuppressGAOptionHandler.class */
public class SuppressGAOptionHandler extends TelnetOptionHandler {
    public SuppressGAOptionHandler(boolean initlocal, boolean initremote, boolean acceptlocal, boolean acceptremote) {
        super(3, initlocal, initremote, acceptlocal, acceptremote);
    }

    public SuppressGAOptionHandler() {
        super(3, false, false, false, false);
    }
}
