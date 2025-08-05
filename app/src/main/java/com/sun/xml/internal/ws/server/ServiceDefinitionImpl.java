package com.sun.xml.internal.ws.server;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.server.SDDocument;
import com.sun.xml.internal.ws.api.server.SDDocumentFilter;
import com.sun.xml.internal.ws.api.server.ServiceDefinition;
import com.sun.xml.internal.ws.wsdl.SDDocumentResolver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/ServiceDefinitionImpl.class */
public final class ServiceDefinitionImpl implements ServiceDefinition, SDDocumentResolver {
    private final List<SDDocumentImpl> docs;
    private final Map<String, SDDocumentImpl> bySystemId;

    @NotNull
    private final SDDocumentImpl primaryWsdl;
    WSEndpointImpl<?> owner;
    final List<SDDocumentFilter> filters = new ArrayList();
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ServiceDefinitionImpl.class.desiredAssertionStatus();
    }

    public ServiceDefinitionImpl(List<SDDocumentImpl> docs, @NotNull SDDocumentImpl primaryWsdl) {
        if (!$assertionsDisabled && !docs.contains(primaryWsdl)) {
            throw new AssertionError();
        }
        this.docs = docs;
        this.primaryWsdl = primaryWsdl;
        this.bySystemId = new HashMap(docs.size());
        for (SDDocumentImpl doc : docs) {
            this.bySystemId.put(doc.getURL().toExternalForm(), doc);
            doc.setFilters(this.filters);
            doc.setResolver(this);
        }
    }

    void setOwner(WSEndpointImpl<?> owner) {
        if (!$assertionsDisabled && (owner == null || this.owner != null)) {
            throw new AssertionError();
        }
        this.owner = owner;
    }

    @Override // com.sun.xml.internal.ws.api.server.ServiceDefinition
    @NotNull
    public SDDocument getPrimary() {
        return this.primaryWsdl;
    }

    @Override // com.sun.xml.internal.ws.api.server.ServiceDefinition
    public void addFilter(SDDocumentFilter filter) {
        this.filters.add(filter);
    }

    @Override // java.lang.Iterable, java.util.List
    public Iterator<SDDocument> iterator() {
        return this.docs.iterator();
    }

    @Override // com.sun.xml.internal.ws.wsdl.SDDocumentResolver
    public SDDocument resolve(String systemId) {
        return this.bySystemId.get(systemId);
    }
}
