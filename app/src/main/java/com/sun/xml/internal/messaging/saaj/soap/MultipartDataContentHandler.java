package com.sun.xml.internal.messaging.saaj.soap;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ContentType;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeMultipart;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.io.OutputStream;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataContentHandler;
import javax.activation.DataSource;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/MultipartDataContentHandler.class */
public class MultipartDataContentHandler implements DataContentHandler {
    private ActivationDataFlavor myDF = new ActivationDataFlavor(MimeMultipart.class, "multipart/mixed", "Multipart");

    @Override // javax.activation.DataContentHandler
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{this.myDF};
    }

    @Override // javax.activation.DataContentHandler
    public Object getTransferData(DataFlavor df, DataSource ds) {
        if (this.myDF.equals(df)) {
            return getContent(ds);
        }
        return null;
    }

    @Override // javax.activation.DataContentHandler
    public Object getContent(DataSource ds) {
        try {
            return new MimeMultipart(ds, new ContentType(ds.getContentType()));
        } catch (Exception e2) {
            return null;
        }
    }

    @Override // javax.activation.DataContentHandler
    public void writeTo(Object obj, String mimeType, OutputStream os) throws IOException {
        if (obj instanceof MimeMultipart) {
            try {
                if (os instanceof ByteOutputStream) {
                    ByteOutputStream baos = (ByteOutputStream) os;
                    ((MimeMultipart) obj).writeTo(baos);
                    return;
                }
                throw new IOException("Input Stream expected to be a com.sun.xml.internal.messaging.saaj.util.ByteOutputStream, but found " + os.getClass().getName());
            } catch (Exception e2) {
                throw new IOException(e2.toString());
            }
        }
    }
}
