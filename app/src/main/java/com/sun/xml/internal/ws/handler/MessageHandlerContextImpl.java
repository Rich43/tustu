package com.sun.xml.internal.ws.handler;

import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.handler.MessageHandlerContext;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import java.util.Set;

/* loaded from: rt.jar:com/sun/xml/internal/ws/handler/MessageHandlerContextImpl.class */
public class MessageHandlerContextImpl extends MessageUpdatableContext implements MessageHandlerContext {

    @Nullable
    private SEIModel seiModel;
    private Set<String> roles;
    private WSBinding binding;

    @Nullable
    private WSDLPort wsdlModel;

    public MessageHandlerContextImpl(@Nullable SEIModel seiModel, WSBinding binding, @Nullable WSDLPort wsdlModel, Packet packet, Set<String> roles) {
        super(packet);
        this.seiModel = seiModel;
        this.binding = binding;
        this.wsdlModel = wsdlModel;
        this.roles = roles;
    }

    @Override // com.sun.xml.internal.ws.api.handler.MessageHandlerContext
    public Message getMessage() {
        return this.packet.getMessage();
    }

    @Override // com.sun.xml.internal.ws.api.handler.MessageHandlerContext
    public void setMessage(Message message) {
        this.packet.setMessage(message);
    }

    @Override // com.sun.xml.internal.ws.api.handler.MessageHandlerContext
    public Set<String> getRoles() {
        return this.roles;
    }

    @Override // com.sun.xml.internal.ws.api.handler.MessageHandlerContext
    public WSBinding getWSBinding() {
        return this.binding;
    }

    @Override // com.sun.xml.internal.ws.api.handler.MessageHandlerContext
    @Nullable
    public SEIModel getSEIModel() {
        return this.seiModel;
    }

    @Override // com.sun.xml.internal.ws.api.handler.MessageHandlerContext
    @Nullable
    public WSDLPort getPort() {
        return this.wsdlModel;
    }

    @Override // com.sun.xml.internal.ws.handler.MessageUpdatableContext
    void updateMessage() {
    }

    @Override // com.sun.xml.internal.ws.handler.MessageUpdatableContext
    void setPacketMessage(Message newMessage) {
        setMessage(newMessage);
    }
}
