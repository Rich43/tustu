package sun.nio.ch;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.DatagramSocketImpl;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketOption;
import java.net.SocketTimeoutException;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.IllegalBlockingModeException;

/* loaded from: rt.jar:sun/nio/ch/DatagramSocketAdaptor.class */
public class DatagramSocketAdaptor extends DatagramSocket {
    private final DatagramChannelImpl dc;
    private volatile int timeout;
    private static final DatagramSocketImpl dummyDatagramSocket = new DatagramSocketImpl() { // from class: sun.nio.ch.DatagramSocketAdaptor.1
        @Override // java.net.DatagramSocketImpl
        protected void create() throws SocketException {
        }

        @Override // java.net.DatagramSocketImpl
        protected void bind(int i2, InetAddress inetAddress) throws SocketException {
        }

        @Override // java.net.DatagramSocketImpl
        protected void send(DatagramPacket datagramPacket) throws IOException {
        }

        @Override // java.net.DatagramSocketImpl
        protected int peek(InetAddress inetAddress) throws IOException {
            return 0;
        }

        @Override // java.net.DatagramSocketImpl
        protected int peekData(DatagramPacket datagramPacket) throws IOException {
            return 0;
        }

        @Override // java.net.DatagramSocketImpl
        protected void receive(DatagramPacket datagramPacket) throws IOException {
        }

        @Override // java.net.DatagramSocketImpl
        @Deprecated
        protected void setTTL(byte b2) throws IOException {
        }

        @Override // java.net.DatagramSocketImpl
        @Deprecated
        protected byte getTTL() throws IOException {
            return (byte) 0;
        }

        @Override // java.net.DatagramSocketImpl
        protected void setTimeToLive(int i2) throws IOException {
        }

        @Override // java.net.DatagramSocketImpl
        protected int getTimeToLive() throws IOException {
            return 0;
        }

        @Override // java.net.DatagramSocketImpl
        protected void join(InetAddress inetAddress) throws IOException {
        }

        @Override // java.net.DatagramSocketImpl
        protected void leave(InetAddress inetAddress) throws IOException {
        }

        @Override // java.net.DatagramSocketImpl
        protected void joinGroup(SocketAddress socketAddress, NetworkInterface networkInterface) throws IOException {
        }

        @Override // java.net.DatagramSocketImpl
        protected void leaveGroup(SocketAddress socketAddress, NetworkInterface networkInterface) throws IOException {
        }

        @Override // java.net.DatagramSocketImpl
        protected void close() {
        }

        @Override // java.net.SocketOptions
        public Object getOption(int i2) throws SocketException {
            return null;
        }

        @Override // java.net.SocketOptions
        public void setOption(int i2, Object obj) throws SocketException {
        }
    };

    private DatagramSocketAdaptor(DatagramChannelImpl datagramChannelImpl) throws IOException {
        super(dummyDatagramSocket);
        this.timeout = 0;
        this.dc = datagramChannelImpl;
    }

    public static DatagramSocket create(DatagramChannelImpl datagramChannelImpl) {
        try {
            return new DatagramSocketAdaptor(datagramChannelImpl);
        } catch (IOException e2) {
            throw new Error(e2);
        }
    }

    private void connectInternal(SocketAddress socketAddress) throws SocketException {
        int port = Net.asInetSocketAddress(socketAddress).getPort();
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("connect: " + port);
        }
        if (socketAddress == null) {
            throw new IllegalArgumentException("connect: null address");
        }
        if (isClosed()) {
            return;
        }
        try {
            this.dc.connect(socketAddress);
        } catch (Exception e2) {
            Net.translateToSocketException(e2);
        }
    }

    @Override // java.net.DatagramSocket
    public void bind(SocketAddress socketAddress) throws SocketException {
        if (socketAddress == null) {
            try {
                socketAddress = new InetSocketAddress(0);
            } catch (Exception e2) {
                Net.translateToSocketException(e2);
                return;
            }
        }
        this.dc.bind(socketAddress);
    }

    @Override // java.net.DatagramSocket
    public void connect(InetAddress inetAddress, int i2) {
        try {
            connectInternal(new InetSocketAddress(inetAddress, i2));
        } catch (SocketException e2) {
        }
    }

    @Override // java.net.DatagramSocket
    public void connect(SocketAddress socketAddress) throws SocketException {
        if (socketAddress == null) {
            throw new IllegalArgumentException("Address can't be null");
        }
        connectInternal(socketAddress);
    }

    @Override // java.net.DatagramSocket
    public void disconnect() {
        try {
            this.dc.disconnect();
        } catch (IOException e2) {
            throw new Error(e2);
        }
    }

    @Override // java.net.DatagramSocket
    public boolean isBound() {
        return this.dc.localAddress() != null;
    }

    @Override // java.net.DatagramSocket
    public boolean isConnected() {
        return this.dc.remoteAddress() != null;
    }

    @Override // java.net.DatagramSocket
    public InetAddress getInetAddress() {
        if (isConnected()) {
            return Net.asInetSocketAddress(this.dc.remoteAddress()).getAddress();
        }
        return null;
    }

    @Override // java.net.DatagramSocket
    public int getPort() {
        if (isConnected()) {
            return Net.asInetSocketAddress(this.dc.remoteAddress()).getPort();
        }
        return -1;
    }

    @Override // java.net.DatagramSocket
    public void send(DatagramPacket datagramPacket) throws IOException {
        synchronized (this.dc.blockingLock()) {
            if (!this.dc.isBlocking()) {
                throw new IllegalBlockingModeException();
            }
            try {
                synchronized (datagramPacket) {
                    ByteBuffer byteBufferWrap = ByteBuffer.wrap(datagramPacket.getData(), datagramPacket.getOffset(), datagramPacket.getLength());
                    if (this.dc.isConnected() && datagramPacket.getAddress() == null) {
                        InetSocketAddress inetSocketAddress = (InetSocketAddress) this.dc.remoteAddress();
                        datagramPacket.setPort(inetSocketAddress.getPort());
                        datagramPacket.setAddress(inetSocketAddress.getAddress());
                        this.dc.write(byteBufferWrap);
                    } else {
                        this.dc.send(byteBufferWrap, datagramPacket.getSocketAddress());
                    }
                }
            } catch (IOException e2) {
                Net.translateException(e2);
            }
        }
    }

    private SocketAddress receive(ByteBuffer byteBuffer) throws IOException {
        SocketAddress socketAddressReceive;
        if (this.timeout == 0) {
            return this.dc.receive(byteBuffer);
        }
        this.dc.configureBlocking(false);
        try {
            SocketAddress socketAddressReceive2 = this.dc.receive(byteBuffer);
            if (socketAddressReceive2 != null) {
                return socketAddressReceive2;
            }
            long jCurrentTimeMillis = this.timeout;
            while (this.dc.isOpen()) {
                long jCurrentTimeMillis2 = System.currentTimeMillis();
                int iPoll = this.dc.poll(Net.POLLIN, jCurrentTimeMillis);
                if (iPoll <= 0 || (iPoll & Net.POLLIN) == 0 || (socketAddressReceive = this.dc.receive(byteBuffer)) == null) {
                    jCurrentTimeMillis -= System.currentTimeMillis() - jCurrentTimeMillis2;
                    if (jCurrentTimeMillis <= 0) {
                        throw new SocketTimeoutException();
                    }
                } else {
                    try {
                        this.dc.configureBlocking(true);
                    } catch (ClosedChannelException e2) {
                    }
                    return socketAddressReceive;
                }
            }
            throw new ClosedChannelException();
        } finally {
            try {
                this.dc.configureBlocking(true);
            } catch (ClosedChannelException e3) {
            }
        }
    }

    @Override // java.net.DatagramSocket
    public void receive(DatagramPacket datagramPacket) throws IOException {
        synchronized (this.dc.blockingLock()) {
            if (!this.dc.isBlocking()) {
                throw new IllegalBlockingModeException();
            }
            try {
                synchronized (datagramPacket) {
                    ByteBuffer byteBufferWrap = ByteBuffer.wrap(datagramPacket.getData(), datagramPacket.getOffset(), datagramPacket.getLength());
                    datagramPacket.setSocketAddress(receive(byteBufferWrap));
                    datagramPacket.setLength(byteBufferWrap.position() - datagramPacket.getOffset());
                }
            } catch (IOException e2) {
                Net.translateException(e2);
            }
        }
    }

    @Override // java.net.DatagramSocket
    public InetAddress getLocalAddress() {
        if (isClosed()) {
            return null;
        }
        SocketAddress socketAddressLocalAddress = this.dc.localAddress();
        if (socketAddressLocalAddress == null) {
            socketAddressLocalAddress = new InetSocketAddress(0);
        }
        InetAddress address = ((InetSocketAddress) socketAddressLocalAddress).getAddress();
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            try {
                securityManager.checkConnect(address.getHostAddress(), -1);
            } catch (SecurityException e2) {
                return new InetSocketAddress(0).getAddress();
            }
        }
        return address;
    }

    @Override // java.net.DatagramSocket
    public int getLocalPort() {
        if (isClosed()) {
            return -1;
        }
        try {
            SocketAddress localAddress = this.dc.getLocalAddress();
            if (localAddress != null) {
                return ((InetSocketAddress) localAddress).getPort();
            }
            return 0;
        } catch (Exception e2) {
            return 0;
        }
    }

    @Override // java.net.DatagramSocket
    public void setSoTimeout(int i2) throws SocketException {
        this.timeout = i2;
    }

    @Override // java.net.DatagramSocket
    public int getSoTimeout() throws SocketException {
        return this.timeout;
    }

    private void setBooleanOption(SocketOption<Boolean> socketOption, boolean z2) throws SocketException {
        try {
            this.dc.setOption((SocketOption<SocketOption<Boolean>>) socketOption, (SocketOption<Boolean>) Boolean.valueOf(z2));
        } catch (IOException e2) {
            Net.translateToSocketException(e2);
        }
    }

    private void setIntOption(SocketOption<Integer> socketOption, int i2) throws SocketException {
        try {
            this.dc.setOption((SocketOption<SocketOption<Integer>>) socketOption, (SocketOption<Integer>) Integer.valueOf(i2));
        } catch (IOException e2) {
            Net.translateToSocketException(e2);
        }
    }

    private boolean getBooleanOption(SocketOption<Boolean> socketOption) throws SocketException {
        try {
            return ((Boolean) this.dc.getOption(socketOption)).booleanValue();
        } catch (IOException e2) {
            Net.translateToSocketException(e2);
            return false;
        }
    }

    private int getIntOption(SocketOption<Integer> socketOption) throws SocketException {
        try {
            return ((Integer) this.dc.getOption(socketOption)).intValue();
        } catch (IOException e2) {
            Net.translateToSocketException(e2);
            return -1;
        }
    }

    @Override // java.net.DatagramSocket
    public void setSendBufferSize(int i2) throws SocketException {
        if (i2 <= 0) {
            throw new IllegalArgumentException("Invalid send size");
        }
        setIntOption(StandardSocketOptions.SO_SNDBUF, i2);
    }

    @Override // java.net.DatagramSocket
    public int getSendBufferSize() throws SocketException {
        return getIntOption(StandardSocketOptions.SO_SNDBUF);
    }

    @Override // java.net.DatagramSocket
    public void setReceiveBufferSize(int i2) throws SocketException {
        if (i2 <= 0) {
            throw new IllegalArgumentException("Invalid receive size");
        }
        setIntOption(StandardSocketOptions.SO_RCVBUF, i2);
    }

    @Override // java.net.DatagramSocket
    public int getReceiveBufferSize() throws SocketException {
        return getIntOption(StandardSocketOptions.SO_RCVBUF);
    }

    @Override // java.net.DatagramSocket
    public void setReuseAddress(boolean z2) throws SocketException {
        setBooleanOption(StandardSocketOptions.SO_REUSEADDR, z2);
    }

    @Override // java.net.DatagramSocket
    public boolean getReuseAddress() throws SocketException {
        return getBooleanOption(StandardSocketOptions.SO_REUSEADDR);
    }

    @Override // java.net.DatagramSocket
    public void setBroadcast(boolean z2) throws SocketException {
        setBooleanOption(StandardSocketOptions.SO_BROADCAST, z2);
    }

    @Override // java.net.DatagramSocket
    public boolean getBroadcast() throws SocketException {
        return getBooleanOption(StandardSocketOptions.SO_BROADCAST);
    }

    @Override // java.net.DatagramSocket
    public void setTrafficClass(int i2) throws SocketException {
        setIntOption(StandardSocketOptions.IP_TOS, i2);
    }

    @Override // java.net.DatagramSocket
    public int getTrafficClass() throws SocketException {
        return getIntOption(StandardSocketOptions.IP_TOS);
    }

    @Override // java.net.DatagramSocket, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        try {
            this.dc.close();
        } catch (IOException e2) {
            throw new Error(e2);
        }
    }

    @Override // java.net.DatagramSocket
    public boolean isClosed() {
        return !this.dc.isOpen();
    }

    @Override // java.net.DatagramSocket
    public DatagramChannel getChannel() {
        return this.dc;
    }
}
