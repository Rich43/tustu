package com.sun.xml.internal.ws.handler;

import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.message.Attachment;
import com.sun.xml.internal.ws.api.message.AttachmentSet;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.api.pipe.TubeCloner;
import com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl;
import com.sun.xml.internal.ws.binding.BindingImpl;
import com.sun.xml.internal.ws.client.HandlerConfiguration;
import com.sun.xml.internal.ws.handler.HandlerProcessor;
import com.sun.xml.internal.ws.message.DataHandlerAttachment;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.activation.DataHandler;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;

/* loaded from: rt.jar:com/sun/xml/internal/ws/handler/ClientSOAPHandlerTube.class */
public class ClientSOAPHandlerTube extends HandlerTube {
    private Set<String> roles;

    public ClientSOAPHandlerTube(WSBinding binding, WSDLPort port, Tube next) {
        super(next, port, binding);
        if (binding.getSOAPVersion() != null) {
        }
    }

    public ClientSOAPHandlerTube(WSBinding binding, Tube next, HandlerTube cousinTube) {
        super(next, cousinTube, binding);
    }

    private ClientSOAPHandlerTube(ClientSOAPHandlerTube that, TubeCloner cloner) {
        super(that, cloner);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
    public AbstractFilterTubeImpl copy(TubeCloner cloner) {
        return new ClientSOAPHandlerTube(this, cloner);
    }

    @Override // com.sun.xml.internal.ws.handler.HandlerTube
    void setUpProcessor() {
        if (this.handlers == null) {
            this.handlers = new ArrayList();
            HandlerConfiguration handlerConfig = ((BindingImpl) getBinding()).getHandlerConfig();
            List<SOAPHandler> soapSnapShot = handlerConfig.getSoapHandlers();
            if (!soapSnapShot.isEmpty()) {
                this.handlers.addAll(soapSnapShot);
                this.roles = new HashSet();
                this.roles.addAll(handlerConfig.getRoles());
                this.processor = new SOAPHandlerProcessor(true, this, getBinding(), this.handlers);
            }
        }
    }

    @Override // com.sun.xml.internal.ws.handler.HandlerTube
    MessageUpdatableContext getContext(Packet packet) {
        SOAPMessageContextImpl context = new SOAPMessageContextImpl(getBinding(), packet, this.roles);
        return context;
    }

    @Override // com.sun.xml.internal.ws.handler.HandlerTube
    boolean callHandlersOnRequest(MessageUpdatableContext context, boolean isOneWay) {
        Map<String, DataHandler> atts = (Map) context.get(MessageContext.OUTBOUND_MESSAGE_ATTACHMENTS);
        AttachmentSet attSet = context.packet.getMessage().getAttachments();
        for (Map.Entry<String, DataHandler> entry : atts.entrySet()) {
            String cid = entry.getKey();
            if (attSet.get(cid) == null) {
                Attachment att = new DataHandlerAttachment(cid, atts.get(cid));
                attSet.add(att);
            }
        }
        try {
            boolean handlerResult = this.processor.callHandlersRequest(HandlerProcessor.Direction.OUTBOUND, context, !isOneWay);
            if (!handlerResult) {
                this.remedyActionTaken = true;
            }
            return handlerResult;
        } catch (WebServiceException wse) {
            this.remedyActionTaken = true;
            throw wse;
        } catch (RuntimeException re) {
            this.remedyActionTaken = true;
            throw new WebServiceException(re);
        }
    }

    @Override // com.sun.xml.internal.ws.handler.HandlerTube
    void callHandlersOnResponse(MessageUpdatableContext context, boolean handleFault) {
        try {
            this.processor.callHandlersResponse(HandlerProcessor.Direction.INBOUND, context, handleFault);
        } catch (WebServiceException wse) {
            throw wse;
        } catch (RuntimeException re) {
            throw new WebServiceException(re);
        }
    }

    @Override // com.sun.xml.internal.ws.handler.HandlerTube
    void closeHandlers(MessageContext mc) {
        closeClientsideHandlers(mc);
    }
}
