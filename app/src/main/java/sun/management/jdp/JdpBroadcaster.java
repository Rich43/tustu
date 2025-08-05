package sun.management.jdp;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.UnsupportedAddressTypeException;

/* loaded from: rt.jar:sun/management/jdp/JdpBroadcaster.class */
public final class JdpBroadcaster {
    private final InetAddress addr;
    private final int port;
    private final DatagramChannel channel;

    public JdpBroadcaster(InetAddress inetAddress, InetAddress inetAddress2, int i2, int i3) throws JdpException, IOException {
        this.addr = inetAddress;
        this.port = i2;
        this.channel = DatagramChannel.open(inetAddress instanceof Inet6Address ? StandardProtocolFamily.INET6 : StandardProtocolFamily.INET);
        this.channel.setOption((SocketOption<SocketOption<Boolean>>) StandardSocketOptions.SO_REUSEADDR, (SocketOption<Boolean>) true);
        this.channel.setOption((SocketOption<SocketOption<Integer>>) StandardSocketOptions.IP_MULTICAST_TTL, (SocketOption<Integer>) Integer.valueOf(i3));
        if (inetAddress2 != null) {
            NetworkInterface byInetAddress = NetworkInterface.getByInetAddress(inetAddress2);
            try {
                this.channel.bind((SocketAddress) new InetSocketAddress(inetAddress2, 0));
                this.channel.setOption((SocketOption<SocketOption<NetworkInterface>>) StandardSocketOptions.IP_MULTICAST_IF, (SocketOption<NetworkInterface>) byInetAddress);
            } catch (UnsupportedAddressTypeException e2) {
                throw new JdpException("Unable to bind to source address");
            }
        }
    }

    public JdpBroadcaster(InetAddress inetAddress, int i2, int i3) throws JdpException, IOException {
        this(inetAddress, null, i2, i3);
    }

    public void sendPacket(JdpPacket jdpPacket) throws IOException {
        this.channel.send(ByteBuffer.wrap(jdpPacket.getPacketData()), new InetSocketAddress(this.addr, this.port));
    }

    public void shutdown() throws IOException {
        this.channel.close();
    }
}
