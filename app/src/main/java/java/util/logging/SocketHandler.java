package java.util.logging;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:java/util/logging/SocketHandler.class */
public class SocketHandler extends StreamHandler {
    private Socket sock;
    private String host;
    private int port;

    private void configure() {
        LogManager logManager = LogManager.getLogManager();
        String name = getClass().getName();
        setLevel(logManager.getLevelProperty(name + ".level", Level.ALL));
        setFilter(logManager.getFilterProperty(name + ".filter", null));
        setFormatter(logManager.getFormatterProperty(name + ".formatter", new XMLFormatter()));
        try {
            setEncoding(logManager.getStringProperty(name + ".encoding", null));
        } catch (Exception e2) {
            try {
                setEncoding(null);
            } catch (Exception e3) {
            }
        }
        this.port = logManager.getIntProperty(name + ".port", 0);
        this.host = logManager.getStringProperty(name + ".host", null);
    }

    public SocketHandler() throws IOException {
        this.sealed = false;
        configure();
        try {
            connect();
            this.sealed = true;
        } catch (IOException e2) {
            System.err.println("SocketHandler: connect failed to " + this.host + CallSiteDescriptor.TOKEN_DELIMITER + this.port);
            throw e2;
        }
    }

    public SocketHandler(String str, int i2) throws IOException {
        this.sealed = false;
        configure();
        this.sealed = true;
        this.port = i2;
        this.host = str;
        connect();
    }

    private void connect() throws IOException {
        if (this.port == 0) {
            throw new IllegalArgumentException("Bad port: " + this.port);
        }
        if (this.host == null) {
            throw new IllegalArgumentException("Null host name: " + this.host);
        }
        this.sock = new Socket(this.host, this.port);
        setOutputStream(new BufferedOutputStream(this.sock.getOutputStream()));
    }

    @Override // java.util.logging.StreamHandler, java.util.logging.Handler
    public synchronized void close() throws SecurityException {
        super.close();
        if (this.sock != null) {
            try {
                this.sock.close();
            } catch (IOException e2) {
            }
        }
        this.sock = null;
    }

    @Override // java.util.logging.StreamHandler, java.util.logging.Handler
    public synchronized void publish(LogRecord logRecord) {
        if (!isLoggable(logRecord)) {
            return;
        }
        super.publish(logRecord);
        flush();
    }
}
