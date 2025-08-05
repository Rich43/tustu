package sun.security.ssl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.SocketChannel;
import javax.net.ssl.SSLSocket;

/* loaded from: jsse.jar:sun/security/ssl/BaseSSLSocketImpl.class */
abstract class BaseSSLSocketImpl extends SSLSocket {
    private final Socket self;
    private final InputStream consumedInput;
    private static final String PROP_NAME = "com.sun.net.ssl.requireCloseNotify";
    static final boolean requireCloseNotify = Utilities.getBooleanProperty(PROP_NAME, false);

    BaseSSLSocketImpl() {
        this.self = this;
        this.consumedInput = null;
    }

    BaseSSLSocketImpl(Socket socket) {
        this.self = socket;
        this.consumedInput = null;
    }

    BaseSSLSocketImpl(Socket socket, InputStream inputStream) {
        this.self = socket;
        this.consumedInput = inputStream;
    }

    @Override // java.net.Socket
    public final SocketChannel getChannel() {
        if (this.self == this) {
            return super.getChannel();
        }
        return this.self.getChannel();
    }

    @Override // java.net.Socket
    public void bind(SocketAddress socketAddress) throws IOException {
        if (this.self == this) {
            super.bind(socketAddress);
            return;
        }
        throw new IOException("Underlying socket should already be connected");
    }

    @Override // java.net.Socket
    public SocketAddress getLocalSocketAddress() {
        if (this.self == this) {
            return super.getLocalSocketAddress();
        }
        return this.self.getLocalSocketAddress();
    }

    @Override // java.net.Socket
    public SocketAddress getRemoteSocketAddress() {
        if (this.self == this) {
            return super.getRemoteSocketAddress();
        }
        return this.self.getRemoteSocketAddress();
    }

    @Override // java.net.Socket
    public final void connect(SocketAddress socketAddress) throws IOException {
        connect(socketAddress, 0);
    }

    @Override // java.net.Socket
    public final boolean isConnected() {
        if (this.self == this) {
            return super.isConnected();
        }
        return this.self.isConnected();
    }

    @Override // java.net.Socket
    public final boolean isBound() {
        if (this.self == this) {
            return super.isBound();
        }
        return this.self.isBound();
    }

    @Override // java.net.Socket
    public void shutdownInput() throws IOException {
        if (this.self == this) {
            super.shutdownInput();
        } else {
            this.self.shutdownInput();
        }
    }

    @Override // java.net.Socket
    public void shutdownOutput() throws IOException {
        if (this.self == this) {
            super.shutdownOutput();
        } else {
            this.self.shutdownOutput();
        }
    }

    @Override // java.net.Socket
    public boolean isInputShutdown() {
        if (this.self == this) {
            return super.isInputShutdown();
        }
        return this.self.isInputShutdown();
    }

    @Override // java.net.Socket
    public boolean isOutputShutdown() {
        if (this.self == this) {
            return super.isOutputShutdown();
        }
        return this.self.isOutputShutdown();
    }

    protected final void finalize() throws Throwable {
        try {
            try {
                close();
            } catch (IOException e2) {
                try {
                    if (this.self == this) {
                        super.close();
                    }
                } catch (IOException e3) {
                }
            }
        } finally {
            super.finalize();
        }
    }

    @Override // java.net.Socket
    public final InetAddress getInetAddress() {
        if (this.self == this) {
            return super.getInetAddress();
        }
        return this.self.getInetAddress();
    }

    @Override // java.net.Socket
    public final InetAddress getLocalAddress() {
        if (this.self == this) {
            return super.getLocalAddress();
        }
        return this.self.getLocalAddress();
    }

    @Override // java.net.Socket
    public final int getPort() {
        if (this.self == this) {
            return super.getPort();
        }
        return this.self.getPort();
    }

    @Override // java.net.Socket
    public final int getLocalPort() {
        if (this.self == this) {
            return super.getLocalPort();
        }
        return this.self.getLocalPort();
    }

    @Override // java.net.Socket
    public final void setTcpNoDelay(boolean z2) throws SocketException {
        if (this.self == this) {
            super.setTcpNoDelay(z2);
        } else {
            this.self.setTcpNoDelay(z2);
        }
    }

    @Override // java.net.Socket
    public final boolean getTcpNoDelay() throws SocketException {
        if (this.self == this) {
            return super.getTcpNoDelay();
        }
        return this.self.getTcpNoDelay();
    }

    @Override // java.net.Socket
    public final void setSoLinger(boolean z2, int i2) throws SocketException {
        if (this.self == this) {
            super.setSoLinger(z2, i2);
        } else {
            this.self.setSoLinger(z2, i2);
        }
    }

    @Override // java.net.Socket
    public final int getSoLinger() throws SocketException {
        if (this.self == this) {
            return super.getSoLinger();
        }
        return this.self.getSoLinger();
    }

    @Override // java.net.Socket
    public final void sendUrgentData(int i2) throws SocketException {
        throw new SocketException("This method is not supported by SSLSockets");
    }

    @Override // java.net.Socket
    public final void setOOBInline(boolean z2) throws SocketException {
        throw new SocketException("This method is ineffective, since sending urgent data is not supported by SSLSockets");
    }

    @Override // java.net.Socket
    public final boolean getOOBInline() throws SocketException {
        throw new SocketException("This method is ineffective, since sending urgent data is not supported by SSLSockets");
    }

    @Override // java.net.Socket
    public final int getSoTimeout() throws SocketException {
        if (this.self == this) {
            return super.getSoTimeout();
        }
        return this.self.getSoTimeout();
    }

    @Override // java.net.Socket
    public final void setSendBufferSize(int i2) throws SocketException {
        if (this.self == this) {
            super.setSendBufferSize(i2);
        } else {
            this.self.setSendBufferSize(i2);
        }
    }

    @Override // java.net.Socket
    public final int getSendBufferSize() throws SocketException {
        if (this.self == this) {
            return super.getSendBufferSize();
        }
        return this.self.getSendBufferSize();
    }

    @Override // java.net.Socket
    public final void setReceiveBufferSize(int i2) throws SocketException {
        if (this.self == this) {
            super.setReceiveBufferSize(i2);
        } else {
            this.self.setReceiveBufferSize(i2);
        }
    }

    @Override // java.net.Socket
    public final int getReceiveBufferSize() throws SocketException {
        if (this.self == this) {
            return super.getReceiveBufferSize();
        }
        return this.self.getReceiveBufferSize();
    }

    @Override // java.net.Socket
    public final void setKeepAlive(boolean z2) throws SocketException {
        if (this.self == this) {
            super.setKeepAlive(z2);
        } else {
            this.self.setKeepAlive(z2);
        }
    }

    @Override // java.net.Socket
    public final boolean getKeepAlive() throws SocketException {
        if (this.self == this) {
            return super.getKeepAlive();
        }
        return this.self.getKeepAlive();
    }

    @Override // java.net.Socket
    public final void setTrafficClass(int i2) throws SocketException {
        if (this.self == this) {
            super.setTrafficClass(i2);
        } else {
            this.self.setTrafficClass(i2);
        }
    }

    @Override // java.net.Socket
    public final int getTrafficClass() throws SocketException {
        if (this.self == this) {
            return super.getTrafficClass();
        }
        return this.self.getTrafficClass();
    }

    @Override // java.net.Socket
    public final void setReuseAddress(boolean z2) throws SocketException {
        if (this.self == this) {
            super.setReuseAddress(z2);
        } else {
            this.self.setReuseAddress(z2);
        }
    }

    @Override // java.net.Socket
    public final boolean getReuseAddress() throws SocketException {
        if (this.self == this) {
            return super.getReuseAddress();
        }
        return this.self.getReuseAddress();
    }

    @Override // java.net.Socket
    public void setPerformancePreferences(int i2, int i3, int i4) {
        if (this.self == this) {
            super.setPerformancePreferences(i2, i3, i4);
        } else {
            this.self.setPerformancePreferences(i2, i3, i4);
        }
    }

    @Override // java.net.Socket
    public String toString() {
        if (this.self == this) {
            return super.toString();
        }
        return this.self.toString();
    }

    @Override // java.net.Socket
    public InputStream getInputStream() throws IOException {
        if (this.self == this) {
            return super.getInputStream();
        }
        if (this.consumedInput != null) {
            return new SequenceInputStream(this.consumedInput, this.self.getInputStream());
        }
        return this.self.getInputStream();
    }

    @Override // java.net.Socket
    public OutputStream getOutputStream() throws IOException {
        if (this.self == this) {
            return super.getOutputStream();
        }
        return this.self.getOutputStream();
    }

    @Override // java.net.Socket, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.self == this) {
            super.close();
        } else {
            this.self.close();
        }
    }

    @Override // java.net.Socket
    public synchronized void setSoTimeout(int i2) throws SocketException {
        if (this.self == this) {
            super.setSoTimeout(i2);
        } else {
            this.self.setSoTimeout(i2);
        }
    }

    boolean isLayered() {
        return this.self != this;
    }
}
