package sun.nio.ch;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketOption;
import java.net.SocketTimeoutException;
import java.net.StandardSocketOptions;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.IllegalBlockingModeException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/* loaded from: rt.jar:sun/nio/ch/ServerSocketAdaptor.class */
class ServerSocketAdaptor extends ServerSocket {
    private final ServerSocketChannelImpl ssc;
    private volatile int timeout = 0;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ServerSocketAdaptor.class.desiredAssertionStatus();
    }

    public static ServerSocket create(ServerSocketChannelImpl serverSocketChannelImpl) {
        try {
            return new ServerSocketAdaptor(serverSocketChannelImpl);
        } catch (IOException e2) {
            throw new Error(e2);
        }
    }

    private ServerSocketAdaptor(ServerSocketChannelImpl serverSocketChannelImpl) throws IOException {
        this.ssc = serverSocketChannelImpl;
    }

    @Override // java.net.ServerSocket
    public void bind(SocketAddress socketAddress) throws IOException {
        bind(socketAddress, 50);
    }

    @Override // java.net.ServerSocket
    public void bind(SocketAddress socketAddress, int i2) throws IOException {
        if (socketAddress == null) {
            socketAddress = new InetSocketAddress(0);
        }
        try {
            this.ssc.bind(socketAddress, i2);
        } catch (Exception e2) {
            Net.translateException(e2);
        }
    }

    @Override // java.net.ServerSocket
    public InetAddress getInetAddress() {
        if (!this.ssc.isBound()) {
            return null;
        }
        return Net.getRevealedLocalAddress(this.ssc.localAddress()).getAddress();
    }

    @Override // java.net.ServerSocket
    public int getLocalPort() {
        if (!this.ssc.isBound()) {
            return -1;
        }
        return Net.asInetSocketAddress(this.ssc.localAddress()).getPort();
    }

    @Override // java.net.ServerSocket
    public Socket accept() throws IOException {
        SocketChannel socketChannelAccept;
        synchronized (this.ssc.blockingLock()) {
            if (!this.ssc.isBound()) {
                throw new IllegalBlockingModeException();
            }
            try {
                if (this.timeout == 0) {
                    SocketChannel socketChannelAccept2 = this.ssc.accept();
                    if (socketChannelAccept2 == null && !this.ssc.isBlocking()) {
                        throw new IllegalBlockingModeException();
                    }
                    return socketChannelAccept2.socket();
                }
                if (!this.ssc.isBlocking()) {
                    throw new IllegalBlockingModeException();
                }
                this.ssc.configureBlocking(false);
                try {
                    SocketChannel socketChannelAccept3 = this.ssc.accept();
                    if (socketChannelAccept3 != null) {
                        return socketChannelAccept3.socket();
                    }
                    long jCurrentTimeMillis = this.timeout;
                    while (this.ssc.isOpen()) {
                        long jCurrentTimeMillis2 = System.currentTimeMillis();
                        if (this.ssc.poll(Net.POLLIN, jCurrentTimeMillis) > 0 && (socketChannelAccept = this.ssc.accept()) != null) {
                            Socket socket = socketChannelAccept.socket();
                            try {
                                this.ssc.configureBlocking(true);
                            } catch (ClosedChannelException e2) {
                            }
                            return socket;
                        }
                        jCurrentTimeMillis -= System.currentTimeMillis() - jCurrentTimeMillis2;
                        if (jCurrentTimeMillis <= 0) {
                            throw new SocketTimeoutException();
                        }
                    }
                    throw new ClosedChannelException();
                } finally {
                    try {
                        this.ssc.configureBlocking(true);
                    } catch (ClosedChannelException e3) {
                    }
                }
            } catch (Exception e4) {
                Net.translateException(e4);
                if ($assertionsDisabled) {
                    return null;
                }
                throw new AssertionError();
            }
        }
    }

    @Override // java.net.ServerSocket, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.ssc.close();
    }

    @Override // java.net.ServerSocket
    public ServerSocketChannel getChannel() {
        return this.ssc;
    }

    @Override // java.net.ServerSocket
    public boolean isBound() {
        return this.ssc.isBound();
    }

    @Override // java.net.ServerSocket
    public boolean isClosed() {
        return !this.ssc.isOpen();
    }

    @Override // java.net.ServerSocket
    public void setSoTimeout(int i2) throws SocketException {
        this.timeout = i2;
    }

    @Override // java.net.ServerSocket
    public int getSoTimeout() throws SocketException {
        return this.timeout;
    }

    @Override // java.net.ServerSocket
    public void setReuseAddress(boolean z2) throws SocketException {
        try {
            this.ssc.setOption((SocketOption<SocketOption<Boolean>>) StandardSocketOptions.SO_REUSEADDR, (SocketOption<Boolean>) Boolean.valueOf(z2));
        } catch (IOException e2) {
            Net.translateToSocketException(e2);
        }
    }

    @Override // java.net.ServerSocket
    public boolean getReuseAddress() throws SocketException {
        try {
            return ((Boolean) this.ssc.getOption(StandardSocketOptions.SO_REUSEADDR)).booleanValue();
        } catch (IOException e2) {
            Net.translateToSocketException(e2);
            return false;
        }
    }

    @Override // java.net.ServerSocket
    public String toString() {
        if (!isBound()) {
            return "ServerSocket[unbound]";
        }
        return "ServerSocket[addr=" + ((Object) getInetAddress()) + ",localport=" + getLocalPort() + "]";
    }

    @Override // java.net.ServerSocket
    public void setReceiveBufferSize(int i2) throws SocketException {
        if (i2 <= 0) {
            throw new IllegalArgumentException("size cannot be 0 or negative");
        }
        try {
            this.ssc.setOption((SocketOption<SocketOption<Integer>>) StandardSocketOptions.SO_RCVBUF, (SocketOption<Integer>) Integer.valueOf(i2));
        } catch (IOException e2) {
            Net.translateToSocketException(e2);
        }
    }

    @Override // java.net.ServerSocket
    public int getReceiveBufferSize() throws SocketException {
        try {
            return ((Integer) this.ssc.getOption(StandardSocketOptions.SO_RCVBUF)).intValue();
        } catch (IOException e2) {
            Net.translateToSocketException(e2);
            return -1;
        }
    }
}
