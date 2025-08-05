package com.sun.xml.internal.messaging.saaj.soap;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler;
import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
import com.sun.xml.internal.messaging.saaj.packaging.mime.Header;
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.BMMimeMultipart;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ContentType;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeBodyPart;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeMultipart;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimePullMultipart;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ParameterList;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ParseException;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.SharedInputStream;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.ASCIIUtility;
import com.sun.xml.internal.messaging.saaj.soap.impl.EnvelopeImpl;
import com.sun.xml.internal.messaging.saaj.util.FastInfosetReflection;
import com.sun.xml.internal.messaging.saaj.util.FinalArrayList;
import com.sun.xml.internal.messaging.saaj.util.LogDomainConstants;
import com.sun.xml.internal.messaging.saaj.util.SAAJUtil;
import com.sun.xml.internal.org.jvnet.mimepull.MIMEPart;
import com.sun.xml.internal.ws.encoding.MtomCodec;
import com.sun.xml.internal.ws.encoding.fastinfoset.FastInfosetMIMETypes;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.stream.StreamSource;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/MessageImpl.class */
public abstract class MessageImpl extends SOAPMessage implements SOAPConstants {
    public static final String CONTENT_ID = "Content-ID";
    public static final String CONTENT_LOCATION = "Content-Location";
    protected static final Logger log = Logger.getLogger(LogDomainConstants.SOAP_DOMAIN, "com.sun.xml.internal.messaging.saaj.soap.LocalStrings");
    protected static final int PLAIN_XML_FLAG = 1;
    protected static final int MIME_MULTIPART_FLAG = 2;
    protected static final int SOAP1_1_FLAG = 4;
    protected static final int SOAP1_2_FLAG = 8;
    protected static final int MIME_MULTIPART_XOP_SOAP1_1_FLAG = 6;
    protected static final int MIME_MULTIPART_XOP_SOAP1_2_FLAG = 10;
    protected static final int XOP_FLAG = 13;
    protected static final int FI_ENCODED_FLAG = 16;
    protected MimeHeaders headers;
    protected ContentType contentType;
    protected SOAPPartImpl soapPartImpl;
    protected FinalArrayList attachments;
    protected boolean saved;
    protected byte[] messageBytes;
    protected int messageByteCount;
    protected HashMap properties;
    protected MimeMultipart multiPart;
    protected boolean attachmentsInitialized;
    protected boolean isFastInfoset;
    protected boolean acceptFastInfoset;
    protected MimeMultipart mmp;
    private boolean optimizeAttachmentProcessing;
    private InputStream inputStreamAfterSaveChanges;
    private static boolean switchOffBM;
    private static boolean switchOffLazyAttachment;
    private static boolean useMimePull;
    private boolean lazyAttachments;
    private static final Iterator nullIter;

    protected abstract boolean isCorrectSoapVersion(int i2);

    protected abstract String getExpectedContentType();

    protected abstract String getExpectedAcceptHeader();

    @Override // javax.xml.soap.SOAPMessage
    public abstract SOAPPart getSOAPPart();

    static {
        switchOffBM = false;
        switchOffLazyAttachment = false;
        useMimePull = false;
        String s2 = SAAJUtil.getSystemProperty("saaj.mime.optimization");
        if (s2 != null && s2.equals("false")) {
            switchOffBM = true;
        }
        String s3 = SAAJUtil.getSystemProperty("saaj.lazy.mime.optimization");
        if (s3 != null && s3.equals("false")) {
            switchOffLazyAttachment = true;
        }
        useMimePull = SAAJUtil.getSystemBoolean("saaj.use.mimepull");
        nullIter = Collections.EMPTY_LIST.iterator();
    }

    private static boolean isSoap1_1Type(String primary, String sub) {
        return (primary.equalsIgnoreCase("text") && sub.equalsIgnoreCase("xml")) || (primary.equalsIgnoreCase("text") && sub.equalsIgnoreCase("xml-soap")) || (primary.equals("application") && sub.equals("fastinfoset"));
    }

    private static boolean isEqualToSoap1_1Type(String type) {
        return type.startsWith("text/xml") || type.startsWith("application/fastinfoset");
    }

    private static boolean isSoap1_2Type(String primary, String sub) {
        return primary.equals("application") && (sub.equals("soap+xml") || sub.equals("soap+fastinfoset"));
    }

    private static boolean isEqualToSoap1_2Type(String type) {
        return type.startsWith("application/soap+xml") || type.startsWith(FastInfosetMIMETypes.SOAP_12);
    }

    protected MessageImpl() {
        this(false, false);
        this.attachmentsInitialized = true;
    }

    protected MessageImpl(boolean isFastInfoset, boolean acceptFastInfoset) {
        this.saved = false;
        this.properties = new HashMap();
        this.multiPart = null;
        this.attachmentsInitialized = false;
        this.isFastInfoset = false;
        this.acceptFastInfoset = false;
        this.mmp = null;
        this.optimizeAttachmentProcessing = true;
        this.inputStreamAfterSaveChanges = null;
        this.lazyAttachments = false;
        this.isFastInfoset = isFastInfoset;
        this.acceptFastInfoset = acceptFastInfoset;
        this.headers = new MimeHeaders();
        this.headers.setHeader(XIncludeHandler.HTTP_ACCEPT, getExpectedAcceptHeader());
        this.contentType = new ContentType();
    }

    protected MessageImpl(SOAPMessage msg) {
        this.saved = false;
        this.properties = new HashMap();
        this.multiPart = null;
        this.attachmentsInitialized = false;
        this.isFastInfoset = false;
        this.acceptFastInfoset = false;
        this.mmp = null;
        this.optimizeAttachmentProcessing = true;
        this.inputStreamAfterSaveChanges = null;
        this.lazyAttachments = false;
        if (!(msg instanceof MessageImpl)) {
        }
        MessageImpl src = (MessageImpl) msg;
        this.headers = src.headers;
        this.soapPartImpl = src.soapPartImpl;
        this.attachments = src.attachments;
        this.saved = src.saved;
        this.messageBytes = src.messageBytes;
        this.messageByteCount = src.messageByteCount;
        this.properties = src.properties;
        this.contentType = src.contentType;
    }

    protected static boolean isSoap1_1Content(int stat) {
        return (stat & 4) != 0;
    }

    protected static boolean isSoap1_2Content(int stat) {
        return (stat & 8) != 0;
    }

    private static boolean isMimeMultipartXOPSoap1_2Package(ContentType contentType) {
        String startinfo;
        String type = contentType.getParameter("type");
        if (type == null || !type.toLowerCase().startsWith(MtomCodec.XOP_XML_MIME_TYPE) || (startinfo = contentType.getParameter("start-info")) == null) {
            return false;
        }
        return isEqualToSoap1_2Type(startinfo.toLowerCase());
    }

    private static boolean isMimeMultipartXOPSoap1_1Package(ContentType contentType) {
        String startinfo;
        String type = contentType.getParameter("type");
        if (type == null || !type.toLowerCase().startsWith(MtomCodec.XOP_XML_MIME_TYPE) || (startinfo = contentType.getParameter("start-info")) == null) {
            return false;
        }
        return isEqualToSoap1_1Type(startinfo.toLowerCase());
    }

    private static boolean isSOAPBodyXOPPackage(ContentType contentType) {
        String primary = contentType.getPrimaryType();
        String sub = contentType.getSubType();
        if (primary.equalsIgnoreCase("application") && sub.equalsIgnoreCase("xop+xml")) {
            String type = getTypeParameter(contentType);
            return isEqualToSoap1_2Type(type) || isEqualToSoap1_1Type(type);
        }
        return false;
    }

    protected MessageImpl(MimeHeaders headers, InputStream in) throws SOAPExceptionImpl {
        this.saved = false;
        this.properties = new HashMap();
        this.multiPart = null;
        this.attachmentsInitialized = false;
        this.isFastInfoset = false;
        this.acceptFastInfoset = false;
        this.mmp = null;
        this.optimizeAttachmentProcessing = true;
        this.inputStreamAfterSaveChanges = null;
        this.lazyAttachments = false;
        this.contentType = parseContentType(headers);
        init(headers, identifyContentType(this.contentType), this.contentType, in);
    }

    private static ContentType parseContentType(MimeHeaders headers) throws SOAPExceptionImpl {
        if (headers != null) {
            String ct = getContentType(headers);
            if (ct == null) {
                log.severe("SAAJ0532.soap.no.Content-Type");
                throw new SOAPExceptionImpl("Absent Content-Type");
            }
            try {
                return new ContentType(ct);
            } catch (Throwable ex) {
                log.severe("SAAJ0535.soap.cannot.internalize.message");
                throw new SOAPExceptionImpl("Unable to internalize message", ex);
            }
        }
        log.severe("SAAJ0550.soap.null.headers");
        throw new SOAPExceptionImpl("Cannot create message: Headers can't be null");
    }

    protected MessageImpl(MimeHeaders headers, ContentType contentType, int stat, InputStream in) throws SOAPExceptionImpl {
        this.saved = false;
        this.properties = new HashMap();
        this.multiPart = null;
        this.attachmentsInitialized = false;
        this.isFastInfoset = false;
        this.acceptFastInfoset = false;
        this.mmp = null;
        this.optimizeAttachmentProcessing = true;
        this.inputStreamAfterSaveChanges = null;
        this.lazyAttachments = false;
        init(headers, stat, contentType, in);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void init(MimeHeaders headers, int stat, final ContentType contentType, final InputStream in) throws SOAPExceptionImpl {
        MimeBodyPart soapMessagePart;
        String[] values;
        this.headers = headers;
        try {
            if ((stat & 16) > 0) {
                this.acceptFastInfoset = true;
                this.isFastInfoset = true;
            }
            if (!this.isFastInfoset && (values = headers.getHeader(XIncludeHandler.HTTP_ACCEPT)) != null) {
                for (String str : values) {
                    StringTokenizer st = new StringTokenizer(str, ",");
                    while (st.hasMoreTokens()) {
                        String token = st.nextToken().trim();
                        if (token.equalsIgnoreCase("application/fastinfoset") || token.equalsIgnoreCase(FastInfosetMIMETypes.SOAP_12)) {
                            this.acceptFastInfoset = true;
                            break;
                        }
                    }
                }
            }
            if (!isCorrectSoapVersion(stat)) {
                log.log(Level.SEVERE, "SAAJ0533.soap.incorrect.Content-Type", (Object[]) new String[]{contentType.toString(), getExpectedContentType()});
                throw new SOAPVersionMismatchException("Cannot create message: incorrect content-type for SOAP version. Got: " + ((Object) contentType) + " Expected: " + getExpectedContentType());
            }
            if ((stat & 1) != 0) {
                if (this.isFastInfoset) {
                    getSOAPPart().setContent(FastInfosetReflection.FastInfosetSource_new(in));
                } else {
                    initCharsetProperty(contentType);
                    getSOAPPart().setContent(new StreamSource(in));
                }
            } else if ((stat & 2) != 0) {
                DataSource ds = new DataSource() { // from class: com.sun.xml.internal.messaging.saaj.soap.MessageImpl.1
                    @Override // javax.activation.DataSource
                    public InputStream getInputStream() {
                        return in;
                    }

                    @Override // javax.activation.DataSource
                    public OutputStream getOutputStream() {
                        return null;
                    }

                    @Override // javax.activation.DataSource
                    public String getContentType() {
                        return contentType.toString();
                    }

                    @Override // javax.activation.DataSource
                    public String getName() {
                        return "";
                    }
                };
                this.multiPart = null;
                if (useMimePull) {
                    this.multiPart = new MimePullMultipart(ds, contentType);
                } else if (switchOffBM) {
                    this.multiPart = new MimeMultipart(ds, contentType);
                } else {
                    this.multiPart = new BMMimeMultipart(ds, contentType);
                }
                String startParam = contentType.getParameter("start");
                InputStream soapPartInputStream = null;
                String contentID = null;
                String contentIDNoAngle = null;
                if (switchOffBM || switchOffLazyAttachment) {
                    if (startParam == null) {
                        soapMessagePart = this.multiPart.getBodyPart(0);
                        for (int i2 = 1; i2 < this.multiPart.getCount(); i2++) {
                            initializeAttachment(this.multiPart, i2);
                        }
                    } else {
                        soapMessagePart = this.multiPart.getBodyPart(startParam);
                        for (int i3 = 0; i3 < this.multiPart.getCount(); i3++) {
                            String contentID2 = this.multiPart.getBodyPart(i3).getContentID();
                            String contentIDNoAngle2 = contentID2 != null ? contentID2.replaceFirst("^<", "").replaceFirst(">$", "") : null;
                            if (!startParam.equals(contentID2) && !startParam.equals(contentIDNoAngle2)) {
                                initializeAttachment(this.multiPart, i3);
                            }
                        }
                    }
                } else if (useMimePull) {
                    MimePullMultipart mpMultipart = (MimePullMultipart) this.multiPart;
                    MIMEPart sp = mpMultipart.readAndReturnSOAPPart();
                    soapMessagePart = new MimeBodyPart(sp);
                    soapPartInputStream = sp.readOnce();
                } else {
                    BMMimeMultipart bMMimeMultipart = (BMMimeMultipart) this.multiPart;
                    InputStream inputStreamInitStream = bMMimeMultipart.initStream();
                    SharedInputStream sin = null;
                    if (inputStreamInitStream instanceof SharedInputStream) {
                        sin = (SharedInputStream) inputStreamInitStream;
                    }
                    String boundary = "--" + contentType.getParameter("boundary");
                    byte[] bndbytes = ASCIIUtility.getBytes(boundary);
                    if (startParam == null) {
                        soapMessagePart = bMMimeMultipart.getNextPart(inputStreamInitStream, bndbytes, sin);
                        bMMimeMultipart.removeBodyPart(soapMessagePart);
                    } else {
                        MimeBodyPart bp2 = null;
                        while (!startParam.equals(contentID) && !startParam.equals(contentIDNoAngle)) {
                            try {
                                bp2 = bMMimeMultipart.getNextPart(inputStreamInitStream, bndbytes, sin);
                                contentID = bp2.getContentID();
                                contentIDNoAngle = contentID != null ? contentID.replaceFirst("^<", "").replaceFirst(">$", "") : null;
                            } catch (Exception e2) {
                                throw new SOAPExceptionImpl(e2);
                            }
                        }
                        soapMessagePart = bp2;
                        bMMimeMultipart.removeBodyPart(bp2);
                    }
                }
                if (soapPartInputStream == null && soapMessagePart != null) {
                    soapPartInputStream = soapMessagePart.getInputStream();
                }
                ContentType soapPartCType = new ContentType(soapMessagePart.getContentType());
                initCharsetProperty(soapPartCType);
                String baseType = soapPartCType.getBaseType().toLowerCase();
                if (!isEqualToSoap1_1Type(baseType) && !isEqualToSoap1_2Type(baseType) && !isSOAPBodyXOPPackage(soapPartCType)) {
                    log.log(Level.SEVERE, "SAAJ0549.soap.part.invalid.Content-Type", new Object[]{baseType});
                    throw new SOAPExceptionImpl("Bad Content-Type for SOAP Part : " + baseType);
                }
                SOAPPart soapPart = getSOAPPart();
                setMimeHeaders(soapPart, soapMessagePart);
                soapPart.setContent(this.isFastInfoset ? FastInfosetReflection.FastInfosetSource_new(soapPartInputStream) : new StreamSource(soapPartInputStream));
            } else {
                log.severe("SAAJ0534.soap.unknown.Content-Type");
                throw new SOAPExceptionImpl("Unrecognized Content-Type");
            }
            needsSave();
        } catch (Throwable ex) {
            log.severe("SAAJ0535.soap.cannot.internalize.message");
            throw new SOAPExceptionImpl("Unable to internalize message", ex);
        }
    }

    public boolean isFastInfoset() {
        return this.isFastInfoset;
    }

    public boolean acceptFastInfoset() {
        return this.acceptFastInfoset;
    }

    public void setIsFastInfoset(boolean value) {
        if (value != this.isFastInfoset) {
            this.isFastInfoset = value;
            if (this.isFastInfoset) {
                this.acceptFastInfoset = true;
            }
            this.saved = false;
        }
    }

    @Override // javax.xml.soap.SOAPMessage
    public Object getProperty(String property) {
        return (String) this.properties.get(property);
    }

    @Override // javax.xml.soap.SOAPMessage
    public void setProperty(String property, Object value) {
        verify(property, value);
        this.properties.put(property, value);
    }

    private void verify(String property, Object value) {
        if (property.equalsIgnoreCase(SOAPMessage.WRITE_XML_DECLARATION)) {
            if (!"true".equals(value) && !"false".equals(value)) {
                throw new RuntimeException(property + " must have value false or true");
            }
            try {
                EnvelopeImpl env = (EnvelopeImpl) getSOAPPart().getEnvelope();
                if ("true".equalsIgnoreCase((String) value)) {
                    env.setOmitXmlDecl("no");
                } else if ("false".equalsIgnoreCase((String) value)) {
                    env.setOmitXmlDecl("yes");
                }
                return;
            } catch (Exception e2) {
                log.log(Level.SEVERE, "SAAJ0591.soap.exception.in.set.property", new Object[]{e2.getMessage(), SOAPMessage.WRITE_XML_DECLARATION});
                throw new RuntimeException(e2);
            }
        }
        if (property.equalsIgnoreCase(SOAPMessage.CHARACTER_SET_ENCODING)) {
            try {
                ((EnvelopeImpl) getSOAPPart().getEnvelope()).setCharsetEncoding((String) value);
            } catch (Exception e3) {
                log.log(Level.SEVERE, "SAAJ0591.soap.exception.in.set.property", new Object[]{e3.getMessage(), SOAPMessage.CHARACTER_SET_ENCODING});
                throw new RuntimeException(e3);
            }
        }
    }

    static int identifyContentType(ContentType ct) throws SOAPExceptionImpl {
        String primary = ct.getPrimaryType().toLowerCase();
        String sub = ct.getSubType().toLowerCase();
        if (primary.equals("multipart")) {
            if (sub.equals("related")) {
                String type = getTypeParameter(ct);
                if (isEqualToSoap1_1Type(type)) {
                    return (type.equals("application/fastinfoset") ? 16 : 0) | 2 | 4;
                }
                if (isEqualToSoap1_2Type(type)) {
                    return (type.equals(FastInfosetMIMETypes.SOAP_12) ? 16 : 0) | 2 | 8;
                }
                if (isMimeMultipartXOPSoap1_1Package(ct)) {
                    return 6;
                }
                if (isMimeMultipartXOPSoap1_2Package(ct)) {
                    return 10;
                }
                log.severe("SAAJ0536.soap.content-type.mustbe.multipart");
                throw new SOAPExceptionImpl("Content-Type needs to be Multipart/Related and with \"type=text/xml\" or \"type=application/soap+xml\"");
            }
            log.severe("SAAJ0537.soap.invalid.content-type");
            throw new SOAPExceptionImpl("Invalid Content-Type: " + primary + '/' + sub);
        }
        if (isSoap1_1Type(primary, sub)) {
            return ((primary.equalsIgnoreCase("application") && sub.equalsIgnoreCase("fastinfoset")) ? 16 : 0) | 1 | 4;
        }
        if (isSoap1_2Type(primary, sub)) {
            return ((primary.equalsIgnoreCase("application") && sub.equalsIgnoreCase("soap+fastinfoset")) ? 16 : 0) | 1 | 8;
        }
        if (isSOAPBodyXOPPackage(ct)) {
            return 13;
        }
        log.severe("SAAJ0537.soap.invalid.content-type");
        throw new SOAPExceptionImpl("Invalid Content-Type:" + primary + '/' + sub + ". Is this an error message instead of a SOAP response?");
    }

    private static String getTypeParameter(ContentType contentType) {
        String p2 = contentType.getParameter("type");
        if (p2 != null) {
            return p2.toLowerCase();
        }
        return "text/xml";
    }

    @Override // javax.xml.soap.SOAPMessage
    public MimeHeaders getMimeHeaders() {
        return this.headers;
    }

    static final String getContentType(MimeHeaders headers) {
        String[] values = headers.getHeader("Content-Type");
        if (values == null) {
            return null;
        }
        return values[0];
    }

    public String getContentType() {
        return getContentType(this.headers);
    }

    public void setContentType(String type) {
        this.headers.setHeader("Content-Type", type);
        needsSave();
    }

    private ContentType contentType() {
        String currentContent;
        ContentType ct = null;
        try {
            currentContent = getContentType();
        } catch (Exception e2) {
        }
        if (currentContent == null) {
            return this.contentType;
        }
        ct = new ContentType(currentContent);
        return ct;
    }

    public String getBaseType() {
        return contentType().getBaseType();
    }

    public void setBaseType(String type) {
        ContentType ct = contentType();
        ct.setParameter("type", type);
        this.headers.setHeader("Content-Type", ct.toString());
        needsSave();
    }

    public String getAction() {
        return contentType().getParameter("action");
    }

    public void setAction(String action) {
        ContentType ct = contentType();
        ct.setParameter("action", action);
        this.headers.setHeader("Content-Type", ct.toString());
        needsSave();
    }

    public String getCharset() {
        return contentType().getParameter("charset");
    }

    public void setCharset(String charset) {
        ContentType ct = contentType();
        ct.setParameter("charset", charset);
        this.headers.setHeader("Content-Type", ct.toString());
        needsSave();
    }

    private final void needsSave() {
        this.saved = false;
    }

    @Override // javax.xml.soap.SOAPMessage
    public boolean saveRequired() {
        return !this.saved;
    }

    @Override // javax.xml.soap.SOAPMessage
    public String getContentDescription() {
        String[] values = this.headers.getHeader("Content-Description");
        if (values != null && values.length > 0) {
            return values[0];
        }
        return null;
    }

    @Override // javax.xml.soap.SOAPMessage
    public void setContentDescription(String description) {
        this.headers.setHeader("Content-Description", description);
        needsSave();
    }

    @Override // javax.xml.soap.SOAPMessage
    public void removeAllAttachments() {
        try {
            initializeAllAttachments();
            if (this.attachments != null) {
                this.attachments.clear();
                needsSave();
            }
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    @Override // javax.xml.soap.SOAPMessage
    public int countAttachments() {
        try {
            initializeAllAttachments();
            if (this.attachments != null) {
                return this.attachments.size();
            }
            return 0;
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    @Override // javax.xml.soap.SOAPMessage
    public void addAttachmentPart(AttachmentPart attachment) {
        try {
            initializeAllAttachments();
            this.optimizeAttachmentProcessing = true;
            if (this.attachments == null) {
                this.attachments = new FinalArrayList();
            }
            this.attachments.add(attachment);
            needsSave();
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    @Override // javax.xml.soap.SOAPMessage
    public Iterator getAttachments() {
        try {
            initializeAllAttachments();
            if (this.attachments == null) {
                return nullIter;
            }
            return this.attachments.iterator();
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    private void setFinalContentType(String charset) {
        ContentType ct = contentType();
        if (ct == null) {
            ct = new ContentType();
        }
        String[] split = getExpectedContentType().split("/");
        ct.setPrimaryType(split[0]);
        ct.setSubType(split[1]);
        ct.setParameter("charset", charset);
        this.headers.setHeader("Content-Type", ct.toString());
    }

    /* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/MessageImpl$MimeMatchingIterator.class */
    private class MimeMatchingIterator implements Iterator {
        private Iterator iter;
        private MimeHeaders headers;
        private Object nextAttachment;

        public MimeMatchingIterator(MimeHeaders headers) {
            this.headers = headers;
            this.iter = MessageImpl.this.attachments.iterator();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.nextAttachment == null) {
                this.nextAttachment = nextMatch();
            }
            return this.nextAttachment != null;
        }

        @Override // java.util.Iterator
        public Object next() {
            if (this.nextAttachment != null) {
                Object ret = this.nextAttachment;
                this.nextAttachment = null;
                return ret;
            }
            if (hasNext()) {
                return this.nextAttachment;
            }
            return null;
        }

        Object nextMatch() {
            while (this.iter.hasNext()) {
                AttachmentPartImpl ap2 = (AttachmentPartImpl) this.iter.next();
                if (ap2.hasAllHeaders(this.headers)) {
                    return ap2;
                }
            }
            return null;
        }

        @Override // java.util.Iterator
        public void remove() {
            this.iter.remove();
        }
    }

    @Override // javax.xml.soap.SOAPMessage
    public Iterator getAttachments(MimeHeaders headers) {
        try {
            initializeAllAttachments();
            if (this.attachments == null) {
                return nullIter;
            }
            return new MimeMatchingIterator(headers);
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    @Override // javax.xml.soap.SOAPMessage
    public void removeAttachments(MimeHeaders headers) {
        try {
            initializeAllAttachments();
            if (this.attachments == null) {
                return;
            }
            Iterator it = new MimeMatchingIterator(headers);
            while (it.hasNext()) {
                int index = this.attachments.indexOf(it.next());
                this.attachments.set(index, null);
            }
            FinalArrayList f2 = new FinalArrayList();
            for (int i2 = 0; i2 < this.attachments.size(); i2++) {
                if (this.attachments.get(i2) != null) {
                    f2.add(this.attachments.get(i2));
                }
            }
            this.attachments = f2;
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    @Override // javax.xml.soap.SOAPMessage
    public AttachmentPart createAttachmentPart() {
        return new AttachmentPartImpl();
    }

    @Override // javax.xml.soap.SOAPMessage
    public AttachmentPart getAttachment(SOAPElement element) throws SOAPException {
        String uri;
        try {
            initializeAllAttachments();
            String hrefAttr = element.getAttribute(Constants.ATTRNAME_HREF);
            if ("".equals(hrefAttr)) {
                Node node = getValueNodeStrict(element);
                String swaRef = null;
                if (node != null) {
                    swaRef = node.getValue();
                }
                if (swaRef == null || "".equals(swaRef)) {
                    return null;
                }
                uri = swaRef;
            } else {
                uri = hrefAttr;
            }
            return getAttachmentPart(uri);
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    private Node getValueNodeStrict(SOAPElement element) {
        Node node = (Node) element.getFirstChild();
        if (node != null && node.getNextSibling() == null && node.getNodeType() == 3) {
            return node;
        }
        return null;
    }

    private AttachmentPart getAttachmentPart(String uri) throws SOAPException {
        AttachmentPart _part;
        int eqIndex;
        try {
            if (uri.startsWith("cid:")) {
                uri = '<' + uri.substring("cid:".length()) + '>';
                MimeHeaders headersToMatch = new MimeHeaders();
                headersToMatch.addHeader(CONTENT_ID, uri);
                Iterator i2 = getAttachments(headersToMatch);
                _part = i2 == null ? null : (AttachmentPart) i2.next();
            } else {
                MimeHeaders headersToMatch2 = new MimeHeaders();
                headersToMatch2.addHeader(CONTENT_LOCATION, uri);
                Iterator i3 = getAttachments(headersToMatch2);
                _part = i3 == null ? null : (AttachmentPart) i3.next();
            }
            if (_part == null) {
                Iterator j2 = getAttachments();
                while (true) {
                    if (!j2.hasNext()) {
                        break;
                    }
                    AttachmentPart p2 = (AttachmentPart) j2.next();
                    String cl = p2.getContentId();
                    if (cl != null && (eqIndex = cl.indexOf("=")) > -1 && cl.substring(1, eqIndex).equalsIgnoreCase(uri)) {
                        _part = p2;
                        break;
                    }
                }
            }
            return _part;
        } catch (Exception se) {
            log.log(Level.SEVERE, "SAAJ0590.soap.unable.to.locate.attachment", new Object[]{uri});
            throw new SOAPExceptionImpl(se);
        }
    }

    private final InputStream getHeaderBytes() throws IOException {
        SOAPPartImpl sp = (SOAPPartImpl) getSOAPPart();
        return sp.getContentAsStream();
    }

    private String convertToSingleLine(String contentType) {
        StringBuffer buffer = new StringBuffer();
        for (int i2 = 0; i2 < contentType.length(); i2++) {
            char c2 = contentType.charAt(i2);
            if (c2 != '\r' && c2 != '\n' && c2 != '\t') {
                buffer.append(c2);
            }
        }
        return buffer.toString();
    }

    private MimeMultipart getMimeMessage() throws SOAPException {
        MimeMultipart headerAndBody;
        try {
            SOAPPartImpl soapPart = (SOAPPartImpl) getSOAPPart();
            MimeBodyPart mimeSoapPart = soapPart.getMimePart();
            ContentType soapPartCtype = new ContentType(getExpectedContentType());
            if (!this.isFastInfoset) {
                soapPartCtype.setParameter("charset", initCharset());
            }
            mimeSoapPart.setHeader("Content-Type", soapPartCtype.toString());
            if (!switchOffBM && !switchOffLazyAttachment && this.multiPart != null && !this.attachmentsInitialized) {
                headerAndBody = new BMMimeMultipart();
                headerAndBody.addBodyPart(mimeSoapPart);
                if (this.attachments != null) {
                    Iterator eachAttachment = this.attachments.iterator();
                    while (eachAttachment.hasNext()) {
                        headerAndBody.addBodyPart(((AttachmentPartImpl) eachAttachment.next()).getMimePart());
                    }
                }
                InputStream in = ((BMMimeMultipart) this.multiPart).getInputStream();
                if (!((BMMimeMultipart) this.multiPart).lastBodyPartFound() && !((BMMimeMultipart) this.multiPart).isEndOfStream()) {
                    ((BMMimeMultipart) headerAndBody).setInputStream(in);
                    ((BMMimeMultipart) headerAndBody).setBoundary(((BMMimeMultipart) this.multiPart).getBoundary());
                    ((BMMimeMultipart) headerAndBody).setLazyAttachments(this.lazyAttachments);
                }
            } else {
                headerAndBody = new MimeMultipart();
                headerAndBody.addBodyPart(mimeSoapPart);
                Iterator eachAttachement = getAttachments();
                while (eachAttachement.hasNext()) {
                    headerAndBody.addBodyPart(((AttachmentPartImpl) eachAttachement.next()).getMimePart());
                }
            }
            ContentType contentType = headerAndBody.getContentType();
            ParameterList l2 = contentType.getParameterList();
            l2.set("type", getExpectedContentType());
            l2.set("boundary", contentType.getParameter("boundary"));
            ContentType nct = new ContentType("multipart", "related", l2);
            this.headers.setHeader("Content-Type", convertToSingleLine(nct.toString()));
            return headerAndBody;
        } catch (SOAPException ex) {
            throw ex;
        } catch (Throwable ex2) {
            log.severe("SAAJ0538.soap.cannot.convert.msg.to.multipart.obj");
            throw new SOAPExceptionImpl("Unable to convert SOAP message into a MimeMultipart object", ex2);
        }
    }

    private String initCharset() {
        String charset = null;
        String[] cts = getMimeHeaders().getHeader("Content-Type");
        if (cts != null && cts[0] != null) {
            charset = getCharsetString(cts[0]);
        }
        if (charset == null) {
            charset = (String) getProperty(SOAPMessage.CHARACTER_SET_ENCODING);
        }
        if (charset != null) {
            return charset;
        }
        return "utf-8";
    }

    private String getCharsetString(String s2) {
        try {
            int index = s2.indexOf(";");
            if (index < 0) {
                return null;
            }
            ParameterList pl = new ParameterList(s2.substring(index));
            return pl.get("charset");
        } catch (Exception e2) {
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x00b2 A[Catch: Throwable -> 0x00cf, TryCatch #0 {Throwable -> 0x00cf, blocks: (B:19:0x003c, B:21:0x0043, B:23:0x0053, B:28:0x0073, B:30:0x007a, B:31:0x0092, B:33:0x009e, B:26:0x005d, B:27:0x0072, B:35:0x00b2, B:37:0x00b9, B:38:0x00c4), top: B:45:0x003c, inners: #1 }] */
    @Override // javax.xml.soap.SOAPMessage
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void saveChanges() throws javax.xml.soap.SOAPException {
        /*
            Method dump skipped, instructions count: 235
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.xml.internal.messaging.saaj.soap.MessageImpl.saveChanges():void");
    }

    private MimeMultipart getXOPMessage() throws SOAPException {
        String action;
        try {
            MimeMultipart headerAndBody = new MimeMultipart();
            SOAPPartImpl soapPart = (SOAPPartImpl) getSOAPPart();
            MimeBodyPart mimeSoapPart = soapPart.getMimePart();
            ContentType soapPartCtype = new ContentType(MtomCodec.XOP_XML_MIME_TYPE);
            soapPartCtype.setParameter("type", getExpectedContentType());
            String charset = initCharset();
            soapPartCtype.setParameter("charset", charset);
            mimeSoapPart.setHeader("Content-Type", soapPartCtype.toString());
            headerAndBody.addBodyPart(mimeSoapPart);
            Iterator eachAttachement = getAttachments();
            while (eachAttachement.hasNext()) {
                headerAndBody.addBodyPart(((AttachmentPartImpl) eachAttachement.next()).getMimePart());
            }
            ContentType contentType = headerAndBody.getContentType();
            ParameterList l2 = contentType.getParameterList();
            l2.set("start-info", getExpectedContentType());
            l2.set("type", MtomCodec.XOP_XML_MIME_TYPE);
            if (isCorrectSoapVersion(8) && (action = getAction()) != null) {
                l2.set("action", action);
            }
            l2.set("boundary", contentType.getParameter("boundary"));
            ContentType nct = new ContentType("Multipart", "Related", l2);
            this.headers.setHeader("Content-Type", convertToSingleLine(nct.toString()));
            return headerAndBody;
        } catch (SOAPException ex) {
            throw ex;
        } catch (Throwable ex2) {
            log.severe("SAAJ0538.soap.cannot.convert.msg.to.multipart.obj");
            throw new SOAPExceptionImpl("Unable to convert SOAP message into a MimeMultipart object", ex2);
        }
    }

    private boolean hasXOPContent() throws ParseException {
        String type = getContentType();
        if (type == null) {
            return false;
        }
        ContentType ct = new ContentType(type);
        return isMimeMultipartXOPSoap1_1Package(ct) || isMimeMultipartXOPSoap1_2Package(ct) || isSOAPBodyXOPPackage(ct);
    }

    @Override // javax.xml.soap.SOAPMessage
    public void writeTo(OutputStream out) throws SOAPException, IOException {
        String[] soapAction;
        if (saveRequired()) {
            this.optimizeAttachmentProcessing = true;
            saveChanges();
        }
        if (!this.optimizeAttachmentProcessing) {
            if (SOAPPartImpl.lazyContentLength && this.messageByteCount <= 0) {
                byte[] buf = new byte[1024];
                while (true) {
                    int length = this.inputStreamAfterSaveChanges.read(buf);
                    if (length == -1) {
                        break;
                    }
                    out.write(buf, 0, length);
                    this.messageByteCount += length;
                }
                if (this.messageByteCount > 0) {
                    this.headers.setHeader("Content-Length", Integer.toString(this.messageByteCount));
                }
            } else {
                out.write(this.messageBytes, 0, this.messageByteCount);
            }
        } else {
            try {
                if (hasXOPContent()) {
                    this.mmp.writeTo(out);
                } else {
                    this.mmp.writeTo(out);
                    if (!switchOffBM && !switchOffLazyAttachment && this.multiPart != null && !this.attachmentsInitialized) {
                        ((BMMimeMultipart) this.multiPart).setInputStream(((BMMimeMultipart) this.mmp).getInputStream());
                    }
                }
            } catch (Exception ex) {
                log.severe("SAAJ0540.soap.err.saving.multipart.msg");
                throw new SOAPExceptionImpl("Error during saving a multipart message", ex);
            }
        }
        if (isCorrectSoapVersion(4) && ((soapAction = this.headers.getHeader("SOAPAction")) == null || soapAction.length == 0)) {
            this.headers.setHeader("SOAPAction", "\"\"");
        }
        this.messageBytes = null;
        needsSave();
    }

    @Override // javax.xml.soap.SOAPMessage
    public SOAPBody getSOAPBody() throws SOAPException {
        SOAPBody body = getSOAPPart().getEnvelope().getBody();
        return body;
    }

    @Override // javax.xml.soap.SOAPMessage
    public SOAPHeader getSOAPHeader() throws SOAPException {
        SOAPHeader hdr = getSOAPPart().getEnvelope().getHeader();
        return hdr;
    }

    private void initializeAllAttachments() throws MessagingException, SOAPException, IllegalArgumentException {
        if (switchOffBM || switchOffLazyAttachment || this.attachmentsInitialized || this.multiPart == null) {
            return;
        }
        if (this.attachments == null) {
            this.attachments = new FinalArrayList();
        }
        int count = this.multiPart.getCount();
        for (int i2 = 0; i2 < count; i2++) {
            initializeAttachment(this.multiPart.getBodyPart(i2));
        }
        this.attachmentsInitialized = true;
        needsSave();
    }

    private void initializeAttachment(MimeBodyPart mbp) throws SOAPException, IllegalArgumentException {
        AttachmentPartImpl attachmentPart = new AttachmentPartImpl();
        DataHandler attachmentHandler = mbp.getDataHandler();
        attachmentPart.setDataHandler(attachmentHandler);
        AttachmentPartImpl.copyMimeHeaders(mbp, attachmentPart);
        this.attachments.add(attachmentPart);
    }

    private void initializeAttachment(MimeMultipart multiPart, int i2) throws Exception {
        MimeBodyPart currentBodyPart = multiPart.getBodyPart(i2);
        AttachmentPartImpl attachmentPart = new AttachmentPartImpl();
        DataHandler attachmentHandler = currentBodyPart.getDataHandler();
        attachmentPart.setDataHandler(attachmentHandler);
        AttachmentPartImpl.copyMimeHeaders(currentBodyPart, attachmentPart);
        addAttachmentPart(attachmentPart);
    }

    private void setMimeHeaders(SOAPPart soapPart, MimeBodyPart soapMessagePart) throws Exception {
        soapPart.removeAllMimeHeaders();
        List headers = soapMessagePart.getAllHeaders();
        int sz = headers.size();
        for (int i2 = 0; i2 < sz; i2++) {
            Header h2 = (Header) headers.get(i2);
            soapPart.addMimeHeader(h2.getName(), h2.getValue());
        }
    }

    private void initCharsetProperty(ContentType contentType) {
        String charset = contentType.getParameter("charset");
        if (charset != null) {
            ((SOAPPartImpl) getSOAPPart()).setSourceCharsetEncoding(charset);
            if (!charset.equalsIgnoreCase("utf-8")) {
                setProperty(SOAPMessage.CHARACTER_SET_ENCODING, charset);
            }
        }
    }

    public void setLazyAttachments(boolean flag) {
        this.lazyAttachments = flag;
    }
}
