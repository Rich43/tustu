package com.sun.xml.internal.ws.encoding;

import com.sun.xml.internal.ws.developer.StreamingDataHandler;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataSource;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/DataSourceStreamingDataHandler.class */
public class DataSourceStreamingDataHandler extends StreamingDataHandler {
    public DataSourceStreamingDataHandler(DataSource ds) {
        super(ds);
    }

    @Override // com.sun.xml.internal.org.jvnet.staxex.StreamingDataHandler
    public InputStream readOnce() throws IOException {
        return getInputStream();
    }

    @Override // com.sun.xml.internal.org.jvnet.staxex.StreamingDataHandler
    public void moveTo(File file) throws IOException {
        InputStream in = getInputStream();
        OutputStream os = new FileOutputStream(file);
        try {
            byte[] temp = new byte[8192];
            while (true) {
                int len = in.read(temp);
                if (len == -1) {
                    break;
                } else {
                    os.write(temp, 0, len);
                }
            }
            in.close();
            if (os != null) {
                os.close();
            }
        } catch (Throwable th) {
            if (os != null) {
                os.close();
            }
            throw th;
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.staxex.StreamingDataHandler, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
    }
}
