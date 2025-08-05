package com.sun.xml.internal.ws.client;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.BindingID;
import com.sun.xml.internal.ws.api.EndpointAddress;
import com.sun.xml.internal.ws.api.WSService;
import com.sun.xml.internal.ws.api.client.WSPortInfo;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.api.policy.PolicyResolver;
import com.sun.xml.internal.ws.api.policy.PolicyResolverFactory;
import com.sun.xml.internal.ws.binding.BindingImpl;
import com.sun.xml.internal.ws.binding.WebServiceFeatureList;
import com.sun.xml.internal.ws.policy.PolicyMap;
import com.sun.xml.internal.ws.policy.jaxws.PolicyUtil;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/PortInfo.class */
public class PortInfo implements WSPortInfo {

    @NotNull
    private final WSServiceDelegate owner;

    @NotNull
    public final QName portName;

    @NotNull
    public final EndpointAddress targetEndpoint;

    @NotNull
    public final BindingID bindingId;

    @NotNull
    public final PolicyMap policyMap = createPolicyMap();

    @Nullable
    public final WSDLPort portModel;

    public PortInfo(WSServiceDelegate owner, EndpointAddress targetEndpoint, QName name, BindingID bindingId) {
        this.owner = owner;
        this.targetEndpoint = targetEndpoint;
        this.portName = name;
        this.bindingId = bindingId;
        this.portModel = getPortModel(owner, name);
    }

    public PortInfo(@NotNull WSServiceDelegate owner, @NotNull WSDLPort port) {
        this.owner = owner;
        this.targetEndpoint = port.getAddress();
        this.portName = port.getName();
        this.bindingId = port.getBinding().getBindingId();
        this.portModel = port;
    }

    @Override // com.sun.xml.internal.ws.api.client.WSPortInfo
    public PolicyMap getPolicyMap() {
        return this.policyMap;
    }

    public PolicyMap createPolicyMap() throws WebServiceException {
        PolicyMap map;
        if (this.portModel != null) {
            map = this.portModel.getOwner().getParent().getPolicyMap();
        } else {
            map = PolicyResolverFactory.create().resolve(new PolicyResolver.ClientContext(null, this.owner.getContainer()));
        }
        if (map == null) {
            map = PolicyMap.createPolicyMap(null);
        }
        return map;
    }

    public BindingImpl createBinding(WebServiceFeature[] webServiceFeatures, Class<?> portInterface) {
        return createBinding(new WebServiceFeatureList(webServiceFeatures), portInterface, null);
    }

    public BindingImpl createBinding(WebServiceFeatureList webServiceFeatures, Class<?> portInterface, BindingImpl existingBinding) {
        Iterable<WebServiceFeature> configFeatures;
        if (existingBinding != null) {
            webServiceFeatures.addAll(existingBinding.getFeatures());
        }
        if (this.portModel != null) {
            configFeatures = this.portModel.getFeatures();
        } else {
            configFeatures = PolicyUtil.getPortScopedFeatures(this.policyMap, this.owner.getServiceName(), this.portName);
        }
        webServiceFeatures.mergeFeatures(configFeatures, false);
        webServiceFeatures.mergeFeatures((Iterable<WebServiceFeature>) this.owner.serviceInterceptor.preCreateBinding(this, portInterface, webServiceFeatures), false);
        BindingImpl bindingImpl = BindingImpl.create(this.bindingId, webServiceFeatures.toArray());
        this.owner.getHandlerConfigurator().configureHandlers(this, bindingImpl);
        return bindingImpl;
    }

    private WSDLPort getPortModel(WSServiceDelegate owner, QName portName) {
        if (owner.getWsdlService() != null) {
            Iterable<? extends WSDLPort> ports = owner.getWsdlService().getPorts();
            for (WSDLPort port : ports) {
                if (port.getName().equals(portName)) {
                    return port;
                }
            }
            return null;
        }
        return null;
    }

    @Override // com.sun.xml.internal.ws.api.client.WSPortInfo
    @Nullable
    public WSDLPort getPort() {
        return this.portModel;
    }

    @Override // com.sun.xml.internal.ws.api.client.WSPortInfo
    @NotNull
    public WSService getOwner() {
        return this.owner;
    }

    @Override // com.sun.xml.internal.ws.api.client.WSPortInfo
    @NotNull
    public BindingID getBindingId() {
        return this.bindingId;
    }

    @Override // com.sun.xml.internal.ws.api.client.WSPortInfo
    @NotNull
    public EndpointAddress getEndpointAddress() {
        return this.targetEndpoint;
    }

    @Override // javax.xml.ws.handler.PortInfo
    public QName getServiceName() {
        return this.owner.getServiceName();
    }

    @Override // javax.xml.ws.handler.PortInfo
    public QName getPortName() {
        return this.portName;
    }

    @Override // javax.xml.ws.handler.PortInfo
    public String getBindingID() {
        return this.bindingId.toString();
    }
}
