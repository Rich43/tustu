package javax.activation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* compiled from: DataHandler.java */
/* loaded from: rt.jar:javax/activation/DataHandlerDataSource.class */
class DataHandlerDataSource implements DataSource {
    DataHandler dataHandler;

    public DataHandlerDataSource(DataHandler dh) {
        this.dataHandler = null;
        this.dataHandler = dh;
    }

    @Override // javax.activation.DataSource
    public InputStream getInputStream() throws IOException {
        return this.dataHandler.getInputStream();
    }

    @Override // javax.activation.DataSource
    public OutputStream getOutputStream() throws IOException {
        return this.dataHandler.getOutputStream();
    }

    @Override // javax.activation.DataSource
    public String getContentType() {
        return this.dataHandler.getContentType();
    }

    @Override // javax.activation.DataSource
    public String getName() {
        return this.dataHandler.getName();
    }
}
