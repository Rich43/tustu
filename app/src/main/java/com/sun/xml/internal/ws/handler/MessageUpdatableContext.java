package com.sun.xml.internal.ws.handler;

import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.Packet;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.xml.ws.handler.MessageContext;

/* loaded from: rt.jar:com/sun/xml/internal/ws/handler/MessageUpdatableContext.class */
public abstract class MessageUpdatableContext implements MessageContext {
    final Packet packet;
    private MessageContextImpl ctxt;

    abstract void updateMessage();

    abstract void setPacketMessage(Message message);

    public MessageUpdatableContext(Packet packet) {
        this.ctxt = new MessageContextImpl(packet);
        this.packet = packet;
    }

    Message getPacketMessage() {
        updateMessage();
        return this.packet.getMessage();
    }

    public final void updatePacket() {
        updateMessage();
    }

    MessageContextImpl getMessageContext() {
        return this.ctxt;
    }

    @Override // javax.xml.ws.handler.MessageContext
    public void setScope(String name, MessageContext.Scope scope) {
        this.ctxt.setScope(name, scope);
    }

    @Override // javax.xml.ws.handler.MessageContext
    public MessageContext.Scope getScope(String name) {
        return this.ctxt.getScope(name);
    }

    @Override // java.util.Map
    public void clear() {
        this.ctxt.clear();
    }

    @Override // java.util.Map
    public boolean containsKey(Object obj) {
        return this.ctxt.containsKey(obj);
    }

    @Override // java.util.Map
    public boolean containsValue(Object obj) {
        return this.ctxt.containsValue(obj);
    }

    @Override // java.util.Map
    public Set<Map.Entry<String, Object>> entrySet() {
        return this.ctxt.entrySet();
    }

    @Override // java.util.Map
    public Object get(Object obj) {
        return this.ctxt.get(obj);
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return this.ctxt.isEmpty();
    }

    @Override // java.util.Map
    public Set<String> keySet() {
        return this.ctxt.keySet();
    }

    @Override // java.util.Map
    public Object put(String str, Object obj) {
        return this.ctxt.put(str, obj);
    }

    @Override // java.util.Map
    public void putAll(Map<? extends String, ? extends Object> map) {
        this.ctxt.putAll(map);
    }

    @Override // java.util.Map
    public Object remove(Object obj) {
        return this.ctxt.remove(obj);
    }

    @Override // java.util.Map
    public int size() {
        return this.ctxt.size();
    }

    @Override // java.util.Map
    public Collection<Object> values() {
        return this.ctxt.values();
    }
}
