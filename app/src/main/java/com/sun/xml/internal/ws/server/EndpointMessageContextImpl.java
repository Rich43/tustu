package com.sun.xml.internal.ws.server;

import com.sun.xml.internal.ws.api.message.Attachment;
import com.sun.xml.internal.ws.api.message.AttachmentSet;
import com.sun.xml.internal.ws.api.message.Packet;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.activation.DataHandler;
import javax.xml.ws.handler.MessageContext;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/EndpointMessageContextImpl.class */
public final class EndpointMessageContextImpl extends AbstractMap<String, Object> implements MessageContext {
    private Set<Map.Entry<String, Object>> entrySet;
    private final Packet packet;

    public EndpointMessageContextImpl(Packet packet) {
        this.packet = packet;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object get(Object key) {
        if (this.packet.supports(key)) {
            return this.packet.get(key);
        }
        if (this.packet.getHandlerScopePropertyNames(true).contains(key)) {
            return null;
        }
        Object value = this.packet.invocationProperties.get(key);
        if (key.equals(MessageContext.OUTBOUND_MESSAGE_ATTACHMENTS) || key.equals(MessageContext.INBOUND_MESSAGE_ATTACHMENTS)) {
            Map<String, DataHandler> atts = (Map) value;
            if (atts == null) {
                atts = new HashMap<>();
            }
            AttachmentSet attSet = this.packet.getMessage().getAttachments();
            for (Attachment att : attSet) {
                atts.put(att.getContentId(), att.asDataHandler());
            }
            return atts;
        }
        return value;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object put(String key, Object value) {
        if (this.packet.supports(key)) {
            return this.packet.put(key, value);
        }
        Object old = this.packet.invocationProperties.get(key);
        if (old != null) {
            if (this.packet.getHandlerScopePropertyNames(true).contains(key)) {
                throw new IllegalArgumentException("Cannot overwrite property in HANDLER scope");
            }
            this.packet.invocationProperties.put(key, value);
            return old;
        }
        this.packet.invocationProperties.put(key, value);
        return null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object remove(Object key) {
        if (this.packet.supports(key)) {
            return this.packet.remove(key);
        }
        Object old = this.packet.invocationProperties.get(key);
        if (old != null) {
            if (this.packet.getHandlerScopePropertyNames(true).contains(key)) {
                throw new IllegalArgumentException("Cannot remove property in HANDLER scope");
            }
            this.packet.invocationProperties.remove(key);
            return old;
        }
        return null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<String, Object>> entrySet() {
        if (this.entrySet == null) {
            this.entrySet = new EntrySet();
        }
        return this.entrySet;
    }

    @Override // javax.xml.ws.handler.MessageContext
    public void setScope(String name, MessageContext.Scope scope) {
        throw new UnsupportedOperationException("All the properties in this context are in APPLICATION scope. Cannot do setScope().");
    }

    @Override // javax.xml.ws.handler.MessageContext
    public MessageContext.Scope getScope(String name) {
        throw new UnsupportedOperationException("All the properties in this context are in APPLICATION scope. Cannot do getScope().");
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/server/EndpointMessageContextImpl$EntrySet.class */
    private class EntrySet extends AbstractSet<Map.Entry<String, Object>> {
        private EntrySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<Map.Entry<String, Object>> iterator() {
            final Iterator<Map.Entry<String, Object>> it = EndpointMessageContextImpl.this.createBackupMap().entrySet().iterator();
            return new Iterator<Map.Entry<String, Object>>() { // from class: com.sun.xml.internal.ws.server.EndpointMessageContextImpl.EntrySet.1
                Map.Entry<String, Object> cur;

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return it.hasNext();
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.Iterator
                public Map.Entry<String, Object> next() {
                    this.cur = (Map.Entry) it.next();
                    return this.cur;
                }

                @Override // java.util.Iterator
                public void remove() {
                    it.remove();
                    EndpointMessageContextImpl.this.remove(this.cur.getKey());
                }
            };
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return EndpointMessageContextImpl.this.createBackupMap().size();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Map<String, Object> createBackupMap() {
        Map<String, Object> backupMap = new HashMap<>();
        backupMap.putAll(this.packet.createMapView());
        Set<String> handlerProps = this.packet.getHandlerScopePropertyNames(true);
        for (Map.Entry<String, Object> e2 : this.packet.invocationProperties.entrySet()) {
            if (!handlerProps.contains(e2.getKey())) {
                backupMap.put(e2.getKey(), e2.getValue());
            }
        }
        return backupMap;
    }
}
