package sun.rmi.transport.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.server.LogStream;
import java.security.AccessController;
import java.util.Enumeration;
import java.util.Hashtable;
import sun.rmi.runtime.Log;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/rmi/transport/tcp/ConnectionMultiplexer.class */
final class ConnectionMultiplexer {
    static int logLevel = LogStream.parseLevel(getLogLevel());
    static final Log multiplexLog = Log.getLog("sun.rmi.transport.tcp.multiplex", "multiplex", logLevel);
    private static final int OPEN = 225;
    private static final int CLOSE = 226;
    private static final int CLOSEACK = 227;
    private static final int REQUEST = 228;
    private static final int TRANSMIT = 229;
    private TCPChannel channel;
    private InputStream in;
    private OutputStream out;
    private boolean orig;
    private DataInputStream dataIn;
    private DataOutputStream dataOut;
    private static final int maxConnections = 256;
    private Hashtable<Integer, MultiplexConnectionInfo> connectionTable = new Hashtable<>(7);
    private int numConnections = 0;
    private int lastID = 4097;
    private boolean alive = true;

    private static String getLogLevel() {
        return (String) AccessController.doPrivileged(new GetPropertyAction("sun.rmi.transport.tcp.multiplex.logLevel"));
    }

    public ConnectionMultiplexer(TCPChannel tCPChannel, InputStream inputStream, OutputStream outputStream, boolean z2) {
        this.channel = tCPChannel;
        this.in = inputStream;
        this.out = outputStream;
        this.orig = z2;
        this.dataIn = new DataInputStream(inputStream);
        this.dataOut = new DataOutputStream(outputStream);
    }

    public void run() throws IOException {
        while (true) {
            try {
                int unsignedByte = this.dataIn.readUnsignedByte();
                switch (unsignedByte) {
                    case 225:
                        int unsignedShort = this.dataIn.readUnsignedShort();
                        if (multiplexLog.isLoggable(Log.VERBOSE)) {
                            multiplexLog.log(Log.VERBOSE, "operation  OPEN " + unsignedShort);
                        }
                        if (this.connectionTable.get(Integer.valueOf(unsignedShort)) != null) {
                            throw new IOException("OPEN: Connection ID already exists");
                        }
                        MultiplexConnectionInfo multiplexConnectionInfo = new MultiplexConnectionInfo(unsignedShort);
                        multiplexConnectionInfo.in = new MultiplexInputStream(this, multiplexConnectionInfo, 2048);
                        multiplexConnectionInfo.out = new MultiplexOutputStream(this, multiplexConnectionInfo, 2048);
                        synchronized (this.connectionTable) {
                            this.connectionTable.put(Integer.valueOf(unsignedShort), multiplexConnectionInfo);
                            this.numConnections++;
                        }
                        this.channel.acceptMultiplexConnection(new TCPConnection(this.channel, multiplexConnectionInfo.in, multiplexConnectionInfo.out));
                    case 226:
                        int unsignedShort2 = this.dataIn.readUnsignedShort();
                        if (multiplexLog.isLoggable(Log.VERBOSE)) {
                            multiplexLog.log(Log.VERBOSE, "operation  CLOSE " + unsignedShort2);
                        }
                        MultiplexConnectionInfo multiplexConnectionInfo2 = this.connectionTable.get(Integer.valueOf(unsignedShort2));
                        if (multiplexConnectionInfo2 == null) {
                            throw new IOException("CLOSE: Invalid connection ID");
                        }
                        multiplexConnectionInfo2.in.disconnect();
                        multiplexConnectionInfo2.out.disconnect();
                        if (!multiplexConnectionInfo2.closed) {
                            sendCloseAck(multiplexConnectionInfo2);
                        }
                        synchronized (this.connectionTable) {
                            this.connectionTable.remove(Integer.valueOf(unsignedShort2));
                            this.numConnections--;
                        }
                    case 227:
                        int unsignedShort3 = this.dataIn.readUnsignedShort();
                        if (multiplexLog.isLoggable(Log.VERBOSE)) {
                            multiplexLog.log(Log.VERBOSE, "operation  CLOSEACK " + unsignedShort3);
                        }
                        MultiplexConnectionInfo multiplexConnectionInfo3 = this.connectionTable.get(Integer.valueOf(unsignedShort3));
                        if (multiplexConnectionInfo3 == null) {
                            throw new IOException("CLOSEACK: Invalid connection ID");
                        }
                        if (!multiplexConnectionInfo3.closed) {
                            throw new IOException("CLOSEACK: Connection not closed");
                        }
                        multiplexConnectionInfo3.in.disconnect();
                        multiplexConnectionInfo3.out.disconnect();
                        synchronized (this.connectionTable) {
                            this.connectionTable.remove(Integer.valueOf(unsignedShort3));
                            this.numConnections--;
                        }
                    case 228:
                        int unsignedShort4 = this.dataIn.readUnsignedShort();
                        MultiplexConnectionInfo multiplexConnectionInfo4 = this.connectionTable.get(Integer.valueOf(unsignedShort4));
                        if (multiplexConnectionInfo4 == null) {
                            throw new IOException("REQUEST: Invalid connection ID");
                        }
                        int i2 = this.dataIn.readInt();
                        if (multiplexLog.isLoggable(Log.VERBOSE)) {
                            multiplexLog.log(Log.VERBOSE, "operation  REQUEST " + unsignedShort4 + ": " + i2);
                        }
                        multiplexConnectionInfo4.out.request(i2);
                        continue;
                    case 229:
                        int unsignedShort5 = this.dataIn.readUnsignedShort();
                        MultiplexConnectionInfo multiplexConnectionInfo5 = this.connectionTable.get(Integer.valueOf(unsignedShort5));
                        if (multiplexConnectionInfo5 == null) {
                            throw new IOException("SEND: Invalid connection ID");
                        }
                        int i3 = this.dataIn.readInt();
                        if (multiplexLog.isLoggable(Log.VERBOSE)) {
                            multiplexLog.log(Log.VERBOSE, "operation  TRANSMIT " + unsignedShort5 + ": " + i3);
                        }
                        multiplexConnectionInfo5.in.receive(i3, this.dataIn);
                        continue;
                    default:
                        throw new IOException("Invalid operation: " + Integer.toHexString(unsignedByte));
                }
            } catch (Throwable th) {
                shutDown();
                throw th;
            }
            shutDown();
            throw th;
        }
    }

    public synchronized TCPConnection openConnection() throws IOException {
        int i2;
        do {
            int i3 = this.lastID + 1;
            this.lastID = i3;
            this.lastID = i3 & Short.MAX_VALUE;
            i2 = this.lastID;
            if (this.orig) {
                i2 |= 32768;
            }
        } while (this.connectionTable.get(Integer.valueOf(i2)) != null);
        MultiplexConnectionInfo multiplexConnectionInfo = new MultiplexConnectionInfo(i2);
        multiplexConnectionInfo.in = new MultiplexInputStream(this, multiplexConnectionInfo, 2048);
        multiplexConnectionInfo.out = new MultiplexOutputStream(this, multiplexConnectionInfo, 2048);
        synchronized (this.connectionTable) {
            if (!this.alive) {
                throw new IOException("Multiplexer connection dead");
            }
            if (this.numConnections >= 256) {
                throw new IOException("Cannot exceed 256 simultaneous multiplexed connections");
            }
            this.connectionTable.put(Integer.valueOf(i2), multiplexConnectionInfo);
            this.numConnections++;
        }
        synchronized (this.dataOut) {
            try {
                this.dataOut.writeByte(225);
                this.dataOut.writeShort(i2);
                this.dataOut.flush();
            } catch (IOException e2) {
                multiplexLog.log(Log.BRIEF, "exception: ", e2);
                shutDown();
                throw e2;
            }
        }
        return new TCPConnection(this.channel, multiplexConnectionInfo.in, multiplexConnectionInfo.out);
    }

    public void shutDown() {
        synchronized (this.connectionTable) {
            if (this.alive) {
                this.alive = false;
                Enumeration<MultiplexConnectionInfo> enumerationElements = this.connectionTable.elements();
                while (enumerationElements.hasMoreElements()) {
                    MultiplexConnectionInfo multiplexConnectionInfoNextElement = enumerationElements.nextElement2();
                    multiplexConnectionInfoNextElement.in.disconnect();
                    multiplexConnectionInfoNextElement.out.disconnect();
                }
                this.connectionTable.clear();
                this.numConnections = 0;
                try {
                    this.in.close();
                } catch (IOException e2) {
                }
                try {
                    this.out.close();
                } catch (IOException e3) {
                }
            }
        }
    }

    void sendRequest(MultiplexConnectionInfo multiplexConnectionInfo, int i2) throws IOException {
        synchronized (this.dataOut) {
            if (this.alive && !multiplexConnectionInfo.closed) {
                try {
                    this.dataOut.writeByte(228);
                    this.dataOut.writeShort(multiplexConnectionInfo.id);
                    this.dataOut.writeInt(i2);
                    this.dataOut.flush();
                } catch (IOException e2) {
                    multiplexLog.log(Log.BRIEF, "exception: ", e2);
                    shutDown();
                    throw e2;
                }
            }
        }
    }

    void sendTransmit(MultiplexConnectionInfo multiplexConnectionInfo, byte[] bArr, int i2, int i3) throws IOException {
        synchronized (this.dataOut) {
            if (this.alive && !multiplexConnectionInfo.closed) {
                try {
                    this.dataOut.writeByte(229);
                    this.dataOut.writeShort(multiplexConnectionInfo.id);
                    this.dataOut.writeInt(i3);
                    this.dataOut.write(bArr, i2, i3);
                    this.dataOut.flush();
                } catch (IOException e2) {
                    multiplexLog.log(Log.BRIEF, "exception: ", e2);
                    shutDown();
                    throw e2;
                }
            }
        }
    }

    void sendClose(MultiplexConnectionInfo multiplexConnectionInfo) throws IOException {
        multiplexConnectionInfo.out.disconnect();
        synchronized (this.dataOut) {
            if (this.alive && !multiplexConnectionInfo.closed) {
                try {
                    this.dataOut.writeByte(226);
                    this.dataOut.writeShort(multiplexConnectionInfo.id);
                    this.dataOut.flush();
                    multiplexConnectionInfo.closed = true;
                } catch (IOException e2) {
                    multiplexLog.log(Log.BRIEF, "exception: ", e2);
                    shutDown();
                    throw e2;
                }
            }
        }
    }

    void sendCloseAck(MultiplexConnectionInfo multiplexConnectionInfo) throws IOException {
        synchronized (this.dataOut) {
            if (this.alive && !multiplexConnectionInfo.closed) {
                try {
                    this.dataOut.writeByte(227);
                    this.dataOut.writeShort(multiplexConnectionInfo.id);
                    this.dataOut.flush();
                    multiplexConnectionInfo.closed = true;
                } catch (IOException e2) {
                    multiplexLog.log(Log.BRIEF, "exception: ", e2);
                    shutDown();
                    throw e2;
                }
            }
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        shutDown();
    }
}
