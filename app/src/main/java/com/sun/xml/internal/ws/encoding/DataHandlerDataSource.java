package com.sun.xml.internal.ws.encoding;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataHandler;
import javax.activation.DataSource;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/DataHandlerDataSource.class */
public class DataHandlerDataSource implements DataSource {
    private final DataHandler dataHandler;

    public DataHandlerDataSource(DataHandler dh) {
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
