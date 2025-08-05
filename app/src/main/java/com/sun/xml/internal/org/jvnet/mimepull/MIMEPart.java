package com.sun.xml.internal.org.jvnet.mimepull;

import com.sun.media.jfxmedia.locator.Locator;
import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/MIMEPart.class */
public class MIMEPart {
    private static final Logger LOGGER;
    private volatile InternetHeaders headers;
    private volatile String contentId;
    private String contentType;
    private String contentTransferEncoding;
    volatile boolean parsed;
    final MIMEMessage msg;
    private final DataHead dataHead;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !MIMEPart.class.desiredAssertionStatus();
        LOGGER = Logger.getLogger(MIMEPart.class.getName());
    }

    MIMEPart(MIMEMessage msg) {
        this.msg = msg;
        this.dataHead = new DataHead(this);
    }

    MIMEPart(MIMEMessage msg, String contentId) {
        this(msg);
        this.contentId = contentId;
    }

    public InputStream read() {
        InputStream is = null;
        try {
            is = MimeUtility.decode(this.dataHead.read(), this.contentTransferEncoding);
        } catch (DecodingException ex) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, (String) null, (Throwable) ex);
            }
        }
        return is;
    }

    public void close() {
        this.dataHead.close();
    }

    public InputStream readOnce() {
        InputStream is = null;
        try {
            is = MimeUtility.decode(this.dataHead.readOnce(), this.contentTransferEncoding);
        } catch (DecodingException ex) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, (String) null, (Throwable) ex);
            }
        }
        return is;
    }

    public void moveTo(File f2) {
        this.dataHead.moveTo(f2);
    }

    public String getContentId() {
        if (this.contentId == null) {
            getHeaders();
        }
        return this.contentId;
    }

    public String getContentTransferEncoding() {
        if (this.contentTransferEncoding == null) {
            getHeaders();
        }
        return this.contentTransferEncoding;
    }

    public String getContentType() {
        if (this.contentType == null) {
            getHeaders();
        }
        return this.contentType;
    }

    private void getHeaders() {
        while (this.headers == null) {
            if (!this.msg.makeProgress() && this.headers == null) {
                throw new IllegalStateException("Internal Error. Didn't get Headers even after complete parsing.");
            }
        }
    }

    public List<String> getHeader(String name) {
        getHeaders();
        if ($assertionsDisabled || this.headers != null) {
            return this.headers.getHeader(name);
        }
        throw new AssertionError();
    }

    public List<? extends Header> getAllHeaders() {
        getHeaders();
        if ($assertionsDisabled || this.headers != null) {
            return this.headers.getAllHeaders();
        }
        throw new AssertionError();
    }

    void setHeaders(InternetHeaders headers) {
        this.headers = headers;
        List<String> ct = getHeader("Content-Type");
        this.contentType = ct == null ? Locator.DEFAULT_CONTENT_TYPE : ct.get(0);
        List<String> cte = getHeader("Content-Transfer-Encoding");
        this.contentTransferEncoding = cte == null ? "binary" : cte.get(0);
    }

    void addBody(ByteBuffer buf) {
        this.dataHead.addBody(buf);
    }

    void doneParsing() {
        this.parsed = true;
        this.dataHead.doneParsing();
    }

    void setContentId(String cid) {
        this.contentId = cid;
    }

    void setContentTransferEncoding(String cte) {
        this.contentTransferEncoding = cte;
    }

    public String toString() {
        return "Part=" + this.contentId + CallSiteDescriptor.TOKEN_DELIMITER + this.contentTransferEncoding;
    }
}
