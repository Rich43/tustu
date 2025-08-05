package com.sun.xml.internal.ws.client.sei;

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
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPFaultException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/ResponseBuilder.class */
public abstract class ResponseBuilder {
    protected Map<QName, WrappedPartBuilder> wrappedParts = null;
    protected QName wrapperName;
    public static final ResponseBuilder NONE;
    private static final Map<Class, Object> primitiveUninitializedValues;
    static final /* synthetic */ boolean $assertionsDisabled;

    public abstract Object readResponse(Message message, Object[] objArr) throws XMLStreamException, JAXBException;

    static {
        $assertionsDisabled = !ResponseBuilder.class.desiredAssertionStatus();
        NONE = new None();
        primitiveUninitializedValues = new HashMap();
        Map<Class, Object> m2 = primitiveUninitializedValues;
        m2.put(Integer.TYPE, 0);
        m2.put(Character.TYPE, (char) 0);
        m2.put(Byte.TYPE, (byte) 0);
        m2.put(Short.TYPE, (short) 0);
        m2.put(Long.TYPE, 0L);
        m2.put(Float.TYPE, Float.valueOf(0.0f));
        m2.put(Double.TYPE, Double.valueOf(0.0d));
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/ResponseBuilder$WrappedPartBuilder.class */
    static final class WrappedPartBuilder {
        private final XMLBridge bridge;
        private final ValueSetter setter;

        public WrappedPartBuilder(XMLBridge bridge, ValueSetter setter) {
            this.bridge = bridge;
            this.setter = setter;
        }

        final Object readResponse(Object[] args, XMLStreamReader r2, AttachmentSet att) throws JAXBException {
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
            return this.setter.put(obj, args);
        }
    }

    protected Object readWrappedResponse(Message msg, Object[] args) throws XMLStreamException, JAXBException {
        Object retVal = null;
        if (!msg.hasPayload()) {
            throw new WebServiceException("No payload. Expecting payload with " + ((Object) this.wrapperName) + " element");
        }
        XMLStreamReader reader = msg.readPayload();
        XMLStreamReaderUtil.verifyTag(reader, this.wrapperName);
        reader.nextTag();
        while (reader.getEventType() == 1) {
            WrappedPartBuilder part = this.wrappedParts.get(reader.getName());
            if (part == null) {
                XMLStreamReaderUtil.skipElement(reader);
                reader.nextTag();
            } else {
                Object o2 = part.readResponse(args, reader, msg.getAttachments());
                if (o2 != null) {
                    if (!$assertionsDisabled && retVal != null) {
                        throw new AssertionError();
                    }
                    retVal = o2;
                }
            }
            if (reader.getEventType() != 1 && reader.getEventType() != 2) {
                XMLStreamReaderUtil.nextElementContent(reader);
            }
        }
        reader.close();
        XMLStreamReaderFactory.recycle(reader);
        return retVal;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/ResponseBuilder$None.class */
    static final class None extends ResponseBuilder {
        private None() {
        }

        @Override // com.sun.xml.internal.ws.client.sei.ResponseBuilder
        public Object readResponse(Message msg, Object[] args) {
            msg.consume();
            return null;
        }
    }

    public static Object getVMUninitializedValue(Type type) {
        return primitiveUninitializedValues.get(type);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/ResponseBuilder$NullSetter.class */
    public static final class NullSetter extends ResponseBuilder {
        private final ValueSetter setter;
        private final Object nullValue;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ResponseBuilder.class.desiredAssertionStatus();
        }

        public NullSetter(ValueSetter setter, Object nullValue) {
            if (!$assertionsDisabled && setter == null) {
                throw new AssertionError();
            }
            this.nullValue = nullValue;
            this.setter = setter;
        }

        @Override // com.sun.xml.internal.ws.client.sei.ResponseBuilder
        public Object readResponse(Message msg, Object[] args) {
            return this.setter.put(this.nullValue, args);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/ResponseBuilder$Composite.class */
    public static final class Composite extends ResponseBuilder {
        private final ResponseBuilder[] builders;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ResponseBuilder.class.desiredAssertionStatus();
        }

        public Composite(ResponseBuilder... builders) {
            this.builders = builders;
        }

        public Composite(Collection<? extends ResponseBuilder> builders) {
            this((ResponseBuilder[]) builders.toArray(new ResponseBuilder[builders.size()]));
        }

        @Override // com.sun.xml.internal.ws.client.sei.ResponseBuilder
        public Object readResponse(Message msg, Object[] args) throws XMLStreamException, JAXBException {
            Object retVal = null;
            for (ResponseBuilder builder : this.builders) {
                Object r2 = builder.readResponse(msg, args);
                if (r2 != null) {
                    if (!$assertionsDisabled && retVal != null) {
                        throw new AssertionError();
                    }
                    retVal = r2;
                }
            }
            return retVal;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/ResponseBuilder$AttachmentBuilder.class */
    public static abstract class AttachmentBuilder extends ResponseBuilder {
        protected final ValueSetter setter;
        protected final ParameterImpl param;
        private final String pname;
        private final String pname1;

        abstract Object mapAttachment(Attachment attachment, Object[] objArr) throws JAXBException;

        AttachmentBuilder(ParameterImpl param, ValueSetter setter) {
            this.setter = setter;
            this.param = param;
            this.pname = param.getPartName();
            this.pname1 = "<" + this.pname;
        }

        public static ResponseBuilder createAttachmentBuilder(ParameterImpl param, ValueSetter setter) {
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
                if (ResponseBuilder.isXMLMimeType(param.getBinding().getMimeType())) {
                    return new JAXBBuilder(param, setter);
                }
                if (String.class.isAssignableFrom(type)) {
                    return new StringBuilder(param, setter);
                }
                throw new UnsupportedOperationException("Unexpected Attachment type =" + ((Object) type));
            }
            return new InputStreamBuilder(param, setter);
        }

        @Override // com.sun.xml.internal.ws.client.sei.ResponseBuilder
        public Object readResponse(Message msg, Object[] args) throws XMLStreamException, JAXBException {
            for (Attachment att : msg.getAttachments()) {
                String part = getWSDLPartName(att);
                if (part != null && (part.equals(this.pname) || part.equals(this.pname1))) {
                    return mapAttachment(att, args);
                }
            }
            return null;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/ResponseBuilder$DataHandlerBuilder.class */
    private static final class DataHandlerBuilder extends AttachmentBuilder {
        DataHandlerBuilder(ParameterImpl param, ValueSetter setter) {
            super(param, setter);
        }

        @Override // com.sun.xml.internal.ws.client.sei.ResponseBuilder.AttachmentBuilder
        Object mapAttachment(Attachment att, Object[] args) {
            return this.setter.put(att.asDataHandler(), args);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/ResponseBuilder$StringBuilder.class */
    private static final class StringBuilder extends AttachmentBuilder {
        StringBuilder(ParameterImpl param, ValueSetter setter) {
            super(param, setter);
        }

        @Override // com.sun.xml.internal.ws.client.sei.ResponseBuilder.AttachmentBuilder
        Object mapAttachment(Attachment att, Object[] args) {
            att.getContentType();
            StringDataContentHandler sdh = new StringDataContentHandler();
            try {
                String str = (String) sdh.getContent(new DataHandlerDataSource(att.asDataHandler()));
                return this.setter.put(str, args);
            } catch (Exception e2) {
                throw new WebServiceException(e2);
            }
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/ResponseBuilder$ByteArrayBuilder.class */
    private static final class ByteArrayBuilder extends AttachmentBuilder {
        ByteArrayBuilder(ParameterImpl param, ValueSetter setter) {
            super(param, setter);
        }

        @Override // com.sun.xml.internal.ws.client.sei.ResponseBuilder.AttachmentBuilder
        Object mapAttachment(Attachment att, Object[] args) {
            return this.setter.put(att.asByteArray(), args);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/ResponseBuilder$SourceBuilder.class */
    private static final class SourceBuilder extends AttachmentBuilder {
        SourceBuilder(ParameterImpl param, ValueSetter setter) {
            super(param, setter);
        }

        @Override // com.sun.xml.internal.ws.client.sei.ResponseBuilder.AttachmentBuilder
        Object mapAttachment(Attachment att, Object[] args) {
            return this.setter.put(att.asSource(), args);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/ResponseBuilder$ImageBuilder.class */
    private static final class ImageBuilder extends AttachmentBuilder {
        ImageBuilder(ParameterImpl param, ValueSetter setter) {
            super(param, setter);
        }

        @Override // com.sun.xml.internal.ws.client.sei.ResponseBuilder.AttachmentBuilder
        Object mapAttachment(Attachment att, Object[] args) {
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
                    return this.setter.put(image, args);
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

    /* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/ResponseBuilder$InputStreamBuilder.class */
    private static final class InputStreamBuilder extends AttachmentBuilder {
        InputStreamBuilder(ParameterImpl param, ValueSetter setter) {
            super(param, setter);
        }

        @Override // com.sun.xml.internal.ws.client.sei.ResponseBuilder.AttachmentBuilder
        Object mapAttachment(Attachment att, Object[] args) {
            return this.setter.put(att.asInputStream(), args);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/ResponseBuilder$JAXBBuilder.class */
    private static final class JAXBBuilder extends AttachmentBuilder {
        JAXBBuilder(ParameterImpl param, ValueSetter setter) {
            super(param, setter);
        }

        @Override // com.sun.xml.internal.ws.client.sei.ResponseBuilder.AttachmentBuilder
        Object mapAttachment(Attachment att, Object[] args) throws JAXBException {
            Object obj = this.param.getXMLBridge().unmarshal(att.asInputStream());
            return this.setter.put(obj, args);
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

    /* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/ResponseBuilder$Header.class */
    public static final class Header extends ResponseBuilder {
        private final XMLBridge<?> bridge;
        private final ValueSetter setter;
        private final QName headerName;
        private final SOAPVersion soapVersion;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ResponseBuilder.class.desiredAssertionStatus();
        }

        public Header(SOAPVersion soapVersion, QName name, XMLBridge<?> bridge, ValueSetter setter) {
            this.soapVersion = soapVersion;
            this.headerName = name;
            this.bridge = bridge;
            this.setter = setter;
        }

        public Header(SOAPVersion soapVersion, ParameterImpl param, ValueSetter setter) {
            this(soapVersion, param.getTypeInfo().tagName, param.getXMLBridge(), setter);
            if (!$assertionsDisabled && param.getOutBinding() != ParameterBinding.HEADER) {
                throw new AssertionError();
            }
        }

        private SOAPFaultException createDuplicateHeaderException() {
            try {
                SOAPFault fault = this.soapVersion.getSOAPFactory().createFault();
                fault.setFaultCode(this.soapVersion.faultCodeServer);
                fault.setFaultString(ServerMessages.DUPLICATE_PORT_KNOWN_HEADER(this.headerName));
                return new SOAPFaultException(fault);
            } catch (SOAPException e2) {
                throw new WebServiceException(e2);
            }
        }

        @Override // com.sun.xml.internal.ws.client.sei.ResponseBuilder
        public Object readResponse(Message msg, Object[] args) throws JAXBException {
            com.sun.xml.internal.ws.api.message.Header header = null;
            Iterator<com.sun.xml.internal.ws.api.message.Header> it = msg.getHeaders().getHeaders(this.headerName, true);
            if (it.hasNext()) {
                header = it.next();
                if (it.hasNext()) {
                    throw createDuplicateHeaderException();
                }
            }
            if (header != null) {
                return this.setter.put(header.readAsJAXB(this.bridge), args);
            }
            return null;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/ResponseBuilder$Body.class */
    public static final class Body extends ResponseBuilder {
        private final XMLBridge<?> bridge;
        private final ValueSetter setter;

        public Body(XMLBridge<?> bridge, ValueSetter setter) {
            this.bridge = bridge;
            this.setter = setter;
        }

        @Override // com.sun.xml.internal.ws.client.sei.ResponseBuilder
        public Object readResponse(Message msg, Object[] args) throws JAXBException {
            return this.setter.put(msg.readPayloadAsJAXB(this.bridge), args);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/ResponseBuilder$DocLit.class */
    public static final class DocLit extends ResponseBuilder {
        private final PartBuilder[] parts;
        private final XMLBridge wrapper;
        private boolean dynamicWrapper;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ResponseBuilder.class.desiredAssertionStatus();
        }

        public DocLit(WrapperParameter wp, ValueSetterFactory setterFactory) {
            this.wrapperName = wp.getName();
            this.wrapper = wp.getXMLBridge();
            Class wrapperType = (Class) this.wrapper.getTypeInfo().type;
            this.dynamicWrapper = WrapperComposite.class.equals(wrapperType);
            List<PartBuilder> tempParts = new ArrayList<>();
            List<ParameterImpl> children = wp.getWrapperChildren();
            for (ParameterImpl p2 : children) {
                if (!p2.isIN()) {
                    QName name = p2.getName();
                    if (this.dynamicWrapper) {
                        if (this.wrappedParts == null) {
                            this.wrappedParts = new HashMap();
                        }
                        XMLBridge xmlBridge = p2.getInlinedRepeatedElementBridge();
                        this.wrappedParts.put(p2.getName(), new WrappedPartBuilder(xmlBridge == null ? p2.getXMLBridge() : xmlBridge, setterFactory.get(p2)));
                    } else {
                        try {
                            tempParts.add(new PartBuilder(wp.getOwner().getBindingContext().getElementPropertyAccessor(wrapperType, name.getNamespaceURI(), p2.getName().getLocalPart()), setterFactory.get(p2)));
                            if (!$assertionsDisabled && p2.getBinding() != ParameterBinding.BODY) {
                                throw new AssertionError();
                            }
                        } catch (JAXBException e2) {
                            throw new WebServiceException(((Object) wrapperType) + " do not have a property of the name " + ((Object) name), e2);
                        }
                    }
                }
            }
            this.parts = (PartBuilder[]) tempParts.toArray(new PartBuilder[tempParts.size()]);
        }

        @Override // com.sun.xml.internal.ws.client.sei.ResponseBuilder
        public Object readResponse(Message msg, Object[] args) throws XMLStreamException, JAXBException {
            if (this.dynamicWrapper) {
                return readWrappedResponse(msg, args);
            }
            Object retVal = null;
            if (this.parts.length > 0) {
                if (!msg.hasPayload()) {
                    throw new WebServiceException("No payload. Expecting payload with " + ((Object) this.wrapperName) + " element");
                }
                XMLStreamReader reader = msg.readPayload();
                XMLStreamReaderUtil.verifyTag(reader, this.wrapperName);
                Object wrapperBean = this.wrapper.unmarshal(reader, msg.getAttachments() != null ? new AttachmentUnmarshallerImpl(msg.getAttachments()) : null);
                try {
                    for (PartBuilder part : this.parts) {
                        Object o2 = part.readResponse(args, wrapperBean);
                        if (o2 != null) {
                            if (!$assertionsDisabled && retVal != null) {
                                throw new AssertionError();
                            }
                            retVal = o2;
                        }
                    }
                    reader.close();
                    XMLStreamReaderFactory.recycle(reader);
                } catch (DatabindingException e2) {
                    throw new WebServiceException(e2);
                }
            } else {
                msg.consume();
            }
            return retVal;
        }

        /* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/ResponseBuilder$DocLit$PartBuilder.class */
        static final class PartBuilder {
            private final PropertyAccessor accessor;
            private final ValueSetter setter;
            static final /* synthetic */ boolean $assertionsDisabled;

            static {
                $assertionsDisabled = !ResponseBuilder.class.desiredAssertionStatus();
            }

            public PartBuilder(PropertyAccessor accessor, ValueSetter setter) {
                this.accessor = accessor;
                this.setter = setter;
                if ($assertionsDisabled) {
                    return;
                }
                if (accessor == null || setter == null) {
                    throw new AssertionError();
                }
            }

            final Object readResponse(Object[] args, Object wrapperBean) throws DatabindingException {
                Object obj = this.accessor.get(wrapperBean);
                return this.setter.put(obj, args);
            }
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/ResponseBuilder$RpcLit.class */
    public static final class RpcLit extends ResponseBuilder {
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ResponseBuilder.class.desiredAssertionStatus();
        }

        public RpcLit(WrapperParameter wp, ValueSetterFactory setterFactory) {
            if (!$assertionsDisabled && wp.getTypeInfo().type != WrapperComposite.class) {
                throw new AssertionError();
            }
            this.wrapperName = wp.getName();
            this.wrappedParts = new HashMap();
            List<ParameterImpl> children = wp.getWrapperChildren();
            for (ParameterImpl p2 : children) {
                this.wrappedParts.put(p2.getName(), new WrappedPartBuilder(p2.getXMLBridge(), setterFactory.get(p2)));
                if (!$assertionsDisabled && p2.getBinding() != ParameterBinding.BODY) {
                    throw new AssertionError();
                }
            }
        }

        @Override // com.sun.xml.internal.ws.client.sei.ResponseBuilder
        public Object readResponse(Message msg, Object[] args) throws XMLStreamException, JAXBException {
            return readWrappedResponse(msg, args);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isXMLMimeType(String mimeType) {
        return mimeType.equals("text/xml") || mimeType.equals(XMLCodec.XML_APPLICATION_MIME_TYPE);
    }
}
