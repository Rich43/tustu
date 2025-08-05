package gnu.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: RXTXcomm.jar:gnu/io/CommPort.class */
public abstract class CommPort {
    protected String name;
    private static final boolean debug = false;

    public abstract void enableReceiveFraming(int i2) throws UnsupportedCommOperationException;

    public abstract void disableReceiveFraming();

    public abstract boolean isReceiveFramingEnabled();

    public abstract int getReceiveFramingByte();

    public abstract void disableReceiveTimeout();

    public abstract void enableReceiveTimeout(int i2) throws UnsupportedCommOperationException;

    public abstract boolean isReceiveTimeoutEnabled();

    public abstract int getReceiveTimeout();

    public abstract void enableReceiveThreshold(int i2) throws UnsupportedCommOperationException;

    public abstract void disableReceiveThreshold();

    public abstract int getReceiveThreshold();

    public abstract boolean isReceiveThresholdEnabled();

    public abstract void setInputBufferSize(int i2);

    public abstract int getInputBufferSize();

    public abstract void setOutputBufferSize(int i2);

    public abstract int getOutputBufferSize();

    public abstract InputStream getInputStream() throws IOException;

    public abstract OutputStream getOutputStream() throws IOException;

    public void close() {
        try {
            if (CommPortIdentifier.getPortIdentifier(this) != null) {
                CommPortIdentifier.getPortIdentifier(this).internalClosePort();
            }
        } catch (NoSuchPortException e2) {
        }
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }
}
