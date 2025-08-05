package com.sun.xml.internal.messaging.saaj.packaging.mime.internet;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MultipartDataSource;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.OutputUtil;
import com.sun.xml.internal.messaging.saaj.util.FinalArrayList;
import com.sun.xml.internal.messaging.saaj.util.SAAJUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataSource;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/packaging/mime/internet/MimeMultipart.class */
public class MimeMultipart {
    protected DataSource ds;
    protected boolean parsed;
    protected FinalArrayList parts;
    protected ContentType contentType;
    protected MimeBodyPart parent;
    protected static final boolean ignoreMissingEndBoundary = SAAJUtil.getSystemBoolean("saaj.mime.multipart.ignoremissingendboundary");

    public MimeMultipart() {
        this("mixed");
    }

    public MimeMultipart(String subtype) {
        this.ds = null;
        this.parsed = true;
        this.parts = new FinalArrayList();
        String boundary = UniqueValue.getUniqueBoundaryValue();
        this.contentType = new ContentType("multipart", subtype, null);
        this.contentType.setParameter("boundary", boundary);
    }

    public MimeMultipart(DataSource ds, ContentType ct) throws MessagingException {
        this.ds = null;
        this.parsed = true;
        this.parts = new FinalArrayList();
        this.parsed = false;
        this.ds = ds;
        if (ct == null) {
            this.contentType = new ContentType(ds.getContentType());
        } else {
            this.contentType = ct;
        }
    }

    public void setSubType(String subtype) {
        this.contentType.setSubType(subtype);
    }

    public int getCount() throws MessagingException {
        parse();
        if (this.parts == null) {
            return 0;
        }
        return this.parts.size();
    }

    public MimeBodyPart getBodyPart(int index) throws MessagingException {
        parse();
        if (this.parts == null) {
            throw new IndexOutOfBoundsException("No such BodyPart");
        }
        return (MimeBodyPart) this.parts.get(index);
    }

    public MimeBodyPart getBodyPart(String CID) throws MessagingException {
        parse();
        int count = getCount();
        for (int i2 = 0; i2 < count; i2++) {
            MimeBodyPart part = getBodyPart(i2);
            String s2 = part.getContentID();
            String sNoAngle = s2 != null ? s2.replaceFirst("^<", "").replaceFirst(">$", "") : null;
            if (s2 != null && (s2.equals(CID) || CID.equals(sNoAngle))) {
                return part;
            }
        }
        return null;
    }

    protected void updateHeaders() throws MessagingException {
        for (int i2 = 0; i2 < this.parts.size(); i2++) {
            ((MimeBodyPart) this.parts.get(i2)).updateHeaders();
        }
    }

    public void writeTo(OutputStream os) throws MessagingException, IOException {
        parse();
        String boundary = "--" + this.contentType.getParameter("boundary");
        for (int i2 = 0; i2 < this.parts.size(); i2++) {
            OutputUtil.writeln(boundary, os);
            getBodyPart(i2).writeTo(os);
            OutputUtil.writeln(os);
        }
        OutputUtil.writeAsAscii(boundary, os);
        OutputUtil.writeAsAscii("--", os);
        os.flush();
    }

    /* JADX WARN: Code restructure failed: missing block: B:133:0x02ca, code lost:
    
        if (com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeMultipart.ignoreMissingEndBoundary != false) goto L140;
     */
    /* JADX WARN: Code restructure failed: missing block: B:135:0x02cf, code lost:
    
        if (r14 != false) goto L140;
     */
    /* JADX WARN: Code restructure failed: missing block: B:137:0x02d3, code lost:
    
        if (r9 != null) goto L140;
     */
    /* JADX WARN: Code restructure failed: missing block: B:139:0x02df, code lost:
    
        throw new com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException("Missing End Boundary for Mime Package : EOF while skipping headers");
     */
    /* JADX WARN: Code restructure failed: missing block: B:140:0x02e0, code lost:
    
        r7.parsed = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:141:0x02e5, code lost:
    
        return;
     */
    /* JADX WARN: Removed duplicated region for block: B:107:0x022a  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x0224 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void parse() throws com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException {
        /*
            Method dump skipped, instructions count: 742
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeMultipart.parse():void");
    }

    protected InternetHeaders createInternetHeaders(InputStream is) throws MessagingException {
        return new InternetHeaders(is);
    }

    protected MimeBodyPart createMimeBodyPart(InternetHeaders headers, byte[] content, int len) {
        return new MimeBodyPart(headers, content, len);
    }

    protected MimeBodyPart createMimeBodyPart(InputStream is) throws MessagingException {
        return new MimeBodyPart(is);
    }

    protected void setMultipartDataSource(MultipartDataSource mp) throws MessagingException {
        this.contentType = new ContentType(mp.getContentType());
        int count = mp.getCount();
        for (int i2 = 0; i2 < count; i2++) {
            addBodyPart(mp.getBodyPart(i2));
        }
    }

    public ContentType getContentType() {
        return this.contentType;
    }

    public boolean removeBodyPart(MimeBodyPart part) throws MessagingException {
        if (this.parts == null) {
            throw new MessagingException("No such body part");
        }
        boolean ret = this.parts.remove(part);
        part.setParent(null);
        return ret;
    }

    public void removeBodyPart(int index) {
        if (this.parts == null) {
            throw new IndexOutOfBoundsException("No such BodyPart");
        }
        MimeBodyPart part = (MimeBodyPart) this.parts.get(index);
        this.parts.remove(index);
        part.setParent(null);
    }

    public synchronized void addBodyPart(MimeBodyPart part) {
        if (this.parts == null) {
            this.parts = new FinalArrayList();
        }
        this.parts.add(part);
        part.setParent(this);
    }

    public synchronized void addBodyPart(MimeBodyPart part, int index) {
        if (this.parts == null) {
            this.parts = new FinalArrayList();
        }
        this.parts.add(index, part);
        part.setParent(this);
    }

    MimeBodyPart getParent() {
        return this.parent;
    }

    void setParent(MimeBodyPart parent) {
        this.parent = parent;
    }
}
