package com.sun.xml.internal.ws.handler;

import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.message.AttachmentSet;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.MessageHeaders;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.message.DOMMessage;
import com.sun.xml.internal.ws.message.EmptyMessageImpl;
import com.sun.xml.internal.ws.message.jaxb.JAXBMessage;
import com.sun.xml.internal.ws.message.source.PayloadSourceMessage;
import com.sun.xml.internal.ws.spi.db.BindingContext;
import com.sun.xml.internal.ws.spi.db.BindingContextFactory;
import com.sun.xml.internal.ws.util.xml.XmlUtil;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.ws.LogicalMessage;
import javax.xml.ws.WebServiceException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/xml/internal/ws/handler/LogicalMessageImpl.class */
class LogicalMessageImpl implements LogicalMessage {
    private Packet packet;
    protected BindingContext defaultJaxbContext;
    private ImmutableLM lm = null;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !LogicalMessageImpl.class.desiredAssertionStatus();
    }

    public LogicalMessageImpl(BindingContext defaultJaxbContext, Packet packet) {
        this.packet = packet;
        this.defaultJaxbContext = defaultJaxbContext;
    }

    @Override // javax.xml.ws.LogicalMessage
    public Source getPayload() {
        if (this.lm == null) {
            Source payload = this.packet.getMessage().copy().readPayloadAsSource();
            if (payload instanceof DOMSource) {
                this.lm = createLogicalMessageImpl(payload);
            }
            return payload;
        }
        return this.lm.getPayload();
    }

    @Override // javax.xml.ws.LogicalMessage
    public void setPayload(Source payload) {
        this.lm = createLogicalMessageImpl(payload);
    }

    private ImmutableLM createLogicalMessageImpl(Source payload) {
        if (payload == null) {
            this.lm = new EmptyLogicalMessageImpl();
        } else if (payload instanceof DOMSource) {
            this.lm = new DOMLogicalMessageImpl((DOMSource) payload);
        } else {
            this.lm = new SourceLogicalMessageImpl(payload);
        }
        return this.lm;
    }

    public Object getPayload(BindingContext context) {
        Object o2;
        if (context == null) {
            context = this.defaultJaxbContext;
        }
        if (context == null) {
            throw new WebServiceException("JAXBContext parameter cannot be null");
        }
        if (this.lm == null) {
            try {
                o2 = this.packet.getMessage().copy().readPayloadAsJAXB(context.createUnmarshaller());
            } catch (JAXBException e2) {
                throw new WebServiceException(e2);
            }
        } else {
            o2 = this.lm.getPayload(context);
            this.lm = new JAXBLogicalMessageImpl(context.getJAXBContext(), o2);
        }
        return o2;
    }

    @Override // javax.xml.ws.LogicalMessage
    public Object getPayload(JAXBContext context) {
        Object o2;
        if (context == null) {
            return getPayload(this.defaultJaxbContext);
        }
        if (context == null) {
            throw new WebServiceException("JAXBContext parameter cannot be null");
        }
        if (this.lm == null) {
            try {
                o2 = this.packet.getMessage().copy().readPayloadAsJAXB(context.createUnmarshaller());
            } catch (JAXBException e2) {
                throw new WebServiceException(e2);
            }
        } else {
            o2 = this.lm.getPayload(context);
            this.lm = new JAXBLogicalMessageImpl(context, o2);
        }
        return o2;
    }

    public void setPayload(Object payload, BindingContext context) {
        if (context == null) {
            context = this.defaultJaxbContext;
        }
        if (payload == null) {
            this.lm = new EmptyLogicalMessageImpl();
        } else {
            this.lm = new JAXBLogicalMessageImpl(context.getJAXBContext(), payload);
        }
    }

    @Override // javax.xml.ws.LogicalMessage
    public void setPayload(Object payload, JAXBContext context) {
        if (context == null) {
            setPayload(payload, this.defaultJaxbContext);
        }
        if (payload == null) {
            this.lm = new EmptyLogicalMessageImpl();
        } else {
            this.lm = new JAXBLogicalMessageImpl(context, payload);
        }
    }

    public boolean isPayloadModifed() {
        return this.lm != null;
    }

    public Message getMessage(MessageHeaders headers, AttachmentSet attachments, WSBinding binding) {
        if (!$assertionsDisabled && !isPayloadModifed()) {
            throw new AssertionError();
        }
        if (isPayloadModifed()) {
            return this.lm.getMessage(headers, attachments, binding);
        }
        return this.packet.getMessage();
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/handler/LogicalMessageImpl$ImmutableLM.class */
    private abstract class ImmutableLM {
        public abstract Source getPayload();

        public abstract Object getPayload(BindingContext bindingContext);

        public abstract Object getPayload(JAXBContext jAXBContext);

        public abstract Message getMessage(MessageHeaders messageHeaders, AttachmentSet attachmentSet, WSBinding wSBinding);

        private ImmutableLM() {
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/handler/LogicalMessageImpl$DOMLogicalMessageImpl.class */
    private class DOMLogicalMessageImpl extends SourceLogicalMessageImpl {
        private DOMSource dom;

        public DOMLogicalMessageImpl(DOMSource dom) {
            super(dom);
            this.dom = dom;
        }

        @Override // com.sun.xml.internal.ws.handler.LogicalMessageImpl.SourceLogicalMessageImpl, com.sun.xml.internal.ws.handler.LogicalMessageImpl.ImmutableLM
        public Source getPayload() {
            return this.dom;
        }

        @Override // com.sun.xml.internal.ws.handler.LogicalMessageImpl.SourceLogicalMessageImpl, com.sun.xml.internal.ws.handler.LogicalMessageImpl.ImmutableLM
        public Message getMessage(MessageHeaders headers, AttachmentSet attachments, WSBinding binding) {
            Node n2 = this.dom.getNode();
            if (n2.getNodeType() == 9) {
                n2 = ((Document) n2).getDocumentElement();
            }
            return new DOMMessage(binding.getSOAPVersion(), headers, (Element) n2, attachments);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/handler/LogicalMessageImpl$EmptyLogicalMessageImpl.class */
    private class EmptyLogicalMessageImpl extends ImmutableLM {
        public EmptyLogicalMessageImpl() {
            super();
        }

        @Override // com.sun.xml.internal.ws.handler.LogicalMessageImpl.ImmutableLM
        public Source getPayload() {
            return null;
        }

        @Override // com.sun.xml.internal.ws.handler.LogicalMessageImpl.ImmutableLM
        public Object getPayload(JAXBContext context) {
            return null;
        }

        @Override // com.sun.xml.internal.ws.handler.LogicalMessageImpl.ImmutableLM
        public Object getPayload(BindingContext context) {
            return null;
        }

        @Override // com.sun.xml.internal.ws.handler.LogicalMessageImpl.ImmutableLM
        public Message getMessage(MessageHeaders headers, AttachmentSet attachments, WSBinding binding) {
            return new EmptyMessageImpl(headers, attachments, binding.getSOAPVersion());
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/handler/LogicalMessageImpl$JAXBLogicalMessageImpl.class */
    private class JAXBLogicalMessageImpl extends ImmutableLM {
        private JAXBContext ctxt;

        /* renamed from: o, reason: collision with root package name */
        private Object f12085o;

        public JAXBLogicalMessageImpl(JAXBContext ctxt, Object o2) {
            super();
            this.ctxt = ctxt;
            this.f12085o = o2;
        }

        @Override // com.sun.xml.internal.ws.handler.LogicalMessageImpl.ImmutableLM
        public Source getPayload() {
            JAXBContext context = this.ctxt;
            if (context == null) {
                context = LogicalMessageImpl.this.defaultJaxbContext.getJAXBContext();
            }
            try {
                return new JAXBSource(context, this.f12085o);
            } catch (JAXBException e2) {
                throw new WebServiceException(e2);
            }
        }

        @Override // com.sun.xml.internal.ws.handler.LogicalMessageImpl.ImmutableLM
        public Object getPayload(JAXBContext context) {
            try {
                Source payloadSrc = getPayload();
                if (payloadSrc == null) {
                    return null;
                }
                Unmarshaller unmarshaller = context.createUnmarshaller();
                return unmarshaller.unmarshal(payloadSrc);
            } catch (JAXBException e2) {
                throw new WebServiceException(e2);
            }
        }

        @Override // com.sun.xml.internal.ws.handler.LogicalMessageImpl.ImmutableLM
        public Object getPayload(BindingContext context) {
            try {
                Source payloadSrc = getPayload();
                if (payloadSrc == null) {
                    return null;
                }
                Unmarshaller unmarshaller = context.createUnmarshaller();
                return unmarshaller.unmarshal(payloadSrc);
            } catch (JAXBException e2) {
                throw new WebServiceException(e2);
            }
        }

        @Override // com.sun.xml.internal.ws.handler.LogicalMessageImpl.ImmutableLM
        public Message getMessage(MessageHeaders headers, AttachmentSet attachments, WSBinding binding) {
            return JAXBMessage.create(BindingContextFactory.create(this.ctxt), this.f12085o, binding.getSOAPVersion(), headers, attachments);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/handler/LogicalMessageImpl$SourceLogicalMessageImpl.class */
    private class SourceLogicalMessageImpl extends ImmutableLM {
        private Source payloadSrc;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !LogicalMessageImpl.class.desiredAssertionStatus();
        }

        public SourceLogicalMessageImpl(Source source) {
            super();
            this.payloadSrc = source;
        }

        @Override // com.sun.xml.internal.ws.handler.LogicalMessageImpl.ImmutableLM
        public Source getPayload() {
            if (!$assertionsDisabled && (this.payloadSrc instanceof DOMSource)) {
                throw new AssertionError();
            }
            try {
                Transformer transformer = XmlUtil.newTransformer();
                DOMResult domResult = new DOMResult();
                transformer.transform(this.payloadSrc, domResult);
                DOMSource dom = new DOMSource(domResult.getNode());
                LogicalMessageImpl.this.lm = LogicalMessageImpl.this.new DOMLogicalMessageImpl(dom);
                this.payloadSrc = null;
                return dom;
            } catch (TransformerException te) {
                throw new WebServiceException(te);
            }
        }

        @Override // com.sun.xml.internal.ws.handler.LogicalMessageImpl.ImmutableLM
        public Object getPayload(JAXBContext context) {
            try {
                Source payloadSrc = getPayload();
                if (payloadSrc == null) {
                    return null;
                }
                Unmarshaller unmarshaller = context.createUnmarshaller();
                return unmarshaller.unmarshal(payloadSrc);
            } catch (JAXBException e2) {
                throw new WebServiceException(e2);
            }
        }

        @Override // com.sun.xml.internal.ws.handler.LogicalMessageImpl.ImmutableLM
        public Object getPayload(BindingContext context) {
            try {
                Source payloadSrc = getPayload();
                if (payloadSrc == null) {
                    return null;
                }
                Unmarshaller unmarshaller = context.createUnmarshaller();
                return unmarshaller.unmarshal(payloadSrc);
            } catch (JAXBException e2) {
                throw new WebServiceException(e2);
            }
        }

        @Override // com.sun.xml.internal.ws.handler.LogicalMessageImpl.ImmutableLM
        public Message getMessage(MessageHeaders headers, AttachmentSet attachments, WSBinding binding) {
            if ($assertionsDisabled || this.payloadSrc != null) {
                return new PayloadSourceMessage(headers, this.payloadSrc, attachments, binding.getSOAPVersion());
            }
            throw new AssertionError();
        }
    }
}
