package sun.nio.ch;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.channels.MembershipKey;
import java.nio.channels.MulticastChannel;
import java.util.HashSet;

/* loaded from: rt.jar:sun/nio/ch/MembershipKeyImpl.class */
class MembershipKeyImpl extends MembershipKey {
    private final MulticastChannel ch;
    private final InetAddress group;
    private final NetworkInterface interf;
    private final InetAddress source;
    private volatile boolean valid;
    private Object stateLock;
    private HashSet<InetAddress> blockedSet;

    private MembershipKeyImpl(MulticastChannel multicastChannel, InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2) {
        this.valid = true;
        this.stateLock = new Object();
        this.ch = multicastChannel;
        this.group = inetAddress;
        this.interf = networkInterface;
        this.source = inetAddress2;
    }

    /* loaded from: rt.jar:sun/nio/ch/MembershipKeyImpl$Type4.class */
    static class Type4 extends MembershipKeyImpl {
        private final int groupAddress;
        private final int interfAddress;
        private final int sourceAddress;

        Type4(MulticastChannel multicastChannel, InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2, int i2, int i3, int i4) {
            super(multicastChannel, inetAddress, networkInterface, inetAddress2);
            this.groupAddress = i2;
            this.interfAddress = i3;
            this.sourceAddress = i4;
        }

        int groupAddress() {
            return this.groupAddress;
        }

        int interfaceAddress() {
            return this.interfAddress;
        }

        int source() {
            return this.sourceAddress;
        }
    }

    /* loaded from: rt.jar:sun/nio/ch/MembershipKeyImpl$Type6.class */
    static class Type6 extends MembershipKeyImpl {
        private final byte[] groupAddress;
        private final int index;
        private final byte[] sourceAddress;

        Type6(MulticastChannel multicastChannel, InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2, byte[] bArr, int i2, byte[] bArr2) {
            super(multicastChannel, inetAddress, networkInterface, inetAddress2);
            this.groupAddress = bArr;
            this.index = i2;
            this.sourceAddress = bArr2;
        }

        byte[] groupAddress() {
            return this.groupAddress;
        }

        int index() {
            return this.index;
        }

        byte[] source() {
            return this.sourceAddress;
        }
    }

    @Override // java.nio.channels.MembershipKey
    public boolean isValid() {
        return this.valid;
    }

    void invalidate() {
        this.valid = false;
    }

    @Override // java.nio.channels.MembershipKey
    public void drop() {
        ((DatagramChannelImpl) this.ch).drop(this);
    }

    @Override // java.nio.channels.MembershipKey
    public MulticastChannel channel() {
        return this.ch;
    }

    @Override // java.nio.channels.MembershipKey
    public InetAddress group() {
        return this.group;
    }

    @Override // java.nio.channels.MembershipKey
    public NetworkInterface networkInterface() {
        return this.interf;
    }

    @Override // java.nio.channels.MembershipKey
    public InetAddress sourceAddress() {
        return this.source;
    }

    @Override // java.nio.channels.MembershipKey
    public MembershipKey block(InetAddress inetAddress) throws IOException {
        if (this.source != null) {
            throw new IllegalStateException("key is source-specific");
        }
        synchronized (this.stateLock) {
            if (this.blockedSet != null && this.blockedSet.contains(inetAddress)) {
                return this;
            }
            ((DatagramChannelImpl) this.ch).block(this, inetAddress);
            if (this.blockedSet == null) {
                this.blockedSet = new HashSet<>();
            }
            this.blockedSet.add(inetAddress);
            return this;
        }
    }

    @Override // java.nio.channels.MembershipKey
    public MembershipKey unblock(InetAddress inetAddress) {
        synchronized (this.stateLock) {
            if (this.blockedSet == null || !this.blockedSet.contains(inetAddress)) {
                throw new IllegalStateException("not blocked");
            }
            ((DatagramChannelImpl) this.ch).unblock(this, inetAddress);
            this.blockedSet.remove(inetAddress);
        }
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        sb.append('<');
        sb.append(this.group.getHostAddress());
        sb.append(',');
        sb.append(this.interf.getName());
        if (this.source != null) {
            sb.append(',');
            sb.append(this.source.getHostAddress());
        }
        sb.append('>');
        return sb.toString();
    }
}
