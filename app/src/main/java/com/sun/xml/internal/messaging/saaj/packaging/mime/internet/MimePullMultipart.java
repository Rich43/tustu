package com.sun.xml.internal.messaging.saaj.packaging.mime.internet;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import com.sun.xml.internal.messaging.saaj.soap.AttachmentPartImpl;
import com.sun.xml.internal.org.jvnet.mimepull.MIMEConfig;
import com.sun.xml.internal.org.jvnet.mimepull.MIMEMessage;
import com.sun.xml.internal.org.jvnet.mimepull.MIMEPart;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.activation.DataSource;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/packaging/mime/internet/MimePullMultipart.class */
public class MimePullMultipart extends MimeMultipart {
    private String boundary;
    private DataSource dataSource;
    private ContentType contType;
    private InputStream in = null;
    private MIMEMessage mm = null;
    private String startParam = null;
    private MIMEPart soapPart = null;

    public MimePullMultipart(DataSource ds, ContentType ct) throws MessagingException {
        this.boundary = null;
        this.dataSource = null;
        this.contType = null;
        this.parsed = false;
        if (ct == null) {
            this.contType = new ContentType(ds.getContentType());
        } else {
            this.contType = ct;
        }
        this.dataSource = ds;
        this.boundary = this.contType.getParameter("boundary");
    }

    public MIMEPart readAndReturnSOAPPart() throws MessagingException {
        if (this.soapPart != null) {
            throw new MessagingException("Inputstream from datasource was already consumed");
        }
        readSOAPPart();
        return this.soapPart;
    }

    protected void readSOAPPart() throws MessagingException {
        try {
            if (this.soapPart != null) {
                return;
            }
            this.in = this.dataSource.getInputStream();
            MIMEConfig config = new MIMEConfig();
            this.mm = new MIMEMessage(this.in, this.boundary, config);
            String st = this.contType.getParameter("start");
            if (this.startParam == null) {
                this.soapPart = this.mm.getPart(0);
            } else {
                if (st != null && st.length() > 2 && st.charAt(0) == '<' && st.charAt(st.length() - 1) == '>') {
                    st = st.substring(1, st.length() - 1);
                }
                this.startParam = st;
                this.soapPart = this.mm.getPart(this.startParam);
            }
        } catch (IOException ex) {
            throw new MessagingException("No inputstream from datasource", ex);
        }
    }

    public void parseAll() throws MessagingException {
        if (this.parsed) {
            return;
        }
        if (this.soapPart == null) {
            readSOAPPart();
        }
        List<MIMEPart> prts = this.mm.getAttachments();
        for (MIMEPart part : prts) {
            if (part != this.soapPart) {
                new AttachmentPartImpl(part);
                addBodyPart(new MimeBodyPart(part));
            }
        }
        this.parsed = true;
    }

    @Override // com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeMultipart
    protected void parse() throws MessagingException {
        parseAll();
    }
}
