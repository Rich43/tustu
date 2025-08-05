package java.nio.channels;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;

/* loaded from: rt.jar:java/nio/channels/MembershipKey.class */
public abstract class MembershipKey {
    public abstract boolean isValid();

    public abstract void drop();

    public abstract MembershipKey block(InetAddress inetAddress) throws IOException;

    public abstract MembershipKey unblock(InetAddress inetAddress);

    public abstract MulticastChannel channel();

    public abstract InetAddress group();

    public abstract NetworkInterface networkInterface();

    public abstract InetAddress sourceAddress();

    protected MembershipKey() {
    }
}
