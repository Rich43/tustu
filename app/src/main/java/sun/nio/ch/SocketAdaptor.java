package sun.nio.ch;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;
import java.net.SocketOption;
import java.net.SocketTimeoutException;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.IllegalBlockingModeException;
import java.nio.channels.SocketChannel;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

/* loaded from: rt.jar:sun/nio/ch/SocketAdaptor.class */
class SocketAdaptor extends Socket {
    private final SocketChannelImpl sc;
    private volatile int timeout;
    private InputStream socketInputStream;

    private SocketAdaptor(SocketChannelImpl socketChannelImpl) throws SocketException {
        super((SocketImpl) null);
        this.timeout = 0;
        this.socketInputStream = null;
        this.sc = socketChannelImpl;
    }

    public static Socket create(SocketChannelImpl socketChannelImpl) {
        try {
            return new SocketAdaptor(socketChannelImpl);
        } catch (SocketException e2) {
            throw new InternalError("Should not reach here");
        }
    }

    @Override // java.net.Socket
    public SocketChannel getChannel() {
        return this.sc;
    }

    @Override // java.net.Socket
    public void connect(SocketAddress socketAddress) throws IOException {
        connect(socketAddress, 0);
    }

    @Override // java.net.Socket
    public void connect(SocketAddress socketAddress, int i2) throws IOException {
        if (socketAddress == null) {
            throw new IllegalArgumentException("connect: The address can't be null");
        }
        if (i2 < 0) {
            throw new IllegalArgumentException("connect: timeout can't be negative");
        }
        synchronized (this.sc.blockingLock()) {
            if (!this.sc.isBlocking()) {
                throw new IllegalBlockingModeException();
            }
            try {
            } catch (Exception e2) {
                Net.translateException(e2, true);
            }
            if (i2 == 0) {
                this.sc.connect(socketAddress);
                return;
            }
            this.sc.configureBlocking(false);
            try {
                if (this.sc.connect(socketAddress)) {
                    return;
                }
                long jCurrentTimeMillis = i2;
                while (this.sc.isOpen()) {
                    long jCurrentTimeMillis2 = System.currentTimeMillis();
                    if (this.sc.poll(Net.POLLCONN, jCurrentTimeMillis) <= 0 || !this.sc.finishConnect()) {
                        jCurrentTimeMillis -= System.currentTimeMillis() - jCurrentTimeMillis2;
                        if (jCurrentTimeMillis <= 0) {
                            try {
                                this.sc.close();
                            } catch (IOException e3) {
                            }
                            throw new SocketTimeoutException();
                        }
                    } else {
                        try {
                            this.sc.configureBlocking(true);
                        } catch (ClosedChannelException e4) {
                        }
                        return;
                    }
                }
                throw new ClosedChannelException();
            } finally {
                try {
                    this.sc.configureBlocking(true);
                } catch (ClosedChannelException e5) {
                }
            }
        }
    }

    @Override // java.net.Socket
    public void bind(SocketAddress socketAddress) throws IOException {
        try {
            this.sc.bind(socketAddress);
        } catch (Exception e2) {
            Net.translateException(e2);
        }
    }

    @Override // java.net.Socket
    public InetAddress getInetAddress() {
        SocketAddress socketAddressRemoteAddress = this.sc.remoteAddress();
        if (socketAddressRemoteAddress == null) {
            return null;
        }
        return ((InetSocketAddress) socketAddressRemoteAddress).getAddress();
    }

    @Override // java.net.Socket
    public InetAddress getLocalAddress() {
        InetSocketAddress inetSocketAddressLocalAddress;
        if (this.sc.isOpen() && (inetSocketAddressLocalAddress = this.sc.localAddress()) != null) {
            return Net.getRevealedLocalAddress(inetSocketAddressLocalAddress).getAddress();
        }
        return new InetSocketAddress(0).getAddress();
    }

    @Override // java.net.Socket
    public int getPort() {
        SocketAddress socketAddressRemoteAddress = this.sc.remoteAddress();
        if (socketAddressRemoteAddress == null) {
            return 0;
        }
        return ((InetSocketAddress) socketAddressRemoteAddress).getPort();
    }

    @Override // java.net.Socket
    public int getLocalPort() {
        InetSocketAddress inetSocketAddressLocalAddress = this.sc.localAddress();
        if (inetSocketAddressLocalAddress == null) {
            return -1;
        }
        return inetSocketAddressLocalAddress.getPort();
    }

    /* loaded from: rt.jar:sun/nio/ch/SocketAdaptor$SocketInputStream.class */
    private class SocketInputStream extends ChannelInputStream {
        private SocketInputStream() {
            super(SocketAdaptor.this.sc);
        }

        @Override // sun.nio.ch.ChannelInputStream
        protected int read(ByteBuffer byteBuffer) throws IOException {
            int i2;
            synchronized (SocketAdaptor.this.sc.blockingLock()) {
                if (SocketAdaptor.this.sc.isOpen()) {
                    if (SocketAdaptor.this.sc.isBlocking()) {
                        if (SocketAdaptor.this.timeout == 0) {
                            return SocketAdaptor.this.sc.read(byteBuffer);
                        }
                        SocketAdaptor.this.sc.configureBlocking(false);
                        try {
                            int i3 = SocketAdaptor.this.sc.read(byteBuffer);
                            if (i3 == 0) {
                                long jCurrentTimeMillis = SocketAdaptor.this.timeout;
                                while (SocketAdaptor.this.sc.isOpen()) {
                                    long jCurrentTimeMillis2 = System.currentTimeMillis();
                                    if (SocketAdaptor.this.sc.poll(Net.POLLIN, jCurrentTimeMillis) > 0 && (i2 = SocketAdaptor.this.sc.read(byteBuffer)) != 0) {
                                        try {
                                            SocketAdaptor.this.sc.configureBlocking(true);
                                        } catch (ClosedChannelException e2) {
                                        }
                                        return i2;
                                    }
                                    jCurrentTimeMillis -= System.currentTimeMillis() - jCurrentTimeMillis2;
                                    if (jCurrentTimeMillis <= 0) {
                                        throw new SocketTimeoutException();
                                    }
                                }
                                throw new ClosedChannelException();
                            }
                            return i3;
                        } finally {
                            try {
                                SocketAdaptor.this.sc.configureBlocking(true);
                            } catch (ClosedChannelException e3) {
                            }
                        }
                    }
                    throw new IllegalBlockingModeException();
                }
                throw new ClosedChannelException();
            }
        }
    }

    @Override // java.net.Socket
    public InputStream getInputStream() throws IOException {
        if (!this.sc.isOpen()) {
            throw new SocketException("Socket is closed");
        }
        if (!this.sc.isConnected()) {
            throw new SocketException("Socket is not connected");
        }
        if (!this.sc.isInputOpen()) {
            throw new SocketException("Socket input is shutdown");
        }
        if (this.socketInputStream == null) {
            try {
                this.socketInputStream = (InputStream) AccessController.doPrivileged(new PrivilegedExceptionAction<InputStream>() { // from class: sun.nio.ch.SocketAdaptor.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public InputStream run() throws IOException {
                        return new SocketInputStream();
                    }
                });
            } catch (PrivilegedActionException e2) {
                throw ((IOException) e2.getException());
            }
        }
        return this.socketInputStream;
    }

    @Override // java.net.Socket
    public OutputStream getOutputStream() throws IOException {
        if (!this.sc.isOpen()) {
            throw new SocketException("Socket is closed");
        }
        if (!this.sc.isConnected()) {
            throw new SocketException("Socket is not connected");
        }
        if (!this.sc.isOutputOpen()) {
            throw new SocketException("Socket output is shutdown");
        }
        try {
            return (OutputStream) AccessController.doPrivileged(new PrivilegedExceptionAction<OutputStream>() { // from class: sun.nio.ch.SocketAdaptor.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public OutputStream run() throws IOException {
                    return Channels.newOutputStream(SocketAdaptor.this.sc);
                }
            });
        } catch (PrivilegedActionException e2) {
            throw ((IOException) e2.getException());
        }
    }

    private void setBooleanOption(SocketOption<Boolean> socketOption, boolean z2) throws SocketException {
        try {
            this.sc.setOption((SocketOption<SocketOption<Boolean>>) socketOption, (SocketOption<Boolean>) Boolean.valueOf(z2));
        } catch (IOException e2) {
            Net.translateToSocketException(e2);
        }
    }

    private void setIntOption(SocketOption<Integer> socketOption, int i2) throws SocketException {
        try {
            this.sc.setOption((SocketOption<SocketOption<Integer>>) socketOption, (SocketOption<Integer>) Integer.valueOf(i2));
        } catch (IOException e2) {
            Net.translateToSocketException(e2);
        }
    }

    private boolean getBooleanOption(SocketOption<Boolean> socketOption) throws SocketException {
        try {
            return ((Boolean) this.sc.getOption(socketOption)).booleanValue();
        } catch (IOException e2) {
            Net.translateToSocketException(e2);
            return false;
        }
    }

    private int getIntOption(SocketOption<Integer> socketOption) throws SocketException {
        try {
            return ((Integer) this.sc.getOption(socketOption)).intValue();
        } catch (IOException e2) {
            Net.translateToSocketException(e2);
            return -1;
        }
    }

    @Override // java.net.Socket
    public void setTcpNoDelay(boolean z2) throws SocketException {
        setBooleanOption(StandardSocketOptions.TCP_NODELAY, z2);
    }

    @Override // java.net.Socket
    public boolean getTcpNoDelay() throws SocketException {
        return getBooleanOption(StandardSocketOptions.TCP_NODELAY);
    }

    @Override // java.net.Socket
    public void setSoLinger(boolean z2, int i2) throws SocketException {
        if (!z2) {
            i2 = -1;
        }
        setIntOption(StandardSocketOptions.SO_LINGER, i2);
    }

    @Override // java.net.Socket
    public int getSoLinger() throws SocketException {
        return getIntOption(StandardSocketOptions.SO_LINGER);
    }

    @Override // java.net.Socket
    public void sendUrgentData(int i2) throws IOException {
        if (this.sc.sendOutOfBandData((byte) i2) == 0) {
            throw new IOException("Socket buffer full");
        }
    }

    @Override // java.net.Socket
    public void setOOBInline(boolean z2) throws SocketException {
        setBooleanOption(ExtendedSocketOption.SO_OOBINLINE, z2);
    }

    @Override // java.net.Socket
    public boolean getOOBInline() throws SocketException {
        return getBooleanOption(ExtendedSocketOption.SO_OOBINLINE);
    }

    @Override // java.net.Socket
    public void setSoTimeout(int i2) throws SocketException {
        if (i2 < 0) {
            throw new IllegalArgumentException("timeout can't be negative");
        }
        this.timeout = i2;
    }

    @Override // java.net.Socket
    public int getSoTimeout() throws SocketException {
        return this.timeout;
    }

    @Override // java.net.Socket
    public void setSendBufferSize(int i2) throws SocketException {
        if (i2 <= 0) {
            throw new IllegalArgumentException("Invalid send size");
        }
        setIntOption(StandardSocketOptions.SO_SNDBUF, i2);
    }

    @Override // java.net.Socket
    public int getSendBufferSize() throws SocketException {
        return getIntOption(StandardSocketOptions.SO_SNDBUF);
    }

    @Override // java.net.Socket
    public void setReceiveBufferSize(int i2) throws SocketException {
        if (i2 <= 0) {
            throw new IllegalArgumentException("Invalid receive size");
        }
        setIntOption(StandardSocketOptions.SO_RCVBUF, i2);
    }

    @Override // java.net.Socket
    public int getReceiveBufferSize() throws SocketException {
        return getIntOption(StandardSocketOptions.SO_RCVBUF);
    }

    @Override // java.net.Socket
    public void setKeepAlive(boolean z2) throws SocketException {
        setBooleanOption(StandardSocketOptions.SO_KEEPALIVE, z2);
    }

    @Override // java.net.Socket
    public boolean getKeepAlive() throws SocketException {
        return getBooleanOption(StandardSocketOptions.SO_KEEPALIVE);
    }

    @Override // java.net.Socket
    public void setTrafficClass(int i2) throws SocketException {
        setIntOption(StandardSocketOptions.IP_TOS, i2);
    }

    @Override // java.net.Socket
    public int getTrafficClass() throws SocketException {
        return getIntOption(StandardSocketOptions.IP_TOS);
    }

    @Override // java.net.Socket
    public void setReuseAddress(boolean z2) throws SocketException {
        setBooleanOption(StandardSocketOptions.SO_REUSEADDR, z2);
    }

    @Override // java.net.Socket
    public boolean getReuseAddress() throws SocketException {
        return getBooleanOption(StandardSocketOptions.SO_REUSEADDR);
    }

    @Override // java.net.Socket, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.sc.close();
    }

    @Override // java.net.Socket
    public void shutdownInput() throws IOException {
        try {
            this.sc.shutdownInput();
        } catch (Exception e2) {
            Net.translateException(e2);
        }
    }

    @Override // java.net.Socket
    public void shutdownOutput() throws IOException {
        try {
            this.sc.shutdownOutput();
        } catch (Exception e2) {
            Net.translateException(e2);
        }
    }

    @Override // java.net.Socket
    public String toString() {
        if (this.sc.isConnected()) {
            return "Socket[addr=" + ((Object) getInetAddress()) + ",port=" + getPort() + ",localport=" + getLocalPort() + "]";
        }
        return "Socket[unconnected]";
    }

    @Override // java.net.Socket
    public boolean isConnected() {
        return this.sc.isConnected();
    }

    @Override // java.net.Socket
    public boolean isBound() {
        return this.sc.localAddress() != null;
    }

    @Override // java.net.Socket
    public boolean isClosed() {
        return !this.sc.isOpen();
    }

    @Override // java.net.Socket
    public boolean isInputShutdown() {
        return !this.sc.isInputOpen();
    }

    @Override // java.net.Socket
    public boolean isOutputShutdown() {
        return !this.sc.isOutputOpen();
    }
}
