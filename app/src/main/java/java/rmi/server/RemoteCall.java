package java.rmi.server;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

@Deprecated
/* loaded from: rt.jar:java/rmi/server/RemoteCall.class */
public interface RemoteCall {
    @Deprecated
    ObjectOutput getOutputStream() throws IOException;

    @Deprecated
    void releaseOutputStream() throws IOException;

    @Deprecated
    ObjectInput getInputStream() throws IOException;

    @Deprecated
    void releaseInputStream() throws IOException;

    @Deprecated
    ObjectOutput getResultStream(boolean z2) throws IOException;

    @Deprecated
    void executeCall() throws Exception;

    @Deprecated
    void done() throws IOException;
}
