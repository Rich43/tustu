package com.sun.xml.internal.ws.server;

import com.sun.istack.internal.NotNull;
import com.sun.org.glassfish.gmbal.AMXMetadata;
import com.sun.org.glassfish.gmbal.Description;
import com.sun.org.glassfish.gmbal.ManagedAttribute;
import com.sun.org.glassfish.gmbal.ManagedObject;
import com.sun.xml.internal.ws.api.BindingID;
import com.sun.xml.internal.ws.api.EndpointAddress;
import com.sun.xml.internal.ws.api.WSFeatureList;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.api.server.Container;
import com.sun.xml.internal.ws.api.server.WSEndpoint;
import com.sun.xml.internal.ws.transport.http.HttpAdapter;
import com.sun.xml.internal.ws.util.RuntimeVersion;
import java.net.URL;
import java.util.Set;
import javax.xml.namespace.QName;

@ManagedObject
@Description("Metro Web Service endpoint")
@AMXMetadata(type = "WSEndpoint")
/* loaded from: rt.jar:com/sun/xml/internal/ws/server/MonitorRootService.class */
public final class MonitorRootService extends MonitorBase {
    private final WSEndpoint endpoint;

    MonitorRootService(WSEndpoint endpoint) {
        this.endpoint = endpoint;
    }

    @ManagedAttribute
    @Description("Policy associated with Endpoint")
    public String policy() {
        if (this.endpoint.getPolicyMap() != null) {
            return this.endpoint.getPolicyMap().toString();
        }
        return null;
    }

    @ManagedAttribute
    @Description("Container")
    @NotNull
    public Container container() {
        return this.endpoint.getContainer();
    }

    @ManagedAttribute
    @Description("Port name")
    @NotNull
    public QName portName() {
        return this.endpoint.getPortName();
    }

    @ManagedAttribute
    @Description("Service name")
    @NotNull
    public QName serviceName() {
        return this.endpoint.getServiceName();
    }

    @ManagedAttribute
    @Description("Binding SOAP Version")
    public String soapVersionHttpBindingId() {
        return this.endpoint.getBinding().getSOAPVersion().httpBindingId;
    }

    @ManagedAttribute
    @Description("Binding Addressing Version")
    public AddressingVersion addressingVersion() {
        return this.endpoint.getBinding().getAddressingVersion();
    }

    @ManagedAttribute
    @Description("Binding Identifier")
    @NotNull
    public BindingID bindingID() {
        return this.endpoint.getBinding().getBindingId();
    }

    @ManagedAttribute
    @Description("Binding features")
    @NotNull
    public WSFeatureList features() {
        return this.endpoint.getBinding().getFeatures();
    }

    @ManagedAttribute
    @Description("WSDLPort bound port type")
    public QName wsdlPortTypeName() {
        if (this.endpoint.getPort() != null) {
            return this.endpoint.getPort().getBinding().getPortTypeName();
        }
        return null;
    }

    @ManagedAttribute
    @Description("Endpoint address")
    public EndpointAddress wsdlEndpointAddress() {
        if (this.endpoint.getPort() != null) {
            return this.endpoint.getPort().getAddress();
        }
        return null;
    }

    @ManagedAttribute
    @Description("Documents referenced")
    public Set<String> serviceDefinitionImports() {
        if (this.endpoint.getServiceDefinition() != null) {
            return this.endpoint.getServiceDefinition().getPrimary().getImports();
        }
        return null;
    }

    @ManagedAttribute
    @Description("System ID where document is taken from")
    public URL serviceDefinitionURL() {
        if (this.endpoint.getServiceDefinition() != null) {
            return this.endpoint.getServiceDefinition().getPrimary().getURL();
        }
        return null;
    }

    @ManagedAttribute
    @Description("SEI model WSDL location")
    public String seiModelWSDLLocation() {
        if (this.endpoint.getSEIModel() != null) {
            return this.endpoint.getSEIModel().getWSDLLocation();
        }
        return null;
    }

    @ManagedAttribute
    @Description("JAX-WS runtime version")
    public String jaxwsRuntimeVersion() {
        return RuntimeVersion.VERSION.toString();
    }

    @ManagedAttribute
    @Description("If true: show what goes across HTTP transport")
    public boolean dumpHTTPMessages() {
        return HttpAdapter.dump;
    }

    @ManagedAttribute
    @Description("Show what goes across HTTP transport")
    public void dumpHTTPMessages(boolean x2) {
        HttpAdapter.setDump(x2);
    }
}
