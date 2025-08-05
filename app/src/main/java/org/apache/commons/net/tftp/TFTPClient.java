package org.apache.commons.net.tftp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketException;
import org.apache.commons.net.io.FromNetASCIIOutputStream;
import org.apache.commons.net.io.ToNetASCIIInputStream;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/tftp/TFTPClient.class */
public class TFTPClient extends TFTP {
    public static final int DEFAULT_MAX_TIMEOUTS = 5;
    private long totalBytesReceived = 0;
    private long totalBytesSent = 0;
    private int __maxTimeouts = 5;

    public void setMaxTimeouts(int numTimeouts) {
        if (numTimeouts < 1) {
            this.__maxTimeouts = 1;
        } else {
            this.__maxTimeouts = numTimeouts;
        }
    }

    public int getMaxTimeouts() {
        return this.__maxTimeouts;
    }

    public long getTotalBytesReceived() {
        return this.totalBytesReceived;
    }

    public long getTotalBytesSent() {
        return this.totalBytesSent;
    }

    public int receiveFile(String filename, int mode, OutputStream output, InetAddress host, int port) throws IOException {
        int bytesRead = 0;
        int lastBlock = 0;
        int block = 1;
        int hostPort = 0;
        int dataLength = 0;
        this.totalBytesReceived = 0L;
        if (mode == 0) {
            output = new FromNetASCIIOutputStream(output);
        }
        TFTPPacket sent = new TFTPReadRequestPacket(host, port, filename, mode);
        TFTPAckPacket ack = new TFTPAckPacket(host, port, 0);
        beginBufferedOps();
        boolean justStarted = true;
        do {
            try {
                bufferedSend(sent);
                boolean wantReply = true;
                int timeouts = 0;
                do {
                    try {
                        try {
                            try {
                                TFTPPacket received = bufferedReceive();
                                int recdPort = received.getPort();
                                InetAddress recdAddress = received.getAddress();
                                if (justStarted) {
                                    justStarted = false;
                                    if (recdPort == port) {
                                        bufferedSend(new TFTPErrorPacket(recdAddress, recdPort, 5, "INCORRECT SOURCE PORT"));
                                        throw new IOException("Incorrect source port (" + recdPort + ") in request reply.");
                                    }
                                    hostPort = recdPort;
                                    ack.setPort(hostPort);
                                    if (!host.equals(recdAddress)) {
                                        host = recdAddress;
                                        ack.setAddress(host);
                                        sent.setAddress(host);
                                    }
                                }
                                if (host.equals(recdAddress) && recdPort == hostPort) {
                                    switch (received.getType()) {
                                        case 3:
                                            TFTPDataPacket data = (TFTPDataPacket) received;
                                            dataLength = data.getDataLength();
                                            lastBlock = data.getBlockNumber();
                                            if (lastBlock == block) {
                                                try {
                                                    output.write(data.getData(), data.getDataOffset(), dataLength);
                                                    block++;
                                                    if (block > 65535) {
                                                        block = 0;
                                                    }
                                                    wantReply = false;
                                                } catch (IOException e2) {
                                                    bufferedSend(new TFTPErrorPacket(host, hostPort, 3, "File write failed."));
                                                    throw e2;
                                                }
                                            } else {
                                                discardPackets();
                                                if (lastBlock == (block == 0 ? 65535 : block - 1)) {
                                                    wantReply = false;
                                                }
                                            }
                                            break;
                                        case 5:
                                            TFTPErrorPacket error = (TFTPErrorPacket) received;
                                            throw new IOException("Error code " + error.getError() + " received: " + error.getMessage());
                                        default:
                                            throw new IOException("Received unexpected packet type (" + received.getType() + ")");
                                    }
                                } else {
                                    bufferedSend(new TFTPErrorPacket(recdAddress, recdPort, 5, "Unexpected host or port."));
                                }
                            } catch (InterruptedIOException e3) {
                                timeouts++;
                                if (timeouts >= this.__maxTimeouts) {
                                    throw new IOException("Connection timed out.");
                                }
                            }
                        } catch (SocketException e4) {
                            timeouts++;
                            if (timeouts >= this.__maxTimeouts) {
                                throw new IOException("Connection timed out.");
                            }
                        }
                    } catch (TFTPPacketException e5) {
                        throw new IOException("Bad packet: " + e5.getMessage());
                    }
                } while (wantReply);
                ack.setBlockNumber(lastBlock);
                sent = ack;
                bytesRead += dataLength;
                this.totalBytesReceived += dataLength;
            } catch (Throwable th) {
                endBufferedOps();
                throw th;
            }
        } while (dataLength == 512);
        bufferedSend(sent);
        endBufferedOps();
        return bytesRead;
    }

    public int receiveFile(String filename, int mode, OutputStream output, String hostname, int port) throws IOException {
        return receiveFile(filename, mode, output, InetAddress.getByName(hostname), port);
    }

    public int receiveFile(String filename, int mode, OutputStream output, InetAddress host) throws IOException {
        return receiveFile(filename, mode, output, host, 69);
    }

    public int receiveFile(String filename, int mode, OutputStream output, String hostname) throws IOException {
        return receiveFile(filename, mode, output, InetAddress.getByName(hostname), 69);
    }

    public void sendFile(String filename, int mode, InputStream input, InetAddress host, int port) throws IOException {
        int bytesRead;
        int block = 0;
        int hostPort = 0;
        boolean justStarted = true;
        boolean lastAckWait = false;
        this.totalBytesSent = 0L;
        if (mode == 0) {
            input = new ToNetASCIIInputStream(input);
        }
        TFTPPacket sent = new TFTPWriteRequestPacket(host, port, filename, mode);
        TFTPDataPacket data = new TFTPDataPacket(host, port, 0, this._sendBuffer, 4, 0);
        beginBufferedOps();
        while (true) {
            try {
                bufferedSend(sent);
                boolean wantReply = true;
                int timeouts = 0;
                do {
                    try {
                        try {
                            try {
                                TFTPPacket received = bufferedReceive();
                                InetAddress recdAddress = received.getAddress();
                                int recdPort = received.getPort();
                                if (justStarted) {
                                    justStarted = false;
                                    if (recdPort == port) {
                                        bufferedSend(new TFTPErrorPacket(recdAddress, recdPort, 5, "INCORRECT SOURCE PORT"));
                                        throw new IOException("Incorrect source port (" + recdPort + ") in request reply.");
                                    }
                                    hostPort = recdPort;
                                    data.setPort(hostPort);
                                    if (!host.equals(recdAddress)) {
                                        host = recdAddress;
                                        data.setAddress(host);
                                        sent.setAddress(host);
                                    }
                                }
                                if (host.equals(recdAddress) && recdPort == hostPort) {
                                    switch (received.getType()) {
                                        case 4:
                                            int lastBlock = ((TFTPAckPacket) received).getBlockNumber();
                                            if (lastBlock == block) {
                                                block++;
                                                if (block > 65535) {
                                                    block = 0;
                                                }
                                                wantReply = false;
                                            } else {
                                                discardPackets();
                                            }
                                            break;
                                        case 5:
                                            TFTPErrorPacket error = (TFTPErrorPacket) received;
                                            throw new IOException("Error code " + error.getError() + " received: " + error.getMessage());
                                        default:
                                            throw new IOException("Received unexpected packet type.");
                                    }
                                } else {
                                    bufferedSend(new TFTPErrorPacket(recdAddress, recdPort, 5, "Unexpected host or port."));
                                }
                            } catch (TFTPPacketException e2) {
                                throw new IOException("Bad packet: " + e2.getMessage());
                            }
                        } catch (InterruptedIOException e3) {
                            timeouts++;
                            if (timeouts >= this.__maxTimeouts) {
                                throw new IOException("Connection timed out.");
                            }
                        }
                    } catch (SocketException e4) {
                        timeouts++;
                        if (timeouts >= this.__maxTimeouts) {
                            throw new IOException("Connection timed out.");
                        }
                    }
                } while (wantReply);
                if (!lastAckWait) {
                    int dataLength = 512;
                    int offset = 4;
                    int totalThisPacket = 0;
                    while (dataLength > 0 && (bytesRead = input.read(this._sendBuffer, offset, dataLength)) > 0) {
                        offset += bytesRead;
                        dataLength -= bytesRead;
                        totalThisPacket += bytesRead;
                    }
                    if (totalThisPacket < 512) {
                        lastAckWait = true;
                    }
                    data.setBlockNumber(block);
                    data.setData(this._sendBuffer, 4, totalThisPacket);
                    sent = data;
                    this.totalBytesSent += totalThisPacket;
                } else {
                    return;
                }
            } finally {
                endBufferedOps();
            }
        }
    }

    public void sendFile(String filename, int mode, InputStream input, String hostname, int port) throws IOException {
        sendFile(filename, mode, input, InetAddress.getByName(hostname), port);
    }

    public void sendFile(String filename, int mode, InputStream input, InetAddress host) throws IOException {
        sendFile(filename, mode, input, host, 69);
    }

    public void sendFile(String filename, int mode, InputStream input, String hostname) throws IOException {
        sendFile(filename, mode, input, InetAddress.getByName(hostname), 69);
    }
}
