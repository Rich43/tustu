package javax.activation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: rt.jar:javax/activation/DataSource.class */
public interface DataSource {
    InputStream getInputStream() throws IOException;

    OutputStream getOutputStream() throws IOException;

    String getContentType();

    String getName();
}
