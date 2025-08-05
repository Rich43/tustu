package org.bluecove.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;
import java.net.UnknownServiceException;
import java.nio.channels.SocketChannel;

/* loaded from: bluecove-bluez-2.1.1.jar:org/bluecove/socket/LocalSocket.class */
public class LocalSocket extends Socket {
    private LocalSocketImpl impl;
    private boolean shutdownIn;
    private boolean shutdownOut;

    public LocalSocket() throws IOException {
        super((SocketImpl) null);
        this.shutdownIn = false;
        this.shutdownOut = false;
        this.impl = new LocalSocketImpl();
        this.impl.create(true);
    }

    public LocalSocket(LocalSocketAddress address) throws IOException {
        this();
        connect(address);
    }

    LocalSocket(LocalSocketImpl impl) throws IOException {
        super((SocketImpl) null);
        this.shutdownIn = false;
        this.shutdownOut = false;
        this.impl = impl;
    }

    @Override // java.net.Socket
    public void connect(SocketAddress endpoint, int timeout) throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is already closed");
        }
        if (isConnected()) {
            throw new SocketException("Socket is already connected");
        }
        this.impl.connect(endpoint, timeout);
    }

    @Override // java.net.Socket
    public String toString() {
        if (isConnected()) {
            return "LocalSocket[" + ((Object) getLocalSocketAddress()) + "]";
        }
        return "LocalSocket[unconnected]";
    }

    @Override // java.net.Socket
    public SocketAddress getRemoteSocketAddress() {
        if (!isConnected()) {
            return null;
        }
        return this.impl.getSocketAddress();
    }

    @Override // java.net.Socket
    public SocketAddress getLocalSocketAddress() {
        return this.impl.getSocketAddress();
    }

    @Override // java.net.Socket
    public void bind(SocketAddress bindpoint) throws IOException {
        throw new UnknownServiceException();
    }

    @Override // java.net.Socket
    public SocketChannel getChannel() {
        return null;
    }

    @Override // java.net.Socket
    public InetAddress getInetAddress() {
        if (!isConnected()) {
            return null;
        }
        throw new IllegalArgumentException("Unsupported address type");
    }

    @Override // java.net.Socket
    public InetAddress getLocalAddress() {
        if (!isConnected()) {
            return null;
        }
        throw new IllegalArgumentException("Unsupported address type");
    }

    @Override // java.net.Socket
    public int getPort() {
        throw new IllegalArgumentException("Unsupported address type");
    }

    @Override // java.net.Socket
    public int getLocalPort() {
        throw new IllegalArgumentException("Unsupported address type");
    }

    @Override // java.net.Socket, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.impl.close();
    }

    @Override // java.net.Socket
    public boolean isConnected() {
        return this.impl.isConnected();
    }

    @Override // java.net.Socket
    public boolean isBound() {
        return this.impl.isBound();
    }

    public Credentials getPeerCredentials() throws IOException {
        return this.impl.readPeerCredentials();
    }

    public static Credentials getProcessCredentials() {
        return LocalSocketImpl.readProcessCredentials();
    }

    @Override // java.net.Socket
    public boolean isClosed() {
        return this.impl.isClosed();
    }

    @Override // java.net.Socket
    public OutputStream getOutputStream() throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is already closed");
        }
        if (!isConnected()) {
            throw new SocketException("Socket is not connected");
        }
        if (isOutputShutdown()) {
            throw new SocketException("Socket output is shutdown");
        }
        return this.impl.getOutputStream();
    }

    @Override // java.net.Socket
    public InputStream getInputStream() throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is already closed");
        }
        if (!isConnected()) {
            throw new SocketException("Socket is not connected");
        }
        if (isInputShutdown()) {
            throw new SocketException("Socket input is shutdown");
        }
        return this.impl.getInputStream();
    }

    @Override // java.net.Socket
    public boolean isInputShutdown() {
        return this.shutdownIn;
    }

    @Override // java.net.Socket
    public boolean isOutputShutdown() {
        return this.shutdownOut;
    }

    @Override // java.net.Socket
    public void shutdownInput() throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is already closed");
        }
        if (!isConnected()) {
            throw new SocketException("Socket is not connected");
        }
        if (isInputShutdown()) {
            throw new SocketException("Socket input is already shutdown");
        }
        this.impl.shutdownInput();
        this.shutdownIn = true;
    }

    @Override // java.net.Socket
    public void shutdownOutput() throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is already closed");
        }
        if (!isConnected()) {
            throw new SocketException("Socket is not connected");
        }
        if (isOutputShutdown()) {
            throw new SocketException("Socket output is already shutdown");
        }
        this.impl.shutdownOutput();
        this.shutdownOut = true;
    }

    @Override // java.net.Socket
    public void setSoLinger(boolean on, int linger) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is already closed");
        }
        if (!on) {
            this.impl.setOption(1, Boolean.valueOf(on));
        } else {
            if (linger < 0) {
                throw new IllegalArgumentException("invalid value for SO_LINGER");
            }
            if (linger > 65535) {
                linger = 65535;
            }
            this.impl.setOption(1, Integer.valueOf(linger));
        }
    }

    @Override // java.net.Socket
    public int getSoLinger() throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is already closed");
        }
        Object value = this.impl.getOption(1);
        if (value instanceof Integer) {
            return ((Integer) value).intValue();
        }
        return -1;
    }

    @Override // java.net.Socket
    public void setReceiveBufferSize(int size) throws SocketException {
        this.impl.setOption(4, Integer.valueOf(size));
    }

    @Override // java.net.Socket
    public int getReceiveBufferSize() throws SocketException {
        return ((Integer) this.impl.getOption(4)).intValue();
    }

    @Override // java.net.Socket
    public void setSendBufferSize(int n2) throws SocketException {
        this.impl.setOption(3, Integer.valueOf(n2));
    }

    @Override // java.net.Socket
    public int getSendBufferSize() throws SocketException {
        return ((Integer) this.impl.getOption(3)).intValue();
    }

    @Override // java.net.Socket
    public void setSoTimeout(int n2) throws SocketException {
        this.impl.setOption(5, Integer.valueOf(n2));
        this.impl.setOption(6, Integer.valueOf(n2));
    }

    @Override // java.net.Socket
    public int getSoTimeout() throws SocketException {
        return ((Integer) this.impl.getOption(6)).intValue();
    }

    public void setSoReceiveTimeout(int n2) throws SocketException {
        this.impl.setOption(5, Integer.valueOf(n2));
    }

    public int getSoReceiveTimeout() throws SocketException {
        return ((Integer) this.impl.getOption(5)).intValue();
    }

    public void setSoSendTimeout(int n2) throws SocketException {
        this.impl.setOption(6, Integer.valueOf(n2));
    }

    public int getSoSendTimeout() throws SocketException {
        return ((Integer) this.impl.getOption(6)).intValue();
    }

    public void setReceiveCredentials(boolean on) throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is already closed");
        }
        this.impl.setOption(2, Integer.valueOf(on ? 1 : 0));
    }

    public boolean getReceiveCredentials() throws SocketException {
        if (isClosed()) {
            throw new SocketException("Socket is already closed");
        }
        Object value = this.impl.getOption(2);
        return (value instanceof Integer) && ((Integer) value).intValue() > 0;
    }
}
