package javax.bluetooth;

import java.io.IOException;
import javax.microedition.io.Connection;

/* loaded from: bluecove-2.1.1.jar:javax/bluetooth/L2CAPConnectionNotifier.class */
public interface L2CAPConnectionNotifier extends Connection {
    L2CAPConnection acceptAndOpen() throws IOException;
}
