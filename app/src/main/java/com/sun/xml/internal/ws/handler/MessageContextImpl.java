package com.sun.xml.internal.ws.handler;

import com.sun.xml.internal.ws.api.message.Attachment;
import com.sun.xml.internal.ws.api.message.AttachmentSet;
import com.sun.xml.internal.ws.api.message.Packet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.activation.DataHandler;
import javax.xml.ws.handler.MessageContext;

/* loaded from: rt.jar:com/sun/xml/internal/ws/handler/MessageContextImpl.class */
class MessageContextImpl implements MessageContext {
    private final Set<String> handlerScopeProps;
    private final Packet packet;
    private final Map<String, Object> asMapIncludingInvocationProperties;

    public MessageContextImpl(Packet packet) {
        this.packet = packet;
        this.asMapIncludingInvocationProperties = packet.asMapIncludingInvocationProperties();
        this.handlerScopeProps = packet.getHandlerScopePropertyNames(false);
    }

    protected void updatePacket() {
        throw new UnsupportedOperationException("wrong call");
    }

    @Override // javax.xml.ws.handler.MessageContext
    public void setScope(String name, MessageContext.Scope scope) {
        if (!containsKey(name)) {
            throw new IllegalArgumentException("Property " + name + " does not exist.");
        }
        if (scope == MessageContext.Scope.APPLICATION) {
            this.handlerScopeProps.remove(name);
        } else {
            this.handlerScopeProps.add(name);
        }
    }

    @Override // javax.xml.ws.handler.MessageContext
    public MessageContext.Scope getScope(String name) {
        if (!containsKey(name)) {
            throw new IllegalArgumentException("Property " + name + " does not exist.");
        }
        if (this.handlerScopeProps.contains(name)) {
            return MessageContext.Scope.HANDLER;
        }
        return MessageContext.Scope.APPLICATION;
    }

    @Override // java.util.Map
    public int size() {
        return this.asMapIncludingInvocationProperties.size();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return this.asMapIncludingInvocationProperties.isEmpty();
    }

    @Override // java.util.Map
    public boolean containsKey(Object key) {
        return this.asMapIncludingInvocationProperties.containsKey(key);
    }

    @Override // java.util.Map
    public boolean containsValue(Object value) {
        return this.asMapIncludingInvocationProperties.containsValue(value);
    }

    @Override // java.util.Map
    public Object put(String key, Object value) {
        if (!this.asMapIncludingInvocationProperties.containsKey(key)) {
            this.handlerScopeProps.add(key);
        }
        return this.asMapIncludingInvocationProperties.put(key, value);
    }

    @Override // java.util.Map
    public Object get(Object key) {
        if (key == null) {
            return null;
        }
        Object value = this.asMapIncludingInvocationProperties.get(key);
        if (key.equals(MessageContext.OUTBOUND_MESSAGE_ATTACHMENTS) || key.equals(MessageContext.INBOUND_MESSAGE_ATTACHMENTS)) {
            Map<String, DataHandler> atts = (Map) value;
            if (atts == null) {
                atts = new HashMap<>();
            }
            AttachmentSet attSet = this.packet.getMessage().getAttachments();
            for (Attachment att : attSet) {
                String cid = att.getContentId();
                if (cid.indexOf("@jaxws.sun.com") == -1) {
                    Object a2 = atts.get(cid);
                    if (a2 == null) {
                        Object a3 = atts.get("<" + cid + ">");
                        if (a3 == null) {
                            atts.put(att.getContentId(), att.asDataHandler());
                        }
                    }
                } else {
                    atts.put(att.getContentId(), att.asDataHandler());
                }
            }
            return atts;
        }
        return value;
    }

    @Override // java.util.Map
    public void putAll(Map<? extends String, ? extends Object> t2) {
        for (String key : t2.keySet()) {
            if (!this.asMapIncludingInvocationProperties.containsKey(key)) {
                this.handlerScopeProps.add(key);
            }
        }
        this.asMapIncludingInvocationProperties.putAll(t2);
    }

    @Override // java.util.Map
    public void clear() {
        this.asMapIncludingInvocationProperties.clear();
    }

    @Override // java.util.Map
    public Object remove(Object key) {
        this.handlerScopeProps.remove(key);
        return this.asMapIncludingInvocationProperties.remove(key);
    }

    @Override // java.util.Map
    public Set<String> keySet() {
        return this.asMapIncludingInvocationProperties.keySet();
    }

    @Override // java.util.Map
    public Set<Map.Entry<String, Object>> entrySet() {
        return this.asMapIncludingInvocationProperties.entrySet();
    }

    @Override // java.util.Map
    public Collection<Object> values() {
        return this.asMapIncludingInvocationProperties.values();
    }
}
