package com.sun.xml.internal.ws.api.pipe;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.addressing.W3CWsaClientTube;
import com.sun.xml.internal.ws.addressing.v200408.MemberSubmissionWsaClientTube;
import com.sun.xml.internal.ws.api.EndpointAddress;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.WSService;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.api.client.ClientPipelineHook;
import com.sun.xml.internal.ws.api.client.WSPortInfo;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.api.pipe.helper.PipeAdapter;
import com.sun.xml.internal.ws.api.server.Container;
import com.sun.xml.internal.ws.binding.BindingImpl;
import com.sun.xml.internal.ws.client.ClientSchemaValidationTube;
import com.sun.xml.internal.ws.developer.SchemaValidationFeature;
import com.sun.xml.internal.ws.developer.WSBindingProvider;
import com.sun.xml.internal.ws.handler.ClientLogicalHandlerTube;
import com.sun.xml.internal.ws.handler.ClientMessageHandlerTube;
import com.sun.xml.internal.ws.handler.ClientSOAPHandlerTube;
import com.sun.xml.internal.ws.handler.HandlerTube;
import com.sun.xml.internal.ws.protocol.soap.ClientMUTube;
import com.sun.xml.internal.ws.transport.DeferredTransportPipe;
import com.sun.xml.internal.ws.util.pipe.DumpTube;
import java.io.PrintStream;
import javax.xml.ws.soap.SOAPBinding;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/ClientTubeAssemblerContext.class */
public class ClientTubeAssemblerContext {

    @NotNull
    private final EndpointAddress address;

    @Nullable
    private final WSDLPort wsdlModel;

    @Nullable
    private final SEIModel seiModel;

    @Nullable
    private final Class sei;

    @NotNull
    private final WSService rootOwner;

    @NotNull
    private final WSBinding binding;

    @NotNull
    private final Container container;

    @NotNull
    private Codec codec;

    @Nullable
    private final WSBindingProvider bindingProvider;

    public ClientTubeAssemblerContext(@NotNull EndpointAddress address, @Nullable WSDLPort wsdlModel, @NotNull WSService rootOwner, @NotNull WSBinding binding) {
        this(address, wsdlModel, rootOwner, binding, Container.NONE);
    }

    public ClientTubeAssemblerContext(@NotNull EndpointAddress address, @Nullable WSDLPort wsdlModel, @NotNull WSService rootOwner, @NotNull WSBinding binding, @NotNull Container container) {
        this(address, wsdlModel, rootOwner, binding, container, ((BindingImpl) binding).createCodec());
    }

    public ClientTubeAssemblerContext(@NotNull EndpointAddress address, @Nullable WSDLPort wsdlModel, @NotNull WSService rootOwner, @NotNull WSBinding binding, @NotNull Container container, Codec codec) {
        this(address, wsdlModel, rootOwner, binding, container, codec, (SEIModel) null, (Class) null);
    }

    public ClientTubeAssemblerContext(@NotNull EndpointAddress address, @Nullable WSDLPort wsdlModel, @NotNull WSService rootOwner, @NotNull WSBinding binding, @NotNull Container container, Codec codec, SEIModel seiModel, Class sei) {
        this(address, wsdlModel, rootOwner, null, binding, container, codec, seiModel, sei);
    }

    public ClientTubeAssemblerContext(@NotNull EndpointAddress address, @Nullable WSDLPort wsdlModel, @NotNull WSBindingProvider bindingProvider, @NotNull WSBinding binding, @NotNull Container container, Codec codec, SEIModel seiModel, Class sei) {
        this(address, wsdlModel, bindingProvider == null ? null : bindingProvider.getPortInfo().getOwner(), bindingProvider, binding, container, codec, seiModel, sei);
    }

    private ClientTubeAssemblerContext(@NotNull EndpointAddress address, @Nullable WSDLPort wsdlModel, @Nullable WSService rootOwner, @Nullable WSBindingProvider bindingProvider, @NotNull WSBinding binding, @NotNull Container container, Codec codec, SEIModel seiModel, Class sei) {
        this.address = address;
        this.wsdlModel = wsdlModel;
        this.rootOwner = rootOwner;
        this.bindingProvider = bindingProvider;
        this.binding = binding;
        this.container = container;
        this.codec = codec;
        this.seiModel = seiModel;
        this.sei = sei;
    }

    @NotNull
    public EndpointAddress getAddress() {
        return this.address;
    }

    @Nullable
    public WSDLPort getWsdlModel() {
        return this.wsdlModel;
    }

    @NotNull
    public WSService getService() {
        return this.rootOwner;
    }

    @Nullable
    public WSPortInfo getPortInfo() {
        if (this.bindingProvider == null) {
            return null;
        }
        return this.bindingProvider.getPortInfo();
    }

    @Nullable
    public WSBindingProvider getBindingProvider() {
        return this.bindingProvider;
    }

    @NotNull
    public WSBinding getBinding() {
        return this.binding;
    }

    @Nullable
    public SEIModel getSEIModel() {
        return this.seiModel;
    }

    @Nullable
    public Class getSEI() {
        return this.sei;
    }

    public Container getContainer() {
        return this.container;
    }

    public Tube createDumpTube(String name, PrintStream out, Tube next) {
        return new DumpTube(name, out, next);
    }

    @NotNull
    public Tube createSecurityTube(@NotNull Tube next) {
        ClientPipelineHook hook = (ClientPipelineHook) this.container.getSPI(ClientPipelineHook.class);
        if (hook != null) {
            ClientPipeAssemblerContext ctxt = new ClientPipeAssemblerContext(this.address, this.wsdlModel, this.rootOwner, this.binding, this.container);
            return PipeAdapter.adapt(hook.createSecurityPipe(ctxt, PipeAdapter.adapt(next)));
        }
        return next;
    }

    public Tube createWsaTube(Tube next) {
        if ((this.binding instanceof SOAPBinding) && AddressingVersion.isEnabled(this.binding) && this.wsdlModel != null) {
            if (AddressingVersion.fromBinding(this.binding) == AddressingVersion.MEMBER) {
                return new MemberSubmissionWsaClientTube(this.wsdlModel, this.binding, next);
            }
            return new W3CWsaClientTube(this.wsdlModel, this.binding, next);
        }
        return next;
    }

    public Tube createHandlerTube(Tube next) {
        HandlerTube cousinHandlerTube = null;
        if (this.binding instanceof SOAPBinding) {
            HandlerTube messageHandlerTube = new ClientMessageHandlerTube(this.seiModel, this.binding, this.wsdlModel, next);
            HandlerTube soapHandlerTube = new ClientSOAPHandlerTube(this.binding, messageHandlerTube, messageHandlerTube);
            cousinHandlerTube = soapHandlerTube;
            next = soapHandlerTube;
        }
        return new ClientLogicalHandlerTube(this.binding, this.seiModel, next, cousinHandlerTube);
    }

    public Tube createClientMUTube(Tube next) {
        if (this.binding instanceof SOAPBinding) {
            return new ClientMUTube(this.binding, next);
        }
        return next;
    }

    public Tube createValidationTube(Tube next) {
        if ((this.binding instanceof SOAPBinding) && this.binding.isFeatureEnabled(SchemaValidationFeature.class) && this.wsdlModel != null) {
            return new ClientSchemaValidationTube(this.binding, this.wsdlModel, next);
        }
        return next;
    }

    public Tube createTransportTube() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        return new DeferredTransportPipe(cl, this);
    }

    @NotNull
    public Codec getCodec() {
        return this.codec;
    }

    public void setCodec(@NotNull Codec codec) {
        this.codec = codec;
    }
}
