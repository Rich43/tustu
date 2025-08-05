package javax.microedition.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: bluecove-2.1.1.jar:javax/microedition/io/OutputConnection.class */
public interface OutputConnection extends Connection {
    OutputStream openOutputStream() throws IOException;

    DataOutputStream openDataOutputStream() throws IOException;
}
