package sun.net;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.InetAddress;

/* loaded from: rt.jar:sun/net/NetHooks.class */
public final class NetHooks {
    public static void beforeTcpBind(FileDescriptor fileDescriptor, InetAddress inetAddress, int i2) throws IOException {
    }

    public static void beforeTcpConnect(FileDescriptor fileDescriptor, InetAddress inetAddress, int i2) throws IOException {
    }
}
