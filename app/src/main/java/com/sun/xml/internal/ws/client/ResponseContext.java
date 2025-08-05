package com.sun.xml.internal.ws.client;

import com.sun.xml.internal.ws.api.message.Attachment;
import com.sun.xml.internal.ws.api.message.AttachmentSet;
import com.sun.xml.internal.ws.api.message.Packet;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.activation.DataHandler;
import javax.xml.ws.handler.MessageContext;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/ResponseContext.class */
public class ResponseContext extends AbstractMap<String, Object> {
    private final Packet packet;
    private Set<Map.Entry<String, Object>> entrySet;

    public ResponseContext(Packet packet) {
        this.packet = packet;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object key) {
        if (this.packet.supports(key)) {
            return this.packet.containsKey(key);
        }
        return this.packet.invocationProperties.containsKey(key) && !this.packet.getHandlerScopePropertyNames(true).contains(key);
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
        if (key.equals(MessageContext.INBOUND_MESSAGE_ATTACHMENTS)) {
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
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void putAll(Map<? extends String, ? extends Object> t2) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<String, Object>> entrySet() {
        if (this.entrySet == null) {
            Map<String, Object> r2 = new HashMap<>();
            r2.putAll(this.packet.invocationProperties);
            r2.keySet().removeAll(this.packet.getHandlerScopePropertyNames(true));
            r2.putAll(this.packet.createMapView());
            this.entrySet = Collections.unmodifiableSet(r2.entrySet());
        }
        return this.entrySet;
    }
}
