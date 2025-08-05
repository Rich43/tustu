package sun.net.spi.nameservice;

import java.net.InetAddress;
import java.net.UnknownHostException;

/* loaded from: rt.jar:sun/net/spi/nameservice/NameService.class */
public interface NameService {
    InetAddress[] lookupAllHostAddr(String str) throws UnknownHostException;

    String getHostByAddr(byte[] bArr) throws UnknownHostException;
}
