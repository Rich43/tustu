package javax.microedition.io;

import com.intel.bluetooth.DebugLog;
import com.intel.bluetooth.MicroeditionConnector;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: bluecove-2.1.1.jar:javax/microedition/io/Connector.class */
public class Connector {
    public static final int READ = 1;
    public static final int WRITE = 2;
    public static final int READ_WRITE = 3;

    private Connector() {
    }

    public static Connection open(String name) throws IOException {
        DebugLog.debug("open using BlueCove javax.microedition.io.Connector");
        return MicroeditionConnector.open(name);
    }

    public static Connection open(String name, int mode) throws IOException {
        return MicroeditionConnector.open(name, mode);
    }

    public static Connection open(String name, int mode, boolean timeouts) throws IOException {
        return MicroeditionConnector.open(name, mode, timeouts);
    }

    public static DataInputStream openDataInputStream(String name) throws IOException {
        return MicroeditionConnector.openDataInputStream(name);
    }

    public static DataOutputStream openDataOutputStream(String name) throws IOException {
        return MicroeditionConnector.openDataOutputStream(name);
    }

    public static InputStream openInputStream(String name) throws IOException {
        return MicroeditionConnector.openInputStream(name);
    }

    public static OutputStream openOutputStream(String name) throws IOException {
        return MicroeditionConnector.openOutputStream(name);
    }
}
