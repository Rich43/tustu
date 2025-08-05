package com.sun.xml.internal.messaging.saaj.soap;

import com.sun.glass.ui.Clipboard;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ContentType;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataContentHandler;
import javax.activation.DataSource;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/StringDataContentHandler.class */
public class StringDataContentHandler implements DataContentHandler {
    private static ActivationDataFlavor myDF = new ActivationDataFlavor(String.class, Clipboard.TEXT_TYPE, "Text String");

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
        int size;
        String enc = null;
        try {
            enc = getCharset(ds.getContentType());
            InputStreamReader is = new InputStreamReader(ds.getInputStream(), enc);
            try {
                int pos = 0;
                char[] buf = new char[1024];
                while (true) {
                    int count = is.read(buf, pos, buf.length - pos);
                    if (count == -1) {
                        break;
                    }
                    pos += count;
                    if (pos >= buf.length) {
                        int size2 = buf.length;
                        if (size2 < 262144) {
                            size = size2 + size2;
                        } else {
                            size = size2 + 262144;
                        }
                        char[] tbuf = new char[size];
                        System.arraycopy(buf, 0, tbuf, 0, pos);
                        buf = tbuf;
                    }
                }
                return new String(buf, 0, pos);
            } finally {
                try {
                    is.close();
                } catch (IOException e2) {
                }
            }
        } catch (IllegalArgumentException e3) {
            throw new UnsupportedEncodingException(enc);
        }
    }

    @Override // javax.activation.DataContentHandler
    public void writeTo(Object obj, String type, OutputStream os) throws IOException {
        if (!(obj instanceof String)) {
            throw new IOException(PdfOps.DOUBLE_QUOTE__TOKEN + getDF().getMimeType() + "\" DataContentHandler requires String object, was given object of type " + obj.getClass().toString());
        }
        String enc = null;
        try {
            enc = getCharset(type);
            OutputStreamWriter osw = new OutputStreamWriter(os, enc);
            String s2 = (String) obj;
            osw.write(s2, 0, s2.length());
            osw.flush();
        } catch (IllegalArgumentException e2) {
            throw new UnsupportedEncodingException(enc);
        }
    }

    private String getCharset(String type) {
        try {
            ContentType ct = new ContentType(type);
            String charset = ct.getParameter("charset");
            if (charset == null) {
                charset = "us-ascii";
            }
            return MimeUtility.javaCharset(charset);
        } catch (Exception e2) {
            return null;
        }
    }
}
