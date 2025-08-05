package com.sun.xml.internal.ws.assembler;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.api.pipe.Codec;
import com.sun.xml.internal.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.api.server.WSEndpoint;
import com.sun.xml.internal.ws.assembler.dev.ServerTubelineAssemblyContext;
import com.sun.xml.internal.ws.policy.PolicyMap;

/* loaded from: rt.jar:com/sun/xml/internal/ws/assembler/DefaultServerTubelineAssemblyContext.class */
class DefaultServerTubelineAssemblyContext extends TubelineAssemblyContextImpl implements ServerTubelineAssemblyContext {

    @NotNull
    private final ServerTubeAssemblerContext wrappedContext;
    private final PolicyMap policyMap;

    public DefaultServerTubelineAssemblyContext(@NotNull ServerTubeAssemblerContext context) {
        this.wrappedContext = context;
        this.policyMap = context.getEndpoint().getPolicyMap();
    }

    @Override // com.sun.xml.internal.ws.assembler.dev.ServerTubelineAssemblyContext
    public PolicyMap getPolicyMap() {
        return this.policyMap;
    }

    @Override // com.sun.xml.internal.ws.assembler.dev.ServerTubelineAssemblyContext
    public boolean isPolicyAvailable() {
        return (this.policyMap == null || this.policyMap.isEmpty()) ? false : true;
    }

    @Override // com.sun.xml.internal.ws.assembler.dev.ServerTubelineAssemblyContext
    @Nullable
    public SEIModel getSEIModel() {
        return this.wrappedContext.getSEIModel();
    }

    @Override // com.sun.xml.internal.ws.assembler.dev.ServerTubelineAssemblyContext
    @Nullable
    public WSDLPort getWsdlPort() {
        return this.wrappedContext.getWsdlModel();
    }

    @Override // com.sun.xml.internal.ws.assembler.dev.ServerTubelineAssemblyContext
    @NotNull
    public WSEndpoint getEndpoint() {
        return this.wrappedContext.getEndpoint();
    }

    @Override // com.sun.xml.internal.ws.assembler.dev.ServerTubelineAssemblyContext
    @NotNull
    public Tube getTerminalTube() {
        return this.wrappedContext.getTerminalTube();
    }

    @Override // com.sun.xml.internal.ws.assembler.dev.ServerTubelineAssemblyContext
    public boolean isSynchronous() {
        return this.wrappedContext.isSynchronous();
    }

    @Override // com.sun.xml.internal.ws.assembler.dev.ServerTubelineAssemblyContext
    @NotNull
    public Codec getCodec() {
        return this.wrappedContext.getCodec();
    }

    @Override // com.sun.xml.internal.ws.assembler.dev.ServerTubelineAssemblyContext
    public void setCodec(@NotNull Codec codec) {
        this.wrappedContext.setCodec(codec);
    }

    @Override // com.sun.xml.internal.ws.assembler.dev.ServerTubelineAssemblyContext
    public ServerTubeAssemblerContext getWrappedContext() {
        return this.wrappedContext;
    }
}
