package com.sun.xml.internal.ws.client;

import com.oracle.webservices.internal.api.message.BaseDistributedPropertySet;
import com.oracle.webservices.internal.api.message.BasePropertySet;
import com.oracle.webservices.internal.api.message.MessageContext;
import com.oracle.webservices.internal.api.message.PropertySet;
import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.EndpointAddress;
import com.sun.xml.internal.ws.api.PropertySet;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.transport.Headers;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.xml.ws.BindingProvider;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/RequestContext.class */
public final class RequestContext extends BaseDistributedPropertySet {

    @NotNull
    private EndpointAddress endpointAddress;
    public ContentNegotiation contentNegotiation;
    private String soapAction;
    private Boolean soapActionUse;
    private static final Logger LOGGER = Logger.getLogger(RequestContext.class.getName());
    private static ContentNegotiation defaultContentNegotiation = ContentNegotiation.obtainFromSystemProperty();
    private static final BasePropertySet.PropertyMap propMap = parse(RequestContext.class);

    public void addSatellite(@NotNull PropertySet satellite) {
        super.addSatellite((com.oracle.webservices.internal.api.message.PropertySet) satellite);
    }

    @PropertySet.Property({BindingProvider.ENDPOINT_ADDRESS_PROPERTY})
    public String getEndPointAddressString() {
        if (this.endpointAddress != null) {
            return this.endpointAddress.toString();
        }
        return null;
    }

    public void setEndPointAddressString(String s2) {
        if (s2 == null) {
            throw new IllegalArgumentException();
        }
        this.endpointAddress = EndpointAddress.create(s2);
    }

    public void setEndpointAddress(@NotNull EndpointAddress epa) {
        this.endpointAddress = epa;
    }

    @NotNull
    public EndpointAddress getEndpointAddress() {
        return this.endpointAddress;
    }

    @PropertySet.Property({ContentNegotiation.PROPERTY})
    public String getContentNegotiationString() {
        return this.contentNegotiation.toString();
    }

    public void setContentNegotiationString(String s2) {
        if (s2 == null) {
            this.contentNegotiation = ContentNegotiation.none;
            return;
        }
        try {
            this.contentNegotiation = ContentNegotiation.valueOf(s2);
        } catch (IllegalArgumentException e2) {
            this.contentNegotiation = ContentNegotiation.none;
        }
    }

    @PropertySet.Property({BindingProvider.SOAPACTION_URI_PROPERTY})
    public String getSoapAction() {
        return this.soapAction;
    }

    public void setSoapAction(String sAction) {
        this.soapAction = sAction;
    }

    @PropertySet.Property({BindingProvider.SOAPACTION_USE_PROPERTY})
    public Boolean getSoapActionUse() {
        return this.soapActionUse;
    }

    public void setSoapActionUse(Boolean sActionUse) {
        this.soapActionUse = sActionUse;
    }

    RequestContext() {
        this.contentNegotiation = defaultContentNegotiation;
    }

    private RequestContext(RequestContext that) {
        this.contentNegotiation = defaultContentNegotiation;
        for (Map.Entry<String, Object> entry : that.asMapLocal().entrySet()) {
            if (!propMap.containsKey(entry.getKey())) {
                asMap().put(entry.getKey(), entry.getValue());
            }
        }
        this.endpointAddress = that.endpointAddress;
        this.soapAction = that.soapAction;
        this.soapActionUse = that.soapActionUse;
        this.contentNegotiation = that.contentNegotiation;
        that.copySatelliteInto(this);
    }

    @Override // com.oracle.webservices.internal.api.message.BaseDistributedPropertySet, com.oracle.webservices.internal.api.message.BasePropertySet, com.oracle.webservices.internal.api.message.PropertySet
    public Object get(Object key) {
        if (supports(key)) {
            return super.get(key);
        }
        return asMap().get(key);
    }

    @Override // com.oracle.webservices.internal.api.message.BaseDistributedPropertySet, com.oracle.webservices.internal.api.message.BasePropertySet, com.oracle.webservices.internal.api.message.PropertySet
    public Object put(String key, Object value) {
        if (supports(key)) {
            return super.put(key, value);
        }
        return asMap().put(key, value);
    }

    public void fill(Packet packet, boolean isAddressingEnabled) {
        if (this.endpointAddress != null) {
            packet.endpointAddress = this.endpointAddress;
        }
        packet.contentNegotiation = this.contentNegotiation;
        fillSOAPAction(packet, isAddressingEnabled);
        mergeRequestHeaders(packet);
        Set<String> handlerScopeNames = new HashSet<>();
        copySatelliteInto((MessageContext) packet);
        for (String key : asMapLocal().keySet()) {
            if (!supportsLocal(key)) {
                handlerScopeNames.add(key);
            }
            if (!propMap.containsKey(key)) {
                Object value = asMapLocal().get(key);
                if (packet.supports(key)) {
                    packet.put(key, value);
                } else {
                    packet.invocationProperties.put(key, value);
                }
            }
        }
        if (!handlerScopeNames.isEmpty()) {
            packet.getHandlerScopePropertyNames(false).addAll(handlerScopeNames);
        }
    }

    private void mergeRequestHeaders(Packet packet) {
        Headers packetHeaders = (Headers) packet.invocationProperties.get(javax.xml.ws.handler.MessageContext.HTTP_REQUEST_HEADERS);
        Map<String, List<String>> myHeaders = (Map) asMap().get(javax.xml.ws.handler.MessageContext.HTTP_REQUEST_HEADERS);
        if (packetHeaders != null && myHeaders != null) {
            for (Map.Entry<String, List<String>> entry : myHeaders.entrySet()) {
                String key = entry.getKey();
                if (key != null && key.trim().length() != 0) {
                    List<String> listFromPacket = packetHeaders.get(key);
                    if (listFromPacket != null) {
                        listFromPacket.addAll(entry.getValue());
                    } else {
                        packetHeaders.put(key, myHeaders.get(key));
                    }
                }
            }
            asMap().put(javax.xml.ws.handler.MessageContext.HTTP_REQUEST_HEADERS, packetHeaders);
        }
    }

    private void fillSOAPAction(Packet packet, boolean isAddressingEnabled) {
        boolean p2 = packet.packetTakesPriorityOverRequestContext;
        String localSoapAction = p2 ? packet.soapAction : this.soapAction;
        Boolean localSoapActionUse = p2 ? (Boolean) packet.invocationProperties.get(BindingProvider.SOAPACTION_USE_PROPERTY) : this.soapActionUse;
        if (((localSoapActionUse != null && localSoapActionUse.booleanValue()) || (localSoapActionUse == null && isAddressingEnabled)) && localSoapAction != null) {
            packet.soapAction = localSoapAction;
        }
        if (isAddressingEnabled) {
            return;
        }
        if ((localSoapActionUse == null || !localSoapActionUse.booleanValue()) && localSoapAction != null) {
            LOGGER.warning("BindingProvider.SOAPACTION_URI_PROPERTY is set in the RequestContext but is ineffective, Either set BindingProvider.SOAPACTION_USE_PROPERTY to true or enable AddressingFeature");
        }
    }

    public RequestContext copy() {
        return new RequestContext(this);
    }

    @Override // com.oracle.webservices.internal.api.message.BasePropertySet
    protected BasePropertySet.PropertyMap getPropertyMap() {
        return propMap;
    }

    @Override // com.oracle.webservices.internal.api.message.BasePropertySet
    protected boolean mapAllowsAdditionalProperties() {
        return true;
    }
}
