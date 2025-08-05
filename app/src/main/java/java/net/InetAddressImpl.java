package java.net;

import java.io.IOException;

/* loaded from: rt.jar:java/net/InetAddressImpl.class */
interface InetAddressImpl {
    String getLocalHostName() throws UnknownHostException;

    InetAddress[] lookupAllHostAddr(String str) throws UnknownHostException;

    String getHostByAddr(byte[] bArr) throws UnknownHostException;

    InetAddress anyLocalAddress();

    InetAddress loopbackAddress();

    boolean isReachable(InetAddress inetAddress, int i2, NetworkInterface networkInterface, int i3) throws IOException;
}
