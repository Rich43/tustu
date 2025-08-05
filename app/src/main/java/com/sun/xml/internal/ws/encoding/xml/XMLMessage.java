package com.sun.xml.internal.ws.encoding.xml;

import com.sun.istack.internal.NotNull;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.xml.internal.bind.api.Bridge;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSFeatureList;
import com.sun.xml.internal.ws.api.message.AttachmentSet;
import com.sun.xml.internal.ws.api.message.HeaderList;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.MessageHeaders;
import com.sun.xml.internal.ws.api.message.Messages;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.api.pipe.Codec;
import com.sun.xml.internal.ws.api.streaming.XMLStreamWriterFactory;
import com.sun.xml.internal.ws.developer.StreamingAttachmentFeature;
import com.sun.xml.internal.ws.encoding.ContentType;
import com.sun.xml.internal.ws.encoding.MimeMultipartParser;
import com.sun.xml.internal.ws.encoding.XMLHTTPBindingCodec;
import com.sun.xml.internal.ws.message.AbstractMessageImpl;
import com.sun.xml.internal.ws.message.EmptyMessageImpl;
import com.sun.xml.internal.ws.message.MimeAttachmentSet;
import com.sun.xml.internal.ws.message.source.PayloadSourceMessage;
import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import com.sun.xml.internal.ws.util.StreamUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataSource;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.WebServiceException;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/xml/XMLMessage.class */
public final class XMLMessage {
    private static final int PLAIN_XML_FLAG = 1;
    private static final int MIME_MULTIPART_FLAG = 2;
    private static final int FI_ENCODED_FLAG = 16;

    /* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/xml/XMLMessage$MessageDataSource.class */
    public interface MessageDataSource {
        boolean hasUnconsumedDataSource();

        DataSource getDataSource();
    }

    public static Message create(String ct, InputStream in, WSFeatureList f2) {
        Message data;
        try {
            InputStream in2 = StreamUtils.hasSomeData(in);
            if (in2 == null) {
                return Messages.createEmpty(SOAPVersion.SOAP_11);
            }
            if (ct != null) {
                ContentType contentType = new ContentType(ct);
                int contentTypeId = identifyContentType(contentType);
                if ((contentTypeId & 2) != 0) {
                    data = new XMLMultiPart(ct, in2, f2);
                } else if ((contentTypeId & 1) != 0) {
                    data = new XmlContent(ct, in2, f2);
                } else {
                    data = new UnknownContent(ct, in2);
                }
            } else {
                data = new UnknownContent(Locator.DEFAULT_CONTENT_TYPE, in2);
            }
            return data;
        } catch (Exception ex) {
            throw new WebServiceException(ex);
        }
    }

    public static Message create(Source source) {
        if (source == null) {
            return Messages.createEmpty(SOAPVersion.SOAP_11);
        }
        return Messages.createUsingPayload(source, SOAPVersion.SOAP_11);
    }

    public static Message create(DataSource ds, WSFeatureList f2) {
        Message messageCreate;
        try {
            if (ds == null) {
                messageCreate = Messages.createEmpty(SOAPVersion.SOAP_11);
            } else {
                messageCreate = create(ds.getContentType(), ds.getInputStream(), f2);
            }
            return messageCreate;
        } catch (IOException ioe) {
            throw new WebServiceException(ioe);
        }
    }

    public static Message create(Exception e2) {
        return new FaultMessage(SOAPVersion.SOAP_11);
    }

    private static int getContentId(String ct) {
        try {
            ContentType contentType = new ContentType(ct);
            return identifyContentType(contentType);
        } catch (Exception ex) {
            throw new WebServiceException(ex);
        }
    }

    public static boolean isFastInfoset(String ct) {
        return (getContentId(ct) & 16) != 0;
    }

    public static int identifyContentType(ContentType contentType) {
        String primary = contentType.getPrimaryType();
        String sub = contentType.getSubType();
        if (primary.equalsIgnoreCase("multipart") && sub.equalsIgnoreCase("related")) {
            String type = contentType.getParameter("type");
            if (type != null) {
                if (isXMLType(type)) {
                    return 3;
                }
                if (isFastInfosetType(type)) {
                    return 18;
                }
                return 0;
            }
            return 0;
        }
        if (isXMLType(primary, sub)) {
            return 1;
        }
        if (isFastInfosetType(primary, sub)) {
            return 16;
        }
        return 0;
    }

    protected static boolean isXMLType(@NotNull String primary, @NotNull String sub) {
        return (primary.equalsIgnoreCase("text") && sub.equalsIgnoreCase("xml")) || (primary.equalsIgnoreCase("application") && sub.equalsIgnoreCase("xml")) || (primary.equalsIgnoreCase("application") && sub.toLowerCase().endsWith("+xml"));
    }

    protected static boolean isXMLType(String type) {
        String lowerType = type.toLowerCase();
        return lowerType.startsWith("text/xml") || lowerType.startsWith(XMLCodec.XML_APPLICATION_MIME_TYPE) || (lowerType.startsWith("application/") && lowerType.indexOf("+xml") != -1);
    }

    protected static boolean isFastInfosetType(String primary, String sub) {
        return primary.equalsIgnoreCase("application") && sub.equalsIgnoreCase("fastinfoset");
    }

    protected static boolean isFastInfosetType(String type) {
        return type.toLowerCase().startsWith("application/fastinfoset");
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/xml/XMLMessage$XmlContent.class */
    private static class XmlContent extends AbstractMessageImpl implements MessageDataSource {
        private final XmlDataSource dataSource;
        private boolean consumed;
        private Message delegate;
        private final HeaderList headerList;
        private WSFeatureList features;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !XMLMessage.class.desiredAssertionStatus();
        }

        public XmlContent(String ct, InputStream in, WSFeatureList f2) {
            super(SOAPVersion.SOAP_11);
            this.dataSource = new XmlDataSource(ct, in);
            this.headerList = new HeaderList(SOAPVersion.SOAP_11);
            this.features = f2;
        }

        private Message getMessage() {
            if (this.delegate == null) {
                InputStream in = this.dataSource.getInputStream();
                if (!$assertionsDisabled && in == null) {
                    throw new AssertionError();
                }
                this.delegate = Messages.createUsingPayload(new StreamSource(in), SOAPVersion.SOAP_11);
                this.consumed = true;
            }
            return this.delegate;
        }

        @Override // com.sun.xml.internal.ws.encoding.xml.XMLMessage.MessageDataSource
        public boolean hasUnconsumedDataSource() {
            return (this.dataSource.consumed() || this.consumed) ? false : true;
        }

        @Override // com.sun.xml.internal.ws.encoding.xml.XMLMessage.MessageDataSource
        public DataSource getDataSource() {
            return hasUnconsumedDataSource() ? this.dataSource : XMLMessage.getDataSource(getMessage(), this.features);
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        public boolean hasHeaders() {
            return false;
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        @NotNull
        public MessageHeaders getHeaders() {
            return this.headerList;
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        public String getPayloadLocalPart() {
            return getMessage().getPayloadLocalPart();
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        public String getPayloadNamespaceURI() {
            return getMessage().getPayloadNamespaceURI();
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        public boolean hasPayload() {
            return true;
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        public boolean isFault() {
            return false;
        }

        @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
        public Source readEnvelopeAsSource() {
            return getMessage().readEnvelopeAsSource();
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        public Source readPayloadAsSource() {
            return getMessage().readPayloadAsSource();
        }

        @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
        public SOAPMessage readAsSOAPMessage() throws SOAPException {
            return getMessage().readAsSOAPMessage();
        }

        @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
        public SOAPMessage readAsSOAPMessage(Packet packet, boolean inbound) throws SOAPException {
            return getMessage().readAsSOAPMessage(packet, inbound);
        }

        @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
        public <T> T readPayloadAsJAXB(Unmarshaller unmarshaller) throws JAXBException {
            return (T) getMessage().readPayloadAsJAXB(unmarshaller);
        }

        @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
        public <T> T readPayloadAsJAXB(Bridge<T> bridge) throws JAXBException {
            return (T) getMessage().readPayloadAsJAXB(bridge);
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        public XMLStreamReader readPayload() throws XMLStreamException {
            return getMessage().readPayload();
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        public void writePayloadTo(XMLStreamWriter sw) throws XMLStreamException {
            getMessage().writePayloadTo(sw);
        }

        @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
        public void writeTo(XMLStreamWriter sw) throws XMLStreamException {
            getMessage().writeTo(sw);
        }

        @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
        public void writeTo(ContentHandler contentHandler, ErrorHandler errorHandler) throws SAXException {
            getMessage().writeTo(contentHandler, errorHandler);
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        public Message copy() {
            return getMessage().copy();
        }

        @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl
        protected void writePayloadTo(ContentHandler contentHandler, ErrorHandler errorHandler, boolean fragment) throws SAXException {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/xml/XMLMessage$XMLMultiPart.class */
    public static final class XMLMultiPart extends AbstractMessageImpl implements MessageDataSource {
        private final DataSource dataSource;
        private final StreamingAttachmentFeature feature;
        private Message delegate;
        private HeaderList headerList;
        private final WSFeatureList features;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !XMLMessage.class.desiredAssertionStatus();
        }

        public XMLMultiPart(String contentType, InputStream is, WSFeatureList f2) {
            super(SOAPVersion.SOAP_11);
            this.headerList = new HeaderList(SOAPVersion.SOAP_11);
            this.dataSource = XMLMessage.createDataSource(contentType, is);
            this.feature = (StreamingAttachmentFeature) f2.get(StreamingAttachmentFeature.class);
            this.features = f2;
        }

        private Message getMessage() {
            if (this.delegate == null) {
                try {
                    MimeMultipartParser mpp = new MimeMultipartParser(this.dataSource.getInputStream(), this.dataSource.getContentType(), this.feature);
                    InputStream in = mpp.getRootPart().asInputStream();
                    if (!$assertionsDisabled && in == null) {
                        throw new AssertionError();
                    }
                    this.delegate = new PayloadSourceMessage(this.headerList, new StreamSource(in), new MimeAttachmentSet(mpp), SOAPVersion.SOAP_11);
                } catch (IOException ioe) {
                    throw new WebServiceException(ioe);
                }
            }
            return this.delegate;
        }

        @Override // com.sun.xml.internal.ws.encoding.xml.XMLMessage.MessageDataSource
        public boolean hasUnconsumedDataSource() {
            return this.delegate == null;
        }

        @Override // com.sun.xml.internal.ws.encoding.xml.XMLMessage.MessageDataSource
        public DataSource getDataSource() {
            return hasUnconsumedDataSource() ? this.dataSource : XMLMessage.getDataSource(getMessage(), this.features);
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        public boolean hasHeaders() {
            return false;
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        @NotNull
        public MessageHeaders getHeaders() {
            return this.headerList;
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        public String getPayloadLocalPart() {
            return getMessage().getPayloadLocalPart();
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        public String getPayloadNamespaceURI() {
            return getMessage().getPayloadNamespaceURI();
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        public boolean hasPayload() {
            return true;
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        public boolean isFault() {
            return false;
        }

        @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
        public Source readEnvelopeAsSource() {
            return getMessage().readEnvelopeAsSource();
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        public Source readPayloadAsSource() {
            return getMessage().readPayloadAsSource();
        }

        @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
        public SOAPMessage readAsSOAPMessage() throws SOAPException {
            return getMessage().readAsSOAPMessage();
        }

        @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
        public SOAPMessage readAsSOAPMessage(Packet packet, boolean inbound) throws SOAPException {
            return getMessage().readAsSOAPMessage(packet, inbound);
        }

        @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
        public <T> T readPayloadAsJAXB(Unmarshaller unmarshaller) throws JAXBException {
            return (T) getMessage().readPayloadAsJAXB(unmarshaller);
        }

        @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
        public <T> T readPayloadAsJAXB(Bridge<T> bridge) throws JAXBException {
            return (T) getMessage().readPayloadAsJAXB(bridge);
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        public XMLStreamReader readPayload() throws XMLStreamException {
            return getMessage().readPayload();
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        public void writePayloadTo(XMLStreamWriter sw) throws XMLStreamException {
            getMessage().writePayloadTo(sw);
        }

        @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
        public void writeTo(XMLStreamWriter sw) throws XMLStreamException {
            getMessage().writeTo(sw);
        }

        @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl, com.sun.xml.internal.ws.api.message.Message
        public void writeTo(ContentHandler contentHandler, ErrorHandler errorHandler) throws SAXException {
            getMessage().writeTo(contentHandler, errorHandler);
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        public Message copy() {
            return getMessage().copy();
        }

        @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl
        protected void writePayloadTo(ContentHandler contentHandler, ErrorHandler errorHandler, boolean fragment) throws SAXException {
            throw new UnsupportedOperationException();
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        public boolean isOneWay(@NotNull WSDLPort port) {
            return false;
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        @NotNull
        public AttachmentSet getAttachments() {
            return getMessage().getAttachments();
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/xml/XMLMessage$FaultMessage.class */
    private static class FaultMessage extends EmptyMessageImpl {
        public FaultMessage(SOAPVersion version) {
            super(version);
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        public boolean isFault() {
            return true;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/xml/XMLMessage$UnknownContent.class */
    public static class UnknownContent extends AbstractMessageImpl implements MessageDataSource {
        private final DataSource ds;
        private final HeaderList headerList;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !XMLMessage.class.desiredAssertionStatus();
        }

        public UnknownContent(String ct, InputStream in) {
            this(XMLMessage.createDataSource(ct, in));
        }

        public UnknownContent(DataSource ds) {
            super(SOAPVersion.SOAP_11);
            this.ds = ds;
            this.headerList = new HeaderList(SOAPVersion.SOAP_11);
        }

        private UnknownContent(UnknownContent that) {
            super(that.soapVersion);
            this.ds = that.ds;
            this.headerList = HeaderList.copy(that.headerList);
        }

        @Override // com.sun.xml.internal.ws.encoding.xml.XMLMessage.MessageDataSource
        public boolean hasUnconsumedDataSource() {
            return true;
        }

        @Override // com.sun.xml.internal.ws.encoding.xml.XMLMessage.MessageDataSource
        public DataSource getDataSource() {
            if ($assertionsDisabled || this.ds != null) {
                return this.ds;
            }
            throw new AssertionError();
        }

        @Override // com.sun.xml.internal.ws.message.AbstractMessageImpl
        protected void writePayloadTo(ContentHandler contentHandler, ErrorHandler errorHandler, boolean fragment) throws SAXException {
            throw new UnsupportedOperationException();
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        public boolean hasHeaders() {
            return false;
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        public boolean isFault() {
            return false;
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        public MessageHeaders getHeaders() {
            return this.headerList;
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        public String getPayloadLocalPart() {
            throw new UnsupportedOperationException();
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        public String getPayloadNamespaceURI() {
            throw new UnsupportedOperationException();
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        public boolean hasPayload() {
            return false;
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        public Source readPayloadAsSource() {
            return null;
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        public XMLStreamReader readPayload() throws XMLStreamException {
            throw new WebServiceException("There isn't XML payload. Shouldn't come here.");
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        public void writePayloadTo(XMLStreamWriter sw) throws XMLStreamException {
        }

        @Override // com.sun.xml.internal.ws.api.message.Message
        public Message copy() {
            return new UnknownContent(this);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static DataSource getDataSource(Message message, WSFeatureList f2) {
        if (message == 0) {
            return null;
        }
        if (message instanceof MessageDataSource) {
            return ((MessageDataSource) message).getDataSource();
        }
        AttachmentSet atts = message.getAttachments();
        if (atts != null && !atts.isEmpty()) {
            ByteArrayBuffer bos = new ByteArrayBuffer();
            try {
                Codec codec = new XMLHTTPBindingCodec(f2);
                Packet packet = new Packet(message);
                com.sun.xml.internal.ws.api.pipe.ContentType ct = codec.getStaticContentType(packet);
                codec.encode(packet, bos);
                return createDataSource(ct.getContentType(), bos.newInputStream());
            } catch (IOException ioe) {
                throw new WebServiceException(ioe);
            }
        }
        ByteArrayBuffer bos2 = new ByteArrayBuffer();
        XMLStreamWriter writer = XMLStreamWriterFactory.create(bos2);
        try {
            message.writePayloadTo(writer);
            writer.flush();
            return createDataSource("text/xml", bos2.newInputStream());
        } catch (XMLStreamException e2) {
            throw new WebServiceException(e2);
        }
    }

    public static DataSource createDataSource(String contentType, InputStream is) {
        return new XmlDataSource(contentType, is);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/xml/XMLMessage$XmlDataSource.class */
    private static class XmlDataSource implements DataSource {
        private final String contentType;
        private final InputStream is;
        private boolean consumed;

        XmlDataSource(String contentType, InputStream is) {
            this.contentType = contentType;
            this.is = is;
        }

        public boolean consumed() {
            return this.consumed;
        }

        @Override // javax.activation.DataSource
        public InputStream getInputStream() {
            this.consumed = !this.consumed;
            return this.is;
        }

        @Override // javax.activation.DataSource
        public OutputStream getOutputStream() {
            return null;
        }

        @Override // javax.activation.DataSource
        public String getContentType() {
            return this.contentType;
        }

        @Override // javax.activation.DataSource
        public String getName() {
            return "";
        }
    }
}
