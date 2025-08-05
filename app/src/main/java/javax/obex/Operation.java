package javax.obex;

import java.io.IOException;
import javax.microedition.io.ContentConnection;

/* loaded from: bluecove-2.1.1.jar:javax/obex/Operation.class */
public interface Operation extends ContentConnection {
    void abort() throws IOException;

    HeaderSet getReceivedHeaders() throws IOException;

    void sendHeaders(HeaderSet headerSet) throws IOException;

    int getResponseCode() throws IOException;
}
