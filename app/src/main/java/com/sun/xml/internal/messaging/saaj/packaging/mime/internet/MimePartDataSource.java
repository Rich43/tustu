package com.sun.xml.internal.messaging.saaj.packaging.mime.internet;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownServiceException;
import javax.activation.DataSource;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/packaging/mime/internet/MimePartDataSource.class */
public final class MimePartDataSource implements DataSource {
    private final MimeBodyPart part;

    public MimePartDataSource(MimeBodyPart part) {
        this.part = part;
    }

    @Override // javax.activation.DataSource
    public InputStream getInputStream() throws IOException {
        try {
            InputStream is = this.part.getContentStream();
            String encoding = this.part.getEncoding();
            if (encoding != null) {
                return MimeUtility.decode(is, encoding);
            }
            return is;
        } catch (MessagingException mex) {
            throw new IOException(mex.getMessage());
        }
    }

    @Override // javax.activation.DataSource
    public OutputStream getOutputStream() throws IOException {
        throw new UnknownServiceException();
    }

    @Override // javax.activation.DataSource
    public String getContentType() {
        return this.part.getContentType();
    }

    @Override // javax.activation.DataSource
    public String getName() {
        try {
            return this.part.getFileName();
        } catch (MessagingException e2) {
            return "";
        }
    }
}
