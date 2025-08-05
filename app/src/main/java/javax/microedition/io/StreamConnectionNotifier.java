package javax.microedition.io;

import java.io.IOException;

/* loaded from: bluecove-2.1.1.jar:javax/microedition/io/StreamConnectionNotifier.class */
public interface StreamConnectionNotifier extends Connection {
    StreamConnection acceptAndOpen() throws IOException;
}
