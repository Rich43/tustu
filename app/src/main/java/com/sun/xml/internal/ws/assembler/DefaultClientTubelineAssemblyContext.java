package com.sun.xml.internal.ws.assembler;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.EndpointAddress;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.WSService;
import com.sun.xml.internal.ws.api.client.WSPortInfo;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.internal.ws.api.pipe.Codec;
import com.sun.xml.internal.ws.api.server.Container;
import com.sun.xml.internal.ws.assembler.dev.ClientTubelineAssemblyContext;
import com.sun.xml.internal.ws.policy.PolicyMap;

/* loaded from: rt.jar:com/sun/xml/internal/ws/assembler/DefaultClientTubelineAssemblyContext.class */
class DefaultClientTubelineAssemblyContext extends TubelineAssemblyContextImpl implements ClientTubelineAssemblyContext {

    @NotNull
    private final ClientTubeAssemblerContext wrappedContext;
    private final PolicyMap policyMap;
    private final WSPortInfo portInfo;
    private final WSDLPort wsdlPort;

    public DefaultClientTubelineAssemblyContext(@NotNull ClientTubeAssemblerContext context) {
        this.wrappedContext = context;
        this.wsdlPort = context.getWsdlModel();
        this.portInfo = context.getPortInfo();
        this.policyMap = context.getPortInfo().getPolicyMap();
    }

    @Override // com.sun.xml.internal.ws.assembler.dev.ClientTubelineAssemblyContext
    public PolicyMap getPolicyMap() {
        return this.policyMap;
    }

    @Override // com.sun.xml.internal.ws.assembler.dev.ClientTubelineAssemblyContext
    public boolean isPolicyAvailable() {
        return (this.policyMap == null || this.policyMap.isEmpty()) ? false : true;
    }

    @Override // com.sun.xml.internal.ws.assembler.dev.ClientTubelineAssemblyContext
    public WSDLPort getWsdlPort() {
        return this.wsdlPort;
    }

    @Override // com.sun.xml.internal.ws.assembler.dev.ClientTubelineAssemblyContext
    public WSPortInfo getPortInfo() {
        return this.portInfo;
    }

    @Override // com.sun.xml.internal.ws.assembler.dev.ClientTubelineAssemblyContext
    @NotNull
    public EndpointAddress getAddress() {
        return this.wrappedContext.getAddress();
    }

    @Override // com.sun.xml.internal.ws.assembler.dev.ClientTubelineAssemblyContext
    @NotNull
    public WSService getService() {
        return this.wrappedContext.getService();
    }

    @Override // com.sun.xml.internal.ws.assembler.dev.ClientTubelineAssemblyContext
    @NotNull
    public WSBinding getBinding() {
        return this.wrappedContext.getBinding();
    }

    @Override // com.sun.xml.internal.ws.assembler.dev.ClientTubelineAssemblyContext
    @Nullable
    public SEIModel getSEIModel() {
        return this.wrappedContext.getSEIModel();
    }

    @Override // com.sun.xml.internal.ws.assembler.dev.ClientTubelineAssemblyContext
    public Container getContainer() {
        return this.wrappedContext.getContainer();
    }

    @Override // com.sun.xml.internal.ws.assembler.dev.ClientTubelineAssemblyContext
    @NotNull
    public Codec getCodec() {
        return this.wrappedContext.getCodec();
    }

    @Override // com.sun.xml.internal.ws.assembler.dev.ClientTubelineAssemblyContext
    public void setCodec(@NotNull Codec codec) {
        this.wrappedContext.setCodec(codec);
    }

    @Override // com.sun.xml.internal.ws.assembler.dev.ClientTubelineAssemblyContext
    public ClientTubeAssemblerContext getWrappedContext() {
        return this.wrappedContext;
    }
}
