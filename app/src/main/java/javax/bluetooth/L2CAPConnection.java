package javax.bluetooth;

import java.io.IOException;
import javax.microedition.io.Connection;

/* loaded from: bluecove-2.1.1.jar:javax/bluetooth/L2CAPConnection.class */
public interface L2CAPConnection extends Connection {
    public static final int DEFAULT_MTU = 672;
    public static final int MINIMUM_MTU = 48;

    int getTransmitMTU() throws IOException;

    int getReceiveMTU() throws IOException;

    void send(byte[] bArr) throws IOException;

    int receive(byte[] bArr) throws IOException;

    boolean ready() throws IOException;
}
