package com.sun.xml.internal.messaging.saaj.soap;

import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.InternetHeaders;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeBodyPart;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimePartDataSource;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.ASCIIUtility;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import com.sun.xml.internal.messaging.saaj.util.LogDomainConstants;
import com.sun.xml.internal.org.jvnet.mimepull.Header;
import com.sun.xml.internal.org.jvnet.mimepull.MIMEPart;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.CommandInfo;
import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.MailcapCommandMap;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.MimeHeader;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/AttachmentPartImpl.class */
public class AttachmentPartImpl extends AttachmentPart {
    protected static final Logger log = Logger.getLogger(LogDomainConstants.SOAP_DOMAIN, "com.sun.xml.internal.messaging.saaj.soap.LocalStrings");
    private final MimeHeaders headers;
    private MimeBodyPart rawContent;
    private DataHandler dataHandler;
    private MIMEPart mimePart;

    public AttachmentPartImpl() {
        this.rawContent = null;
        this.dataHandler = null;
        this.mimePart = null;
        this.headers = new MimeHeaders();
        initializeJavaActivationHandlers();
    }

    public AttachmentPartImpl(MIMEPart part) {
        this.rawContent = null;
        this.dataHandler = null;
        this.mimePart = null;
        this.headers = new MimeHeaders();
        this.mimePart = part;
        List<? extends Header> hdrs = part.getAllHeaders();
        for (Header hd : hdrs) {
            this.headers.addHeader(hd.getName(), hd.getValue());
        }
    }

    @Override // javax.xml.soap.AttachmentPart
    public int getSize() throws SOAPException {
        if (this.mimePart != null) {
            try {
                return this.mimePart.read().available();
            } catch (IOException e2) {
                return -1;
            }
        }
        if (this.rawContent == null && this.dataHandler == null) {
            return 0;
        }
        if (this.rawContent != null) {
            try {
                return this.rawContent.getSize();
            } catch (Exception ex) {
                log.log(Level.SEVERE, "SAAJ0573.soap.attachment.getrawbytes.ioexception", (Object[]) new String[]{ex.getLocalizedMessage()});
                throw new SOAPExceptionImpl("Raw InputStream Error: " + ((Object) ex));
            }
        }
        ByteOutputStream bout = new ByteOutputStream();
        try {
            this.dataHandler.writeTo(bout);
            return bout.size();
        } catch (IOException ex2) {
            log.log(Level.SEVERE, "SAAJ0501.soap.data.handler.err", (Object[]) new String[]{ex2.getLocalizedMessage()});
            throw new SOAPExceptionImpl("Data handler error: " + ((Object) ex2));
        }
    }

    @Override // javax.xml.soap.AttachmentPart
    public void clearContent() {
        if (this.mimePart != null) {
            this.mimePart.close();
            this.mimePart = null;
        }
        this.dataHandler = null;
        this.rawContent = null;
    }

    @Override // javax.xml.soap.AttachmentPart
    public Object getContent() throws SOAPException {
        try {
            if (this.mimePart != null) {
                return this.mimePart.read();
            }
            if (this.dataHandler != null) {
                return getDataHandler().getContent();
            }
            if (this.rawContent != null) {
                return this.rawContent.getContent();
            }
            log.severe("SAAJ0572.soap.no.content.for.attachment");
            throw new SOAPExceptionImpl("No data handler/content associated with this attachment");
        } catch (Exception ex) {
            log.log(Level.SEVERE, "SAAJ0575.soap.attachment.getcontent.exception", (Throwable) ex);
            throw new SOAPExceptionImpl(ex.getLocalizedMessage());
        }
    }

    @Override // javax.xml.soap.AttachmentPart
    public void setContent(Object object, String contentType) throws IllegalArgumentException {
        if (this.mimePart != null) {
            this.mimePart.close();
            this.mimePart = null;
        }
        DataHandler dh = new DataHandler(object, contentType);
        setDataHandler(dh);
    }

    @Override // javax.xml.soap.AttachmentPart
    public DataHandler getDataHandler() throws SOAPException {
        if (this.mimePart != null) {
            return new DataHandler(new DataSource() { // from class: com.sun.xml.internal.messaging.saaj.soap.AttachmentPartImpl.1
                @Override // javax.activation.DataSource
                public InputStream getInputStream() throws IOException {
                    return AttachmentPartImpl.this.mimePart.read();
                }

                @Override // javax.activation.DataSource
                public OutputStream getOutputStream() throws IOException {
                    throw new UnsupportedOperationException("getOutputStream cannot be supported : You have enabled LazyAttachments Option");
                }

                @Override // javax.activation.DataSource
                public String getContentType() {
                    return AttachmentPartImpl.this.mimePart.getContentType();
                }

                @Override // javax.activation.DataSource
                public String getName() {
                    return "MIMEPart Wrapper DataSource";
                }
            });
        }
        if (this.dataHandler == null) {
            if (this.rawContent != null) {
                return new DataHandler(new MimePartDataSource(this.rawContent));
            }
            log.severe("SAAJ0502.soap.no.handler.for.attachment");
            throw new SOAPExceptionImpl("No data handler associated with this attachment");
        }
        return this.dataHandler;
    }

    @Override // javax.xml.soap.AttachmentPart
    public void setDataHandler(DataHandler dataHandler) throws IllegalArgumentException {
        if (this.mimePart != null) {
            this.mimePart.close();
            this.mimePart = null;
        }
        if (dataHandler == null) {
            log.severe("SAAJ0503.soap.no.null.to.dataHandler");
            throw new IllegalArgumentException("Null dataHandler argument to setDataHandler");
        }
        this.dataHandler = dataHandler;
        this.rawContent = null;
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "SAAJ0580.soap.set.Content-Type", (Object[]) new String[]{dataHandler.getContentType()});
        }
        setMimeHeader("Content-Type", dataHandler.getContentType());
    }

    @Override // javax.xml.soap.AttachmentPart
    public void removeAllMimeHeaders() {
        this.headers.removeAllHeaders();
    }

    @Override // javax.xml.soap.AttachmentPart
    public void removeMimeHeader(String header) {
        this.headers.removeHeader(header);
    }

    @Override // javax.xml.soap.AttachmentPart
    public String[] getMimeHeader(String name) {
        return this.headers.getHeader(name);
    }

    @Override // javax.xml.soap.AttachmentPart
    public void setMimeHeader(String name, String value) {
        this.headers.setHeader(name, value);
    }

    @Override // javax.xml.soap.AttachmentPart
    public void addMimeHeader(String name, String value) {
        this.headers.addHeader(name, value);
    }

    @Override // javax.xml.soap.AttachmentPart
    public Iterator getAllMimeHeaders() {
        return this.headers.getAllHeaders();
    }

    @Override // javax.xml.soap.AttachmentPart
    public Iterator getMatchingMimeHeaders(String[] names) {
        return this.headers.getMatchingHeaders(names);
    }

    @Override // javax.xml.soap.AttachmentPart
    public Iterator getNonMatchingMimeHeaders(String[] names) {
        return this.headers.getNonMatchingHeaders(names);
    }

    boolean hasAllHeaders(MimeHeaders hdrs) {
        if (hdrs != null) {
            Iterator i2 = hdrs.getAllHeaders();
            while (i2.hasNext()) {
                MimeHeader hdr = (MimeHeader) i2.next();
                String[] values = this.headers.getHeader(hdr.getName());
                boolean found = false;
                if (values != null) {
                    int j2 = 0;
                    while (true) {
                        if (j2 >= values.length) {
                            break;
                        }
                        if (!hdr.getValue().equalsIgnoreCase(values[j2])) {
                            j2++;
                        } else {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    MimeBodyPart getMimePart() throws SOAPException {
        try {
            if (this.mimePart != null) {
                return new MimeBodyPart(this.mimePart);
            }
            if (this.rawContent != null) {
                copyMimeHeaders(this.headers, this.rawContent);
                return this.rawContent;
            }
            MimeBodyPart envelope = new MimeBodyPart();
            envelope.setDataHandler(this.dataHandler);
            copyMimeHeaders(this.headers, envelope);
            return envelope;
        } catch (Exception ex) {
            log.severe("SAAJ0504.soap.cannot.externalize.attachment");
            throw new SOAPExceptionImpl("Unable to externalize attachment", ex);
        }
    }

    public static void copyMimeHeaders(MimeHeaders headers, MimeBodyPart mbp) throws SOAPException {
        Iterator i2 = headers.getAllHeaders();
        while (i2.hasNext()) {
            try {
                MimeHeader mh = (MimeHeader) i2.next();
                mbp.setHeader(mh.getName(), mh.getValue());
            } catch (Exception ex) {
                log.severe("SAAJ0505.soap.cannot.copy.mime.hdr");
                throw new SOAPExceptionImpl("Unable to copy MIME header", ex);
            }
        }
    }

    public static void copyMimeHeaders(MimeBodyPart mbp, AttachmentPartImpl ap2) throws SOAPException {
        try {
            List hdr = mbp.getAllHeaders();
            int sz = hdr.size();
            for (int i2 = 0; i2 < sz; i2++) {
                com.sun.xml.internal.messaging.saaj.packaging.mime.Header h2 = (com.sun.xml.internal.messaging.saaj.packaging.mime.Header) hdr.get(i2);
                if (!h2.getName().equalsIgnoreCase("Content-Type")) {
                    ap2.addMimeHeader(h2.getName(), h2.getValue());
                }
            }
        } catch (Exception ex) {
            log.severe("SAAJ0506.soap.cannot.copy.mime.hdrs.into.attachment");
            throw new SOAPExceptionImpl("Unable to copy MIME headers into attachment", ex);
        }
    }

    @Override // javax.xml.soap.AttachmentPart
    public void setBase64Content(InputStream content, String contentType) throws SOAPException {
        if (this.mimePart != null) {
            this.mimePart.close();
            this.mimePart = null;
        }
        this.dataHandler = null;
        InputStream decoded = null;
        try {
            try {
                decoded = MimeUtility.decode(content, "base64");
                InternetHeaders hdrs = new InternetHeaders();
                hdrs.setHeader("Content-Type", contentType);
                ByteOutputStream bos = new ByteOutputStream();
                bos.write(decoded);
                this.rawContent = new MimeBodyPart(hdrs, bos.getBytes(), bos.getCount());
                setMimeHeader("Content-Type", contentType);
                try {
                    decoded.close();
                } catch (IOException ex) {
                    throw new SOAPException(ex);
                }
            } catch (Exception e2) {
                log.log(Level.SEVERE, "SAAJ0578.soap.attachment.setbase64content.exception", (Throwable) e2);
                throw new SOAPExceptionImpl(e2.getLocalizedMessage());
            }
        } catch (Throwable th) {
            try {
                decoded.close();
                throw th;
            } catch (IOException ex2) {
                throw new SOAPException(ex2);
            }
        }
    }

    @Override // javax.xml.soap.AttachmentPart
    public InputStream getBase64Content() throws SOAPException {
        InputStream stream;
        if (this.mimePart != null) {
            stream = this.mimePart.read();
        } else if (this.rawContent != null) {
            try {
                stream = this.rawContent.getInputStream();
            } catch (Exception e2) {
                log.log(Level.SEVERE, "SAAJ0579.soap.attachment.getbase64content.exception", (Throwable) e2);
                throw new SOAPExceptionImpl(e2.getLocalizedMessage());
            }
        } else if (this.dataHandler != null) {
            try {
                stream = this.dataHandler.getInputStream();
            } catch (IOException e3) {
                log.severe("SAAJ0574.soap.attachment.datahandler.ioexception");
                throw new SOAPExceptionImpl("DataHandler error" + ((Object) e3));
            }
        } else {
            log.severe("SAAJ0572.soap.no.content.for.attachment");
            throw new SOAPExceptionImpl("No data handler/content associated with this attachment");
        }
        try {
            if (stream != null) {
                try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
                    OutputStream ret = MimeUtility.encode(bos, "base64");
                    byte[] buf = new byte[1024];
                    while (true) {
                        int len = stream.read(buf, 0, 1024);
                        if (len == -1) {
                            break;
                        }
                        ret.write(buf, 0, len);
                    }
                    ret.flush();
                    return new ByteArrayInputStream(bos.toByteArray());
                } catch (Exception e4) {
                    log.log(Level.SEVERE, "SAAJ0579.soap.attachment.getbase64content.exception", (Throwable) e4);
                    throw new SOAPExceptionImpl(e4.getLocalizedMessage());
                }
            }
            log.log(Level.SEVERE, "SAAJ0572.soap.no.content.for.attachment");
            throw new SOAPExceptionImpl("No data handler/content associated with this attachment");
        } finally {
            try {
                stream.close();
            } catch (IOException e5) {
            }
        }
    }

    @Override // javax.xml.soap.AttachmentPart
    public void setRawContent(InputStream content, String contentType) throws SOAPException {
        if (this.mimePart != null) {
            this.mimePart.close();
            this.mimePart = null;
        }
        this.dataHandler = null;
        try {
            try {
                InternetHeaders hdrs = new InternetHeaders();
                hdrs.setHeader("Content-Type", contentType);
                ByteOutputStream bos = new ByteOutputStream();
                bos.write(content);
                this.rawContent = new MimeBodyPart(hdrs, bos.getBytes(), bos.getCount());
                setMimeHeader("Content-Type", contentType);
                try {
                    content.close();
                } catch (IOException ex) {
                    throw new SOAPException(ex);
                }
            } catch (Exception e2) {
                log.log(Level.SEVERE, "SAAJ0576.soap.attachment.setrawcontent.exception", (Throwable) e2);
                throw new SOAPExceptionImpl(e2.getLocalizedMessage());
            }
        } catch (Throwable th) {
            try {
                content.close();
                throw th;
            } catch (IOException ex2) {
                throw new SOAPException(ex2);
            }
        }
    }

    @Override // javax.xml.soap.AttachmentPart
    public void setRawContentBytes(byte[] content, int off, int len, String contentType) throws SOAPException {
        if (this.mimePart != null) {
            this.mimePart.close();
            this.mimePart = null;
        }
        if (content == null) {
            throw new SOAPExceptionImpl("Null content passed to setRawContentBytes");
        }
        this.dataHandler = null;
        try {
            InternetHeaders hdrs = new InternetHeaders();
            hdrs.setHeader("Content-Type", contentType);
            this.rawContent = new MimeBodyPart(hdrs, content, off, len);
            setMimeHeader("Content-Type", contentType);
        } catch (Exception e2) {
            log.log(Level.SEVERE, "SAAJ0576.soap.attachment.setrawcontent.exception", (Throwable) e2);
            throw new SOAPExceptionImpl(e2.getLocalizedMessage());
        }
    }

    @Override // javax.xml.soap.AttachmentPart
    public InputStream getRawContent() throws SOAPException {
        if (this.mimePart != null) {
            return this.mimePart.read();
        }
        if (this.rawContent != null) {
            try {
                return this.rawContent.getInputStream();
            } catch (Exception e2) {
                log.log(Level.SEVERE, "SAAJ0577.soap.attachment.getrawcontent.exception", (Throwable) e2);
                throw new SOAPExceptionImpl(e2.getLocalizedMessage());
            }
        }
        if (this.dataHandler != null) {
            try {
                return this.dataHandler.getInputStream();
            } catch (IOException e3) {
                log.severe("SAAJ0574.soap.attachment.datahandler.ioexception");
                throw new SOAPExceptionImpl("DataHandler error" + ((Object) e3));
            }
        }
        log.severe("SAAJ0572.soap.no.content.for.attachment");
        throw new SOAPExceptionImpl("No data handler/content associated with this attachment");
    }

    @Override // javax.xml.soap.AttachmentPart
    public byte[] getRawContentBytes() throws SOAPException {
        if (this.mimePart != null) {
            try {
                InputStream ret = this.mimePart.read();
                return ASCIIUtility.getBytes(ret);
            } catch (IOException ex) {
                log.log(Level.SEVERE, "SAAJ0577.soap.attachment.getrawcontent.exception", (Throwable) ex);
                throw new SOAPExceptionImpl(ex);
            }
        }
        if (this.rawContent != null) {
            try {
                InputStream ret2 = this.rawContent.getInputStream();
                return ASCIIUtility.getBytes(ret2);
            } catch (Exception e2) {
                log.log(Level.SEVERE, "SAAJ0577.soap.attachment.getrawcontent.exception", (Throwable) e2);
                throw new SOAPExceptionImpl(e2);
            }
        }
        if (this.dataHandler != null) {
            try {
                InputStream ret3 = this.dataHandler.getInputStream();
                return ASCIIUtility.getBytes(ret3);
            } catch (IOException e3) {
                log.severe("SAAJ0574.soap.attachment.datahandler.ioexception");
                throw new SOAPExceptionImpl("DataHandler error" + ((Object) e3));
            }
        }
        log.severe("SAAJ0572.soap.no.content.for.attachment");
        throw new SOAPExceptionImpl("No data handler/content associated with this attachment");
    }

    public boolean equals(Object o2) {
        return this == o2;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public MimeHeaders getMimeHeaders() {
        return this.headers;
    }

    public static void initializeJavaActivationHandlers() {
        try {
            CommandMap map = CommandMap.getDefaultCommandMap();
            if (map instanceof MailcapCommandMap) {
                MailcapCommandMap mailMap = (MailcapCommandMap) map;
                if (!cmdMapInitialized(mailMap)) {
                    mailMap.addMailcap("text/xml;;x-java-content-handler=com.sun.xml.internal.messaging.saaj.soap.XmlDataContentHandler");
                    mailMap.addMailcap("application/xml;;x-java-content-handler=com.sun.xml.internal.messaging.saaj.soap.XmlDataContentHandler");
                    mailMap.addMailcap("application/fastinfoset;;x-java-content-handler=com.sun.xml.internal.messaging.saaj.soap.FastInfosetDataContentHandler");
                    mailMap.addMailcap("image/*;;x-java-content-handler=com.sun.xml.internal.messaging.saaj.soap.ImageDataContentHandler");
                    mailMap.addMailcap("text/plain;;x-java-content-handler=com.sun.xml.internal.messaging.saaj.soap.StringDataContentHandler");
                }
            }
        } catch (Throwable th) {
        }
    }

    private static boolean cmdMapInitialized(MailcapCommandMap mailMap) {
        CommandInfo[] commands = mailMap.getAllCommands("application/fastinfoset");
        if (commands == null || commands.length == 0) {
            return false;
        }
        for (CommandInfo command : commands) {
            String commandClass = command.getCommandClass();
            if ("com.sun.xml.internal.ws.binding.FastInfosetDataContentHandler".equals(commandClass)) {
                return true;
            }
        }
        return false;
    }
}
