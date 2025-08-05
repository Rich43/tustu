package java.net;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: rt.jar:java/net/SocketImpl.class */
public abstract class SocketImpl implements SocketOptions {
    Socket socket = null;
    ServerSocket serverSocket = null;
    protected FileDescriptor fd;
    protected InetAddress address;
    protected int port;
    protected int localport;

    protected abstract void create(boolean z2) throws IOException;

    protected abstract void connect(String str, int i2) throws IOException;

    protected abstract void connect(InetAddress inetAddress, int i2) throws IOException;

    protected abstract void connect(SocketAddress socketAddress, int i2) throws IOException;

    protected abstract void bind(InetAddress inetAddress, int i2) throws IOException;

    protected abstract void listen(int i2) throws IOException;

    protected abstract void accept(SocketImpl socketImpl) throws IOException;

    protected abstract InputStream getInputStream() throws IOException;

    protected abstract OutputStream getOutputStream() throws IOException;

    protected abstract int available() throws IOException;

    protected abstract void close() throws IOException;

    protected abstract void sendUrgentData(int i2) throws IOException;

    protected void shutdownInput() throws IOException {
        throw new IOException("Method not implemented!");
    }

    protected void shutdownOutput() throws IOException {
        throw new IOException("Method not implemented!");
    }

    protected FileDescriptor getFileDescriptor() {
        return this.fd;
    }

    protected InetAddress getInetAddress() {
        return this.address;
    }

    protected int getPort() {
        return this.port;
    }

    protected boolean supportsUrgentData() {
        return false;
    }

    protected int getLocalPort() {
        return this.localport;
    }

    void setSocket(Socket socket) {
        this.socket = socket;
    }

    Socket getSocket() {
        return this.socket;
    }

    void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    ServerSocket getServerSocket() {
        return this.serverSocket;
    }

    public String toString() {
        return "Socket[addr=" + ((Object) getInetAddress()) + ",port=" + getPort() + ",localport=" + getLocalPort() + "]";
    }

    void reset() throws IOException {
        this.address = null;
        this.port = 0;
        this.localport = 0;
    }

    protected void setPerformancePreferences(int i2, int i3, int i4) {
    }

    <T> void setOption(SocketOption<T> socketOption, T t2) throws IOException {
        if (socketOption == StandardSocketOptions.SO_KEEPALIVE) {
            setOption(8, t2);
            return;
        }
        if (socketOption == StandardSocketOptions.SO_SNDBUF) {
            setOption(4097, t2);
            return;
        }
        if (socketOption == StandardSocketOptions.SO_RCVBUF) {
            setOption(SocketOptions.SO_RCVBUF, t2);
            return;
        }
        if (socketOption == StandardSocketOptions.SO_REUSEADDR) {
            setOption(4, t2);
            return;
        }
        if (socketOption == StandardSocketOptions.SO_LINGER) {
            setOption(128, t2);
        } else if (socketOption == StandardSocketOptions.IP_TOS) {
            setOption(3, t2);
        } else {
            if (socketOption == StandardSocketOptions.TCP_NODELAY) {
                setOption(1, t2);
                return;
            }
            throw new UnsupportedOperationException("unsupported option");
        }
    }

    <T> T getOption(SocketOption<T> socketOption) throws IOException {
        if (socketOption == StandardSocketOptions.SO_KEEPALIVE) {
            return (T) getOption(8);
        }
        if (socketOption == StandardSocketOptions.SO_SNDBUF) {
            return (T) getOption(4097);
        }
        if (socketOption == StandardSocketOptions.SO_RCVBUF) {
            return (T) getOption(SocketOptions.SO_RCVBUF);
        }
        if (socketOption == StandardSocketOptions.SO_REUSEADDR) {
            return (T) getOption(4);
        }
        if (socketOption == StandardSocketOptions.SO_LINGER) {
            return (T) getOption(128);
        }
        if (socketOption == StandardSocketOptions.IP_TOS) {
            return (T) getOption(3);
        }
        if (socketOption == StandardSocketOptions.TCP_NODELAY) {
            return (T) getOption(1);
        }
        throw new UnsupportedOperationException("unsupported option");
    }
}
