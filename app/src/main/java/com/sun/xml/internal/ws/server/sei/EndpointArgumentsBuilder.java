package com.sun.xml.internal.ws.server.sei;

import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.message.Attachment;
import com.sun.xml.internal.ws.api.message.AttachmentSet;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.model.ParameterBinding;
import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
import com.sun.xml.internal.ws.encoding.DataHandlerDataSource;
import com.sun.xml.internal.ws.encoding.StringDataContentHandler;
import com.sun.xml.internal.ws.encoding.xml.XMLCodec;
import com.sun.xml.internal.ws.message.AttachmentUnmarshallerImpl;
import com.sun.xml.internal.ws.model.ParameterImpl;
import com.sun.xml.internal.ws.model.WrapperParameter;
import com.sun.xml.internal.ws.resources.ServerMessages;
import com.sun.xml.internal.ws.spi.db.DatabindingException;
import com.sun.xml.internal.ws.spi.db.PropertyAccessor;
import com.sun.xml.internal.ws.spi.db.RepeatedElementBridge;
import com.sun.xml.internal.ws.spi.db.WrapperComposite;
import com.sun.xml.internal.ws.spi.db.XMLBridge;
import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.activation.DataHandler;
import javax.imageio.ImageIO;
import javax.jws.WebParam;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPFaultException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/EndpointArgumentsBuilder.class */
public abstract class EndpointArgumentsBuilder {
    public static final EndpointArgumentsBuilder NONE = new None();
    private static final Map<Class, Object> primitiveUninitializedValues = new HashMap();
    protected QName wrapperName;
    protected Map<QName, WrappedPartBuilder> wrappedParts = null;

    public abstract void readRequest(Message message, Object[] objArr) throws XMLStreamException, JAXBException;

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/EndpointArgumentsBuilder$None.class */
    static final class None extends EndpointArgumentsBuilder {
        private None() {
        }

        @Override // com.sun.xml.internal.ws.server.sei.EndpointArgumentsBuilder
        public void readRequest(Message msg, Object[] args) {
            msg.consume();
        }
    }

    static {
        Map<Class, Object> m2 = primitiveUninitializedValues;
        m2.put(Integer.TYPE, 0);
        m2.put(Character.TYPE, (char) 0);
        m2.put(Byte.TYPE, (byte) 0);
        m2.put(Short.TYPE, (short) 0);
        m2.put(Long.TYPE, 0L);
        m2.put(Float.TYPE, Float.valueOf(0.0f));
        m2.put(Double.TYPE, Double.valueOf(0.0d));
    }

    public static Object getVMUninitializedValue(Type type) {
        return primitiveUninitializedValues.get(type);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/EndpointArgumentsBuilder$WrappedPartBuilder.class */
    static final class WrappedPartBuilder {
        private final XMLBridge bridge;
        private final EndpointValueSetter setter;

        public WrappedPartBuilder(XMLBridge bridge, EndpointValueSetter setter) {
            this.bridge = bridge;
            this.setter = setter;
        }

        void readRequest(Object[] args, XMLStreamReader r2, AttachmentSet att) throws JAXBException {
            Object obj;
            AttachmentUnmarshallerImpl au2 = att != null ? new AttachmentUnmarshallerImpl(att) : null;
            if (this.bridge instanceof RepeatedElementBridge) {
                RepeatedElementBridge rbridge = (RepeatedElementBridge) this.bridge;
                ArrayList list = new ArrayList();
                QName name = r2.getName();
                while (r2.getEventType() == 1 && name.equals(r2.getName())) {
                    list.add(rbridge.unmarshal(r2, au2));
                    XMLStreamReaderUtil.toNextTag(r2, name);
                }
                obj = rbridge.collectionHandler().convert(list);
            } else {
                obj = this.bridge.unmarshal(r2, au2);
            }
            this.setter.put(obj, args);
        }
    }

    protected void readWrappedRequest(Message msg, Object[] args) throws XMLStreamException, JAXBException {
        if (!msg.hasPayload()) {
            throw new WebServiceException("No payload. Expecting payload with " + ((Object) this.wrapperName) + " element");
        }
        XMLStreamReader reader = msg.readPayload();
        XMLStreamReaderUtil.verifyTag(reader, this.wrapperName);
        reader.nextTag();
        while (reader.getEventType() == 1) {
            QName name = reader.getName();
            WrappedPartBuilder part = this.wrappedParts.get(name);
            if (part == null) {
                XMLStreamReaderUtil.skipElement(reader);
                reader.nextTag();
            } else {
                part.readRequest(args, reader, msg.getAttachments());
            }
            XMLStreamReaderUtil.toNextTag(reader, name);
        }
        reader.close();
        XMLStreamReaderFactory.recycle(reader);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/EndpointArgumentsBuilder$NullSetter.class */
    public static final class NullSetter extends EndpointArgumentsBuilder {
        private final EndpointValueSetter setter;
        private final Object nullValue;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !EndpointArgumentsBuilder.class.desiredAssertionStatus();
        }

        public NullSetter(EndpointValueSetter setter, Object nullValue) {
            if (!$assertionsDisabled && setter == null) {
                throw new AssertionError();
            }
            this.nullValue = nullValue;
            this.setter = setter;
        }

        @Override // com.sun.xml.internal.ws.server.sei.EndpointArgumentsBuilder
        public void readRequest(Message msg, Object[] args) {
            this.setter.put(this.nullValue, args);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/EndpointArgumentsBuilder$Composite.class */
    public static final class Composite extends EndpointArgumentsBuilder {
        private final EndpointArgumentsBuilder[] builders;

        public Composite(EndpointArgumentsBuilder... builders) {
            this.builders = builders;
        }

        public Composite(Collection<? extends EndpointArgumentsBuilder> builders) {
            this((EndpointArgumentsBuilder[]) builders.toArray(new EndpointArgumentsBuilder[builders.size()]));
        }

        @Override // com.sun.xml.internal.ws.server.sei.EndpointArgumentsBuilder
        public void readRequest(Message msg, Object[] args) throws XMLStreamException, JAXBException {
            for (EndpointArgumentsBuilder builder : this.builders) {
                builder.readRequest(msg, args);
            }
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/EndpointArgumentsBuilder$AttachmentBuilder.class */
    public static abstract class AttachmentBuilder extends EndpointArgumentsBuilder {
        protected final EndpointValueSetter setter;
        protected final ParameterImpl param;
        protected final String pname;
        protected final String pname1;

        abstract void mapAttachment(Attachment attachment, Object[] objArr) throws JAXBException;

        AttachmentBuilder(ParameterImpl param, EndpointValueSetter setter) {
            this.setter = setter;
            this.param = param;
            this.pname = param.getPartName();
            this.pname1 = "<" + this.pname;
        }

        public static EndpointArgumentsBuilder createAttachmentBuilder(ParameterImpl param, EndpointValueSetter setter) {
            Class type = (Class) param.getTypeInfo().type;
            if (DataHandler.class.isAssignableFrom(type)) {
                return new DataHandlerBuilder(param, setter);
            }
            if (byte[].class == type) {
                return new ByteArrayBuilder(param, setter);
            }
            if (Source.class.isAssignableFrom(type)) {
                return new SourceBuilder(param, setter);
            }
            if (Image.class.isAssignableFrom(type)) {
                return new ImageBuilder(param, setter);
            }
            if (InputStream.class != type) {
                if (EndpointArgumentsBuilder.isXMLMimeType(param.getBinding().getMimeType())) {
                    return new JAXBBuilder(param, setter);
                }
                if (String.class.isAssignableFrom(type)) {
                    return new StringBuilder(param, setter);
                }
                throw new UnsupportedOperationException("Unknown Type=" + ((Object) type) + " Attachment is not mapped.");
            }
            return new InputStreamBuilder(param, setter);
        }

        @Override // com.sun.xml.internal.ws.server.sei.EndpointArgumentsBuilder
        public void readRequest(Message msg, Object[] args) throws XMLStreamException, JAXBException {
            boolean foundAttachment = false;
            for (Attachment att : msg.getAttachments()) {
                String part = getWSDLPartName(att);
                if (part != null && (part.equals(this.pname) || part.equals(this.pname1))) {
                    foundAttachment = true;
                    mapAttachment(att, args);
                    break;
                }
            }
            if (!foundAttachment) {
                throw new WebServiceException("Missing Attachment for " + this.pname);
            }
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/EndpointArgumentsBuilder$DataHandlerBuilder.class */
    private static final class DataHandlerBuilder extends AttachmentBuilder {
        DataHandlerBuilder(ParameterImpl param, EndpointValueSetter setter) {
            super(param, setter);
        }

        @Override // com.sun.xml.internal.ws.server.sei.EndpointArgumentsBuilder.AttachmentBuilder
        void mapAttachment(Attachment att, Object[] args) {
            this.setter.put(att.asDataHandler(), args);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/EndpointArgumentsBuilder$ByteArrayBuilder.class */
    private static final class ByteArrayBuilder extends AttachmentBuilder {
        ByteArrayBuilder(ParameterImpl param, EndpointValueSetter setter) {
            super(param, setter);
        }

        @Override // com.sun.xml.internal.ws.server.sei.EndpointArgumentsBuilder.AttachmentBuilder
        void mapAttachment(Attachment att, Object[] args) {
            this.setter.put(att.asByteArray(), args);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/EndpointArgumentsBuilder$SourceBuilder.class */
    private static final class SourceBuilder extends AttachmentBuilder {
        SourceBuilder(ParameterImpl param, EndpointValueSetter setter) {
            super(param, setter);
        }

        @Override // com.sun.xml.internal.ws.server.sei.EndpointArgumentsBuilder.AttachmentBuilder
        void mapAttachment(Attachment att, Object[] args) {
            this.setter.put(att.asSource(), args);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/EndpointArgumentsBuilder$ImageBuilder.class */
    private static final class ImageBuilder extends AttachmentBuilder {
        ImageBuilder(ParameterImpl param, EndpointValueSetter setter) {
            super(param, setter);
        }

        @Override // com.sun.xml.internal.ws.server.sei.EndpointArgumentsBuilder.AttachmentBuilder
        void mapAttachment(Attachment att, Object[] args) {
            InputStream is = null;
            try {
                try {
                    is = att.asInputStream();
                    Image image = ImageIO.read(is);
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException ioe) {
                            throw new WebServiceException(ioe);
                        }
                    }
                    this.setter.put(image, args);
                } catch (Throwable th) {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException ioe2) {
                            throw new WebServiceException(ioe2);
                        }
                    }
                    throw th;
                }
            } catch (IOException ioe3) {
                throw new WebServiceException(ioe3);
            }
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/EndpointArgumentsBuilder$InputStreamBuilder.class */
    private static final class InputStreamBuilder extends AttachmentBuilder {
        InputStreamBuilder(ParameterImpl param, EndpointValueSetter setter) {
            super(param, setter);
        }

        @Override // com.sun.xml.internal.ws.server.sei.EndpointArgumentsBuilder.AttachmentBuilder
        void mapAttachment(Attachment att, Object[] args) {
            this.setter.put(att.asInputStream(), args);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/EndpointArgumentsBuilder$JAXBBuilder.class */
    private static final class JAXBBuilder extends AttachmentBuilder {
        JAXBBuilder(ParameterImpl param, EndpointValueSetter setter) {
            super(param, setter);
        }

        @Override // com.sun.xml.internal.ws.server.sei.EndpointArgumentsBuilder.AttachmentBuilder
        void mapAttachment(Attachment att, Object[] args) throws JAXBException {
            Object obj = this.param.getXMLBridge().unmarshal(att.asInputStream());
            this.setter.put(obj, args);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/EndpointArgumentsBuilder$StringBuilder.class */
    private static final class StringBuilder extends AttachmentBuilder {
        StringBuilder(ParameterImpl param, EndpointValueSetter setter) {
            super(param, setter);
        }

        @Override // com.sun.xml.internal.ws.server.sei.EndpointArgumentsBuilder.AttachmentBuilder
        void mapAttachment(Attachment att, Object[] args) {
            att.getContentType();
            StringDataContentHandler sdh = new StringDataContentHandler();
            try {
                String str = (String) sdh.getContent(new DataHandlerDataSource(att.asDataHandler()));
                this.setter.put(str, args);
            } catch (Exception e2) {
                throw new WebServiceException(e2);
            }
        }
    }

    public static final String getWSDLPartName(Attachment att) {
        String localPart;
        int index;
        String cId = att.getContentId();
        int index2 = cId.lastIndexOf(64, cId.length());
        if (index2 == -1 || (index = (localPart = cId.substring(0, index2)).lastIndexOf(61, localPart.length())) == -1) {
            return null;
        }
        try {
            return URLDecoder.decode(localPart.substring(0, index), "UTF-8");
        } catch (UnsupportedEncodingException e2) {
            throw new WebServiceException(e2);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/EndpointArgumentsBuilder$Header.class */
    public static final class Header extends EndpointArgumentsBuilder {
        private final XMLBridge<?> bridge;
        private final EndpointValueSetter setter;
        private final QName headerName;
        private final SOAPVersion soapVersion;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !EndpointArgumentsBuilder.class.desiredAssertionStatus();
        }

        public Header(SOAPVersion soapVersion, QName name, XMLBridge<?> bridge, EndpointValueSetter setter) {
            this.soapVersion = soapVersion;
            this.headerName = name;
            this.bridge = bridge;
            this.setter = setter;
        }

        public Header(SOAPVersion soapVersion, ParameterImpl param, EndpointValueSetter setter) {
            this(soapVersion, param.getTypeInfo().tagName, param.getXMLBridge(), setter);
            if (!$assertionsDisabled && param.getOutBinding() != ParameterBinding.HEADER) {
                throw new AssertionError();
            }
        }

        private SOAPFaultException createDuplicateHeaderException() {
            try {
                SOAPFault fault = this.soapVersion.getSOAPFactory().createFault();
                fault.setFaultCode(this.soapVersion.faultCodeClient);
                fault.setFaultString(ServerMessages.DUPLICATE_PORT_KNOWN_HEADER(this.headerName));
                return new SOAPFaultException(fault);
            } catch (SOAPException e2) {
                throw new WebServiceException(e2);
            }
        }

        @Override // com.sun.xml.internal.ws.server.sei.EndpointArgumentsBuilder
        public void readRequest(Message msg, Object[] args) throws JAXBException {
            com.sun.xml.internal.ws.api.message.Header header = null;
            Iterator<com.sun.xml.internal.ws.api.message.Header> it = msg.getHeaders().getHeaders(this.headerName, true);
            if (it.hasNext()) {
                header = it.next();
                if (it.hasNext()) {
                    throw createDuplicateHeaderException();
                }
            }
            if (header != null) {
                this.setter.put(header.readAsJAXB(this.bridge), args);
            }
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/EndpointArgumentsBuilder$Body.class */
    public static final class Body extends EndpointArgumentsBuilder {
        private final XMLBridge<?> bridge;
        private final EndpointValueSetter setter;

        public Body(XMLBridge<?> bridge, EndpointValueSetter setter) {
            this.bridge = bridge;
            this.setter = setter;
        }

        @Override // com.sun.xml.internal.ws.server.sei.EndpointArgumentsBuilder
        public void readRequest(Message msg, Object[] args) throws JAXBException {
            this.setter.put(msg.readPayloadAsJAXB(this.bridge), args);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/EndpointArgumentsBuilder$DocLit.class */
    public static final class DocLit extends EndpointArgumentsBuilder {
        private final PartBuilder[] parts;
        private final XMLBridge wrapper;
        private boolean dynamicWrapper;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !EndpointArgumentsBuilder.class.desiredAssertionStatus();
        }

        public DocLit(WrapperParameter wp, WebParam.Mode skipMode) {
            this.wrapperName = wp.getName();
            this.wrapper = wp.getXMLBridge();
            Class wrapperType = (Class) this.wrapper.getTypeInfo().type;
            this.dynamicWrapper = WrapperComposite.class.equals(wrapperType);
            List<PartBuilder> parts = new ArrayList<>();
            List<ParameterImpl> children = wp.getWrapperChildren();
            for (ParameterImpl p2 : children) {
                if (p2.getMode() != skipMode) {
                    QName name = p2.getName();
                    try {
                        if (this.dynamicWrapper) {
                            if (this.wrappedParts == null) {
                                this.wrappedParts = new HashMap();
                            }
                            XMLBridge xmlBridge = p2.getInlinedRepeatedElementBridge();
                            this.wrappedParts.put(p2.getName(), new WrappedPartBuilder(xmlBridge == null ? p2.getXMLBridge() : xmlBridge, EndpointValueSetter.get(p2)));
                        } else {
                            parts.add(new PartBuilder(wp.getOwner().getBindingContext().getElementPropertyAccessor(wrapperType, name.getNamespaceURI(), p2.getName().getLocalPart()), EndpointValueSetter.get(p2)));
                            if (!$assertionsDisabled && p2.getBinding() != ParameterBinding.BODY) {
                                throw new AssertionError();
                            }
                        }
                    } catch (JAXBException e2) {
                        throw new WebServiceException(((Object) wrapperType) + " do not have a property of the name " + ((Object) name), e2);
                    }
                }
            }
            this.parts = (PartBuilder[]) parts.toArray(new PartBuilder[parts.size()]);
        }

        @Override // com.sun.xml.internal.ws.server.sei.EndpointArgumentsBuilder
        public void readRequest(Message msg, Object[] args) throws XMLStreamException, JAXBException {
            if (this.dynamicWrapper) {
                readWrappedRequest(msg, args);
                return;
            }
            if (this.parts.length > 0) {
                if (!msg.hasPayload()) {
                    throw new WebServiceException("No payload. Expecting payload with " + ((Object) this.wrapperName) + " element");
                }
                XMLStreamReader reader = msg.readPayload();
                XMLStreamReaderUtil.verifyTag(reader, this.wrapperName);
                Object wrapperBean = this.wrapper.unmarshal(reader, msg.getAttachments() != null ? new AttachmentUnmarshallerImpl(msg.getAttachments()) : null);
                try {
                    for (PartBuilder part : this.parts) {
                        part.readRequest(args, wrapperBean);
                    }
                    reader.close();
                    XMLStreamReaderFactory.recycle(reader);
                    return;
                } catch (DatabindingException e2) {
                    throw new WebServiceException(e2);
                }
            }
            msg.consume();
        }

        /* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/EndpointArgumentsBuilder$DocLit$PartBuilder.class */
        static final class PartBuilder {
            private final PropertyAccessor accessor;
            private final EndpointValueSetter setter;
            static final /* synthetic */ boolean $assertionsDisabled;

            static {
                $assertionsDisabled = !EndpointArgumentsBuilder.class.desiredAssertionStatus();
            }

            public PartBuilder(PropertyAccessor accessor, EndpointValueSetter setter) {
                this.accessor = accessor;
                this.setter = setter;
                if ($assertionsDisabled) {
                    return;
                }
                if (accessor == null || setter == null) {
                    throw new AssertionError();
                }
            }

            final void readRequest(Object[] args, Object wrapperBean) throws DatabindingException {
                Object obj = this.accessor.get(wrapperBean);
                this.setter.put(obj, args);
            }
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/EndpointArgumentsBuilder$RpcLit.class */
    public static final class RpcLit extends EndpointArgumentsBuilder {
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !EndpointArgumentsBuilder.class.desiredAssertionStatus();
        }

        public RpcLit(WrapperParameter wp) {
            if (!$assertionsDisabled && wp.getTypeInfo().type != WrapperComposite.class) {
                throw new AssertionError();
            }
            this.wrapperName = wp.getName();
            this.wrappedParts = new HashMap();
            List<ParameterImpl> children = wp.getWrapperChildren();
            for (ParameterImpl p2 : children) {
                this.wrappedParts.put(p2.getName(), new WrappedPartBuilder(p2.getXMLBridge(), EndpointValueSetter.get(p2)));
                if (!$assertionsDisabled && p2.getBinding() != ParameterBinding.BODY) {
                    throw new AssertionError();
                }
            }
        }

        @Override // com.sun.xml.internal.ws.server.sei.EndpointArgumentsBuilder
        public void readRequest(Message msg, Object[] args) throws XMLStreamException, JAXBException {
            readWrappedRequest(msg, args);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isXMLMimeType(String mimeType) {
        return mimeType.equals("text/xml") || mimeType.equals(XMLCodec.XML_APPLICATION_MIME_TYPE);
    }
}
