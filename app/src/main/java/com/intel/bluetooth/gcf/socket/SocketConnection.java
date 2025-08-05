package com.intel.bluetooth.gcf.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/gcf/socket/SocketConnection.class */
public class SocketConnection implements javax.microedition.io.SocketConnection {
    protected Socket socket;

    public SocketConnection() {
    }

    public SocketConnection(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
    }

    public SocketConnection(Socket socket) {
        this.socket = socket;
    }

    @Override // javax.microedition.io.SocketConnection
    public String getAddress() throws IOException {
        if (this.socket == null || this.socket.isClosed()) {
            throw new IOException();
        }
        return this.socket.getInetAddress().toString();
    }

    @Override // javax.microedition.io.SocketConnection
    public String getLocalAddress() throws IOException {
        if (this.socket == null || this.socket.isClosed()) {
            throw new IOException();
        }
        return this.socket.getLocalAddress().toString();
    }

    @Override // javax.microedition.io.SocketConnection
    public int getLocalPort() throws IOException {
        if (this.socket == null || this.socket.isClosed()) {
            throw new IOException();
        }
        return this.socket.getLocalPort();
    }

    @Override // javax.microedition.io.SocketConnection
    public int getPort() throws IOException {
        if (this.socket == null || this.socket.isClosed()) {
            throw new IOException();
        }
        return this.socket.getPort();
    }

    @Override // javax.microedition.io.SocketConnection
    public int getSocketOption(byte option) throws IOException, IllegalArgumentException {
        if (this.socket != null && this.socket.isClosed()) {
            throw new IOException();
        }
        switch (option) {
            case 0:
                if (this.socket.getTcpNoDelay()) {
                    return 1;
                }
                return 0;
            case 1:
                int value = this.socket.getSoLinger();
                if (value == -1) {
                    return 0;
                }
                return value;
            case 2:
                if (this.socket.getKeepAlive()) {
                    return 1;
                }
                return 0;
            case 3:
                return this.socket.getReceiveBufferSize();
            case 4:
                return this.socket.getSendBufferSize();
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override // javax.microedition.io.SocketConnection
    public void setSocketOption(byte option, int value) throws IOException, IllegalArgumentException {
        int keepalive;
        int delay;
        if (this.socket.isClosed()) {
            throw new IOException();
        }
        switch (option) {
            case 0:
                if (value == 0) {
                    delay = 0;
                } else {
                    delay = 1;
                }
                this.socket.setTcpNoDelay(delay != 0);
                return;
            case 1:
                if (value < 0) {
                    throw new IllegalArgumentException();
                }
                this.socket.setSoLinger(value != 0, value);
                return;
            case 2:
                if (value == 0) {
                    keepalive = 0;
                } else {
                    keepalive = 1;
                }
                this.socket.setKeepAlive(keepalive != 0);
                return;
            case 3:
                if (value <= 0) {
                    throw new IllegalArgumentException();
                }
                this.socket.setReceiveBufferSize(value);
                return;
            case 4:
                if (value <= 0) {
                    throw new IllegalArgumentException();
                }
                this.socket.setSendBufferSize(value);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override // javax.microedition.io.Connection
    public void close() throws IOException {
        this.socket.close();
    }

    @Override // javax.microedition.io.InputConnection
    public InputStream openInputStream() throws IOException {
        return this.socket.getInputStream();
    }

    @Override // javax.microedition.io.InputConnection
    public DataInputStream openDataInputStream() throws IOException {
        return new DataInputStream(openInputStream());
    }

    @Override // javax.microedition.io.OutputConnection
    public OutputStream openOutputStream() throws IOException {
        return this.socket.getOutputStream();
    }

    @Override // javax.microedition.io.OutputConnection
    public DataOutputStream openDataOutputStream() throws IOException {
        return new DataOutputStream(openOutputStream());
    }
}
