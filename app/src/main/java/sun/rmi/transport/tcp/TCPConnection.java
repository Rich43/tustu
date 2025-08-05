package sun.rmi.transport.tcp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import sun.rmi.runtime.Log;
import sun.rmi.transport.Channel;
import sun.rmi.transport.Connection;
import sun.rmi.transport.proxy.RMISocketInfo;

/* loaded from: rt.jar:sun/rmi/transport/tcp/TCPConnection.class */
public class TCPConnection implements Connection {
    private Socket socket;
    private Channel channel;
    private InputStream in;
    private OutputStream out;
    private long expiration;
    private long lastuse;
    private long roundtrip;

    TCPConnection(TCPChannel tCPChannel, Socket socket, InputStream inputStream, OutputStream outputStream) {
        this.in = null;
        this.out = null;
        this.expiration = Long.MAX_VALUE;
        this.lastuse = Long.MIN_VALUE;
        this.roundtrip = 5L;
        this.socket = socket;
        this.channel = tCPChannel;
        this.in = inputStream;
        this.out = outputStream;
    }

    TCPConnection(TCPChannel tCPChannel, InputStream inputStream, OutputStream outputStream) {
        this(tCPChannel, null, inputStream, outputStream);
    }

    TCPConnection(TCPChannel tCPChannel, Socket socket) {
        this(tCPChannel, socket, null, null);
    }

    @Override // sun.rmi.transport.Connection
    public OutputStream getOutputStream() throws IOException {
        if (this.out == null) {
            this.out = new BufferedOutputStream(this.socket.getOutputStream());
        }
        return this.out;
    }

    @Override // sun.rmi.transport.Connection
    public void releaseOutputStream() throws IOException {
        if (this.out != null) {
            this.out.flush();
        }
    }

    @Override // sun.rmi.transport.Connection
    public InputStream getInputStream() throws IOException {
        if (this.in == null) {
            this.in = new BufferedInputStream(this.socket.getInputStream());
        }
        return this.in;
    }

    @Override // sun.rmi.transport.Connection
    public void releaseInputStream() {
    }

    @Override // sun.rmi.transport.Connection
    public boolean isReusable() {
        if (this.socket != null && (this.socket instanceof RMISocketInfo)) {
            return ((RMISocketInfo) this.socket).isReusable();
        }
        return true;
    }

    void setExpiration(long j2) {
        this.expiration = j2;
    }

    void setLastUseTime(long j2) {
        this.lastuse = j2;
    }

    boolean expired(long j2) {
        return this.expiration <= j2;
    }

    public boolean isDead() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (this.roundtrip > 0 && jCurrentTimeMillis < this.lastuse + this.roundtrip) {
            return false;
        }
        try {
            InputStream inputStream = getInputStream();
            OutputStream outputStream = getOutputStream();
            try {
                outputStream.write(82);
                outputStream.flush();
                int i2 = inputStream.read();
                if (i2 == 83) {
                    this.roundtrip = (System.currentTimeMillis() - jCurrentTimeMillis) * 2;
                    return false;
                }
                if (TCPTransport.tcpLog.isLoggable(Log.BRIEF)) {
                    TCPTransport.tcpLog.log(Log.BRIEF, i2 == -1 ? "server has been deactivated" : "server protocol error: ping response = " + i2);
                    return true;
                }
                return true;
            } catch (IOException e2) {
                TCPTransport.tcpLog.log(Log.VERBOSE, "exception: ", e2);
                TCPTransport.tcpLog.log(Log.BRIEF, "server ping failed");
                return true;
            }
        } catch (IOException e3) {
            return true;
        }
    }

    @Override // sun.rmi.transport.Connection
    public void close() throws IOException {
        TCPTransport.tcpLog.log(Log.BRIEF, "close connection");
        if (this.socket != null) {
            this.socket.close();
        } else {
            this.in.close();
            this.out.close();
        }
    }

    @Override // sun.rmi.transport.Connection
    public Channel getChannel() {
        return this.channel;
    }
}
