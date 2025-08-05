package com.sun.xml.internal.messaging.saaj.packaging.mime.internet;

import com.sun.glass.ui.Clipboard;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.HeaderTokenizer;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.OutputUtil;
import com.sun.xml.internal.messaging.saaj.soap.MessageImpl;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import com.sun.xml.internal.messaging.saaj.util.FinalArrayList;
import com.sun.xml.internal.org.jvnet.mimepull.Header;
import com.sun.xml.internal.org.jvnet.mimepull.MIMEPart;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.activation.DataHandler;
import javax.activation.DataSource;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/packaging/mime/internet/MimeBodyPart.class */
public final class MimeBodyPart {
    public static final String ATTACHMENT = "attachment";
    public static final String INLINE = "inline";
    private static boolean setDefaultTextCharset;
    private DataHandler dh;
    private byte[] content;
    private int contentLength;
    private int start;
    private InputStream contentStream;
    private final InternetHeaders headers;
    private MimeMultipart parent;
    private MIMEPart mimePart;

    static {
        setDefaultTextCharset = true;
        try {
            String s2 = System.getProperty("mail.mime.setdefaulttextcharset");
            setDefaultTextCharset = s2 == null || !s2.equalsIgnoreCase("false");
        } catch (SecurityException e2) {
        }
    }

    public MimeBodyPart() {
        this.start = 0;
        this.headers = new InternetHeaders();
    }

    public MimeBodyPart(InputStream is) throws MessagingException {
        this.start = 0;
        if (!(is instanceof ByteArrayInputStream) && !(is instanceof BufferedInputStream) && !(is instanceof SharedInputStream)) {
            is = new BufferedInputStream(is);
        }
        this.headers = new InternetHeaders(is);
        if (is instanceof SharedInputStream) {
            SharedInputStream sis = (SharedInputStream) is;
            this.contentStream = sis.newStream(sis.getPosition(), -1L);
            return;
        }
        try {
            ByteOutputStream bos = new ByteOutputStream();
            bos.write(is);
            this.content = bos.getBytes();
            this.contentLength = bos.getCount();
        } catch (IOException ioex) {
            throw new MessagingException("Error reading input stream", ioex);
        }
    }

    public MimeBodyPart(InternetHeaders headers, byte[] content, int len) {
        this.start = 0;
        this.headers = headers;
        this.content = content;
        this.contentLength = len;
    }

    public MimeBodyPart(InternetHeaders headers, byte[] content, int start, int len) {
        this.start = 0;
        this.headers = headers;
        this.content = content;
        this.start = start;
        this.contentLength = len;
    }

    public MimeBodyPart(MIMEPart part) {
        this.start = 0;
        this.mimePart = part;
        this.headers = new InternetHeaders();
        List<? extends Header> hdrs = this.mimePart.getAllHeaders();
        for (Header hd : hdrs) {
            this.headers.addHeader(hd.getName(), hd.getValue());
        }
    }

    public MimeMultipart getParent() {
        return this.parent;
    }

    public void setParent(MimeMultipart parent) {
        this.parent = parent;
    }

    public int getSize() {
        if (this.mimePart != null) {
            try {
                return this.mimePart.read().available();
            } catch (IOException e2) {
                return -1;
            }
        }
        if (this.content != null) {
            return this.contentLength;
        }
        if (this.contentStream != null) {
            try {
                int size = this.contentStream.available();
                if (size > 0) {
                    return size;
                }
                return -1;
            } catch (IOException e3) {
                return -1;
            }
        }
        return -1;
    }

    public int getLineCount() {
        return -1;
    }

    public String getContentType() {
        if (this.mimePart != null) {
            return this.mimePart.getContentType();
        }
        String s2 = getHeader("Content-Type", null);
        if (s2 == null) {
            s2 = Clipboard.TEXT_TYPE;
        }
        return s2;
    }

    public boolean isMimeType(String mimeType) {
        boolean result;
        try {
            ContentType ct = new ContentType(getContentType());
            result = ct.match(mimeType);
        } catch (ParseException e2) {
            result = getContentType().equalsIgnoreCase(mimeType);
        }
        return result;
    }

    public String getDisposition() throws MessagingException {
        String s2 = getHeader("Content-Disposition", null);
        if (s2 == null) {
            return null;
        }
        ContentDisposition cd = new ContentDisposition(s2);
        return cd.getDisposition();
    }

    public void setDisposition(String disposition) throws MessagingException {
        if (disposition == null) {
            removeHeader("Content-Disposition");
            return;
        }
        String s2 = getHeader("Content-Disposition", null);
        if (s2 != null) {
            ContentDisposition cd = new ContentDisposition(s2);
            cd.setDisposition(disposition);
            disposition = cd.toString();
        }
        setHeader("Content-Disposition", disposition);
    }

    public String getEncoding() throws MessagingException {
        HeaderTokenizer.Token tk;
        int tkType;
        String s2 = getHeader("Content-Transfer-Encoding", null);
        if (s2 == null) {
            return null;
        }
        String s3 = s2.trim();
        if (s3.equalsIgnoreCase("7bit") || s3.equalsIgnoreCase("8bit") || s3.equalsIgnoreCase("quoted-printable") || s3.equalsIgnoreCase("base64")) {
            return s3;
        }
        HeaderTokenizer h2 = new HeaderTokenizer(s3, HeaderTokenizer.MIME);
        do {
            tk = h2.next();
            tkType = tk.getType();
            if (tkType == -4) {
                return s3;
            }
        } while (tkType != -1);
        return tk.getValue();
    }

    public String getContentID() {
        return getHeader(MessageImpl.CONTENT_ID, null);
    }

    public void setContentID(String cid) {
        if (cid == null) {
            removeHeader(MessageImpl.CONTENT_ID);
        } else {
            setHeader(MessageImpl.CONTENT_ID, cid);
        }
    }

    public String getContentMD5() {
        return getHeader("Content-MD5", null);
    }

    public void setContentMD5(String md5) {
        setHeader("Content-MD5", md5);
    }

    public String[] getContentLanguage() throws MessagingException {
        String s2 = getHeader("Content-Language", null);
        if (s2 == null) {
            return null;
        }
        HeaderTokenizer h2 = new HeaderTokenizer(s2, HeaderTokenizer.MIME);
        FinalArrayList v2 = new FinalArrayList();
        while (true) {
            HeaderTokenizer.Token tk = h2.next();
            int tkType = tk.getType();
            if (tkType == -4) {
                break;
            }
            if (tkType == -1) {
                v2.add(tk.getValue());
            }
        }
        if (v2.size() == 0) {
            return null;
        }
        return (String[]) v2.toArray(new String[v2.size()]);
    }

    public void setContentLanguage(String[] languages) {
        StringBuffer sb = new StringBuffer(languages[0]);
        for (int i2 = 1; i2 < languages.length; i2++) {
            sb.append(',').append(languages[i2]);
        }
        setHeader("Content-Language", sb.toString());
    }

    public String getDescription() {
        String rawvalue = getHeader("Content-Description", null);
        if (rawvalue == null) {
            return null;
        }
        try {
            return MimeUtility.decodeText(MimeUtility.unfold(rawvalue));
        } catch (UnsupportedEncodingException e2) {
            return rawvalue;
        }
    }

    public void setDescription(String description) throws MessagingException {
        setDescription(description, null);
    }

    public void setDescription(String description, String charset) throws MessagingException {
        if (description == null) {
            removeHeader("Content-Description");
            return;
        }
        try {
            setHeader("Content-Description", MimeUtility.fold(21, MimeUtility.encodeText(description, charset, null)));
        } catch (UnsupportedEncodingException uex) {
            throw new MessagingException("Encoding error", uex);
        }
    }

    public String getFileName() throws MessagingException {
        String s2;
        String filename = null;
        String s3 = getHeader("Content-Disposition", null);
        if (s3 != null) {
            ContentDisposition cd = new ContentDisposition(s3);
            filename = cd.getParameter("filename");
        }
        if (filename == null && (s2 = getHeader("Content-Type", null)) != null) {
            try {
                ContentType ct = new ContentType(s2);
                filename = ct.getParameter("name");
            } catch (ParseException e2) {
            }
        }
        return filename;
    }

    public void setFileName(String filename) throws MessagingException {
        String s2 = getHeader("Content-Disposition", null);
        ContentDisposition cd = new ContentDisposition(s2 == null ? ATTACHMENT : s2);
        cd.setParameter("filename", filename);
        setHeader("Content-Disposition", cd.toString());
        String s3 = getHeader("Content-Type", null);
        if (s3 != null) {
            try {
                ContentType cType = new ContentType(s3);
                cType.setParameter("name", filename);
                setHeader("Content-Type", cType.toString());
            } catch (ParseException e2) {
            }
        }
    }

    public InputStream getInputStream() throws IOException {
        return getDataHandler().getInputStream();
    }

    InputStream getContentStream() throws MessagingException {
        if (this.mimePart != null) {
            return this.mimePart.read();
        }
        if (this.contentStream != null) {
            return ((SharedInputStream) this.contentStream).newStream(0L, -1L);
        }
        if (this.content != null) {
            return new ByteArrayInputStream(this.content, this.start, this.contentLength);
        }
        throw new MessagingException("No content");
    }

    public InputStream getRawInputStream() throws MessagingException {
        return getContentStream();
    }

    public DataHandler getDataHandler() {
        if (this.mimePart != null) {
            return new DataHandler(new DataSource() { // from class: com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeBodyPart.1
                @Override // javax.activation.DataSource
                public InputStream getInputStream() throws IOException {
                    return MimeBodyPart.this.mimePart.read();
                }

                @Override // javax.activation.DataSource
                public OutputStream getOutputStream() throws IOException {
                    throw new UnsupportedOperationException("getOutputStream cannot be supported : You have enabled LazyAttachments Option");
                }

                @Override // javax.activation.DataSource
                public String getContentType() {
                    return MimeBodyPart.this.mimePart.getContentType();
                }

                @Override // javax.activation.DataSource
                public String getName() {
                    return "MIMEPart Wrapped DataSource";
                }
            });
        }
        if (this.dh == null) {
            this.dh = new DataHandler(new MimePartDataSource(this));
        }
        return this.dh;
    }

    public Object getContent() throws IOException {
        return getDataHandler().getContent();
    }

    public void setDataHandler(DataHandler dh) {
        if (this.mimePart != null) {
            this.mimePart = null;
        }
        this.dh = dh;
        this.content = null;
        this.contentStream = null;
        removeHeader("Content-Type");
        removeHeader("Content-Transfer-Encoding");
    }

    public void setContent(Object o2, String type) {
        if (this.mimePart != null) {
            this.mimePart = null;
        }
        if (o2 instanceof MimeMultipart) {
            setContent((MimeMultipart) o2);
        } else {
            setDataHandler(new DataHandler(o2, type));
        }
    }

    public void setText(String text) {
        setText(text, null);
    }

    public void setText(String text, String charset) {
        if (charset == null) {
            if (MimeUtility.checkAscii(text) != 1) {
                charset = MimeUtility.getDefaultMIMECharset();
            } else {
                charset = "us-ascii";
            }
        }
        setContent(text, "text/plain; charset=" + MimeUtility.quote(charset, HeaderTokenizer.MIME));
    }

    public void setContent(MimeMultipart mp) {
        if (this.mimePart != null) {
            this.mimePart = null;
        }
        setDataHandler(new DataHandler(mp, mp.getContentType().toString()));
        mp.setParent(this);
    }

    public void writeTo(OutputStream os) throws MessagingException, IOException {
        List hdrLines = this.headers.getAllHeaderLines();
        int sz = hdrLines.size();
        for (int i2 = 0; i2 < sz; i2++) {
            OutputUtil.writeln((String) hdrLines.get(i2), os);
        }
        OutputUtil.writeln(os);
        if (this.contentStream != null) {
            ((SharedInputStream) this.contentStream).writeTo(0L, -1L, os);
            return;
        }
        if (this.content != null) {
            os.write(this.content, this.start, this.contentLength);
            return;
        }
        if (this.dh != null) {
            OutputStream wos = MimeUtility.encode(os, getEncoding());
            getDataHandler().writeTo(wos);
            if (os != wos) {
                wos.flush();
                return;
            }
            return;
        }
        if (this.mimePart != null) {
            OutputStream wos2 = MimeUtility.encode(os, getEncoding());
            getDataHandler().writeTo(wos2);
            if (os != wos2) {
                wos2.flush();
                return;
            }
            return;
        }
        throw new MessagingException("no content");
    }

    public String[] getHeader(String name) {
        return this.headers.getHeader(name);
    }

    public String getHeader(String name, String delimiter) {
        return this.headers.getHeader(name, delimiter);
    }

    public void setHeader(String name, String value) {
        this.headers.setHeader(name, value);
    }

    public void addHeader(String name, String value) {
        this.headers.addHeader(name, value);
    }

    public void removeHeader(String name) {
        this.headers.removeHeader(name);
    }

    public FinalArrayList getAllHeaders() {
        return this.headers.getAllHeaders();
    }

    public void addHeaderLine(String line) {
        this.headers.addHeaderLine(line);
    }

    protected void updateHeaders() throws MessagingException {
        String charset;
        DataHandler dh = getDataHandler();
        if (dh == null) {
            return;
        }
        try {
            String type = dh.getContentType();
            boolean composite = false;
            boolean needCTHeader = getHeader("Content-Type") == null;
            ContentType cType = new ContentType(type);
            if (cType.match("multipart/*")) {
                composite = true;
                Object o2 = dh.getContent();
                ((MimeMultipart) o2).updateHeaders();
            } else if (cType.match("message/rfc822")) {
                composite = true;
            }
            if (!composite) {
                if (getHeader("Content-Transfer-Encoding") == null) {
                    setEncoding(MimeUtility.getEncoding(dh));
                }
                if (needCTHeader && setDefaultTextCharset && cType.match("text/*") && cType.getParameter("charset") == null) {
                    String enc = getEncoding();
                    if (enc != null && enc.equalsIgnoreCase("7bit")) {
                        charset = "us-ascii";
                    } else {
                        charset = MimeUtility.getDefaultMIMECharset();
                    }
                    cType.setParameter("charset", charset);
                    type = cType.toString();
                }
            }
            if (needCTHeader) {
                String s2 = getHeader("Content-Disposition", null);
                if (s2 != null) {
                    ContentDisposition cd = new ContentDisposition(s2);
                    String filename = cd.getParameter("filename");
                    if (filename != null) {
                        cType.setParameter("name", filename);
                        type = cType.toString();
                    }
                }
                setHeader("Content-Type", type);
            }
        } catch (IOException ex) {
            throw new MessagingException("IOException updating headers", ex);
        }
    }

    private void setEncoding(String encoding) {
        setHeader("Content-Transfer-Encoding", encoding);
    }
}
