package com.sun.xml.internal.messaging.saaj.soap;

import java.awt.Component;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataContentHandler;
import javax.activation.DataSource;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/GifDataContentHandler.class */
public class GifDataContentHandler extends Component implements DataContentHandler {
    private static ActivationDataFlavor myDF = new ActivationDataFlavor(Image.class, "image/gif", "GIF Image");

    protected ActivationDataFlavor getDF() {
        return myDF;
    }

    @Override // javax.activation.DataContentHandler
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{getDF()};
    }

    @Override // javax.activation.DataContentHandler
    public Object getTransferData(DataFlavor df, DataSource ds) throws IOException {
        if (getDF().equals(df)) {
            return getContent(ds);
        }
        return null;
    }

    @Override // javax.activation.DataContentHandler
    public Object getContent(DataSource ds) throws IOException {
        int i2;
        int i3;
        InputStream is = ds.getInputStream();
        int pos = 0;
        byte[] buf = new byte[1024];
        while (true) {
            int count = is.read(buf, pos, buf.length - pos);
            if (count != -1) {
                pos += count;
                if (pos >= buf.length) {
                    int size = buf.length;
                    if (size < 262144) {
                        i2 = size;
                        i3 = size;
                    } else {
                        i2 = size;
                        i3 = 262144;
                    }
                    byte[] tbuf = new byte[i2 + i3];
                    System.arraycopy(buf, 0, tbuf, 0, pos);
                    buf = tbuf;
                }
            } else {
                Toolkit tk = Toolkit.getDefaultToolkit();
                return tk.createImage(buf, 0, pos);
            }
        }
    }

    @Override // javax.activation.DataContentHandler
    public void writeTo(Object obj, String type, OutputStream os) throws IOException {
        if (obj != null && !(obj instanceof Image)) {
            throw new IOException(PdfOps.DOUBLE_QUOTE__TOKEN + getDF().getMimeType() + "\" DataContentHandler requires Image object, was given object of type " + obj.getClass().toString());
        }
        throw new IOException(getDF().getMimeType() + " encoding not supported");
    }
}
