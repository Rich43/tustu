package javax.activation;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: rt.jar:javax/activation/DataContentHandler.class */
public interface DataContentHandler {
    DataFlavor[] getTransferDataFlavors();

    Object getTransferData(DataFlavor dataFlavor, DataSource dataSource) throws UnsupportedFlavorException, IOException;

    Object getContent(DataSource dataSource) throws IOException;

    void writeTo(Object obj, String str, OutputStream outputStream) throws IOException;
}
