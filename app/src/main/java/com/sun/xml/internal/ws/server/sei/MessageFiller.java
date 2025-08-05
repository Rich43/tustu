package com.sun.xml.internal.ws.server.sei;

import com.sun.xml.internal.ws.api.message.Attachment;
import com.sun.xml.internal.ws.api.message.Headers;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.encoding.xml.XMLCodec;
import com.sun.xml.internal.ws.message.ByteArrayAttachment;
import com.sun.xml.internal.ws.message.DataHandlerAttachment;
import com.sun.xml.internal.ws.message.JAXBAttachment;
import com.sun.xml.internal.ws.model.ParameterImpl;
import com.sun.xml.internal.ws.spi.db.XMLBridge;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;
import javax.activation.DataHandler;
import javax.xml.transform.Source;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/MessageFiller.class */
public abstract class MessageFiller {
    protected final int methodPos;

    public abstract void fillIn(Object[] objArr, Object obj, Message message);

    protected MessageFiller(int methodPos) {
        this.methodPos = methodPos;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/MessageFiller$AttachmentFiller.class */
    public static abstract class AttachmentFiller extends MessageFiller {
        protected final ParameterImpl param;
        protected final ValueGetter getter;
        protected final String mimeType;
        private final String contentIdPart;

        protected AttachmentFiller(ParameterImpl param, ValueGetter getter) {
            super(param.getIndex());
            this.param = param;
            this.getter = getter;
            this.mimeType = param.getBinding().getMimeType();
            try {
                this.contentIdPart = URLEncoder.encode(param.getPartName(), "UTF-8") + '=';
            } catch (UnsupportedEncodingException e2) {
                throw new WebServiceException(e2);
            }
        }

        public static MessageFiller createAttachmentFiller(ParameterImpl param, ValueGetter getter) {
            Class type = (Class) param.getTypeInfo().type;
            if (DataHandler.class.isAssignableFrom(type) || Source.class.isAssignableFrom(type)) {
                return new DataHandlerFiller(param, getter);
            }
            if (byte[].class != type) {
                if (MessageFiller.isXMLMimeType(param.getBinding().getMimeType())) {
                    return new JAXBFiller(param, getter);
                }
                return new DataHandlerFiller(param, getter);
            }
            return new ByteArrayFiller(param, getter);
        }

        String getContentId() {
            return this.contentIdPart + ((Object) UUID.randomUUID()) + "@jaxws.sun.com";
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/MessageFiller$ByteArrayFiller.class */
    private static class ByteArrayFiller extends AttachmentFiller {
        protected ByteArrayFiller(ParameterImpl param, ValueGetter getter) {
            super(param, getter);
        }

        @Override // com.sun.xml.internal.ws.server.sei.MessageFiller
        public void fillIn(Object[] methodArgs, Object returnValue, Message msg) {
            String contentId = getContentId();
            Object obj = this.methodPos == -1 ? returnValue : this.getter.get(methodArgs[this.methodPos]);
            if (obj != null) {
                Attachment att = new ByteArrayAttachment(contentId, (byte[]) obj, this.mimeType);
                msg.getAttachments().add(att);
            }
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/MessageFiller$DataHandlerFiller.class */
    private static class DataHandlerFiller extends AttachmentFiller {
        protected DataHandlerFiller(ParameterImpl param, ValueGetter getter) {
            super(param, getter);
        }

        @Override // com.sun.xml.internal.ws.server.sei.MessageFiller
        public void fillIn(Object[] methodArgs, Object returnValue, Message msg) {
            String contentId = getContentId();
            Object obj = this.methodPos == -1 ? returnValue : this.getter.get(methodArgs[this.methodPos]);
            DataHandler dh = obj instanceof DataHandler ? (DataHandler) obj : new DataHandler(obj, this.mimeType);
            Attachment att = new DataHandlerAttachment(contentId, dh);
            msg.getAttachments().add(att);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/MessageFiller$JAXBFiller.class */
    private static class JAXBFiller extends AttachmentFiller {
        protected JAXBFiller(ParameterImpl param, ValueGetter getter) {
            super(param, getter);
        }

        @Override // com.sun.xml.internal.ws.server.sei.MessageFiller
        public void fillIn(Object[] methodArgs, Object returnValue, Message msg) {
            String contentId = getContentId();
            Object obj = this.methodPos == -1 ? returnValue : this.getter.get(methodArgs[this.methodPos]);
            Attachment att = new JAXBAttachment(contentId, obj, this.param.getXMLBridge(), this.mimeType);
            msg.getAttachments().add(att);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/MessageFiller$Header.class */
    public static final class Header extends MessageFiller {
        private final XMLBridge bridge;
        private final ValueGetter getter;

        public Header(int methodPos, XMLBridge bridge, ValueGetter getter) {
            super(methodPos);
            this.bridge = bridge;
            this.getter = getter;
        }

        @Override // com.sun.xml.internal.ws.server.sei.MessageFiller
        public void fillIn(Object[] methodArgs, Object returnValue, Message msg) {
            Object value = this.methodPos == -1 ? returnValue : this.getter.get(methodArgs[this.methodPos]);
            msg.getHeaders().add(Headers.create(this.bridge, value));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isXMLMimeType(String mimeType) {
        return mimeType.equals("text/xml") || mimeType.equals(XMLCodec.XML_APPLICATION_MIME_TYPE);
    }
}
