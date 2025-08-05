package org.apache.commons.net.telnet;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/telnet/EchoOptionHandler.class */
public class EchoOptionHandler extends TelnetOptionHandler {
    public EchoOptionHandler(boolean initlocal, boolean initremote, boolean acceptlocal, boolean acceptremote) {
        super(1, initlocal, initremote, acceptlocal, acceptremote);
    }

    public EchoOptionHandler() {
        super(1, false, false, false, false);
    }
}
