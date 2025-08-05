package org.apache.commons.net.telnet;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/telnet/TelnetNotificationHandler.class */
public interface TelnetNotificationHandler {
    public static final int RECEIVED_DO = 1;
    public static final int RECEIVED_DONT = 2;
    public static final int RECEIVED_WILL = 3;
    public static final int RECEIVED_WONT = 4;
    public static final int RECEIVED_COMMAND = 5;

    void receivedNegotiation(int i2, int i3);
}
