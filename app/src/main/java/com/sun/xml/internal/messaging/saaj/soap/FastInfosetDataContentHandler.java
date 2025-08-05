package com.sun.xml.internal.messaging.saaj.soap;

import com.sun.xml.internal.messaging.saaj.util.FastInfosetReflection;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataContentHandler;
import javax.activation.DataSource;
import javax.xml.transform.Source;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/FastInfosetDataContentHandler.class */
public class FastInfosetDataContentHandler implements DataContentHandler {
    public static final String STR_SRC = "com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSource";

    @Override // javax.activation.DataContentHandler
    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] flavors = {new ActivationDataFlavor(FastInfosetReflection.getFastInfosetSource_class(), "application/fastinfoset", "Fast Infoset")};
        return flavors;
    }

    @Override // javax.activation.DataContentHandler
    public Object getTransferData(DataFlavor flavor, DataSource dataSource) throws IOException {
        if (flavor.getMimeType().startsWith("application/fastinfoset")) {
            try {
                if (flavor.getRepresentationClass().getName().equals(STR_SRC)) {
                    return FastInfosetReflection.FastInfosetSource_new(dataSource.getInputStream());
                }
                return null;
            } catch (Exception e2) {
                throw new IOException(e2.getMessage());
            }
        }
        return null;
    }

    @Override // javax.activation.DataContentHandler
    public Object getContent(DataSource dataSource) throws IOException {
        try {
            return FastInfosetReflection.FastInfosetSource_new(dataSource.getInputStream());
        } catch (Exception e2) {
            throw new IOException(e2.getMessage());
        }
    }

    @Override // javax.activation.DataContentHandler
    public void writeTo(Object obj, String mimeType, OutputStream os) throws IOException {
        if (!mimeType.equals("application/fastinfoset")) {
            throw new IOException("Invalid content type \"" + mimeType + "\" for FastInfosetDCH");
        }
        try {
            InputStream is = FastInfosetReflection.FastInfosetSource_getInputStream((Source) obj);
            byte[] buffer = new byte[4096];
            while (true) {
                int n2 = is.read(buffer);
                if (n2 != -1) {
                    os.write(buffer, 0, n2);
                } else {
                    return;
                }
            }
        } catch (Exception ex) {
            throw new IOException("Error copying FI source to output stream " + ex.getMessage());
        }
    }
}
