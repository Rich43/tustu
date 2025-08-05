package java.nio.channels;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;

/* loaded from: rt.jar:java/nio/channels/MulticastChannel.class */
public interface MulticastChannel extends NetworkChannel {
    @Override // java.nio.channels.Channel, java.io.Closeable, java.lang.AutoCloseable
    void close() throws IOException;

    MembershipKey join(InetAddress inetAddress, NetworkInterface networkInterface) throws IOException;

    MembershipKey join(InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2) throws IOException;
}
