package com.sun.xml.internal.ws.db;

import com.oracle.webservices.internal.api.databinding.JavaCallInfo;
import com.oracle.webservices.internal.api.message.MessageContext;
import com.sun.xml.internal.ws.api.databinding.ClientCallBridge;
import com.sun.xml.internal.ws.api.databinding.Databinding;
import com.sun.xml.internal.ws.api.databinding.DatabindingConfig;
import com.sun.xml.internal.ws.api.databinding.EndpointCallBridge;
import com.sun.xml.internal.ws.api.databinding.WSDLGenInfo;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.MessageContextFactory;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.MEP;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.model.WSDLOperationMapping;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.api.pipe.Codec;
import com.sun.xml.internal.ws.api.pipe.ContentType;
import com.sun.xml.internal.ws.binding.BindingImpl;
import com.sun.xml.internal.ws.client.sei.StubAsyncHandler;
import com.sun.xml.internal.ws.client.sei.StubHandler;
import com.sun.xml.internal.ws.model.AbstractSEIModelImpl;
import com.sun.xml.internal.ws.model.JavaMethodImpl;
import com.sun.xml.internal.ws.model.RuntimeModeler;
import com.sun.xml.internal.ws.server.sei.TieHandler;
import com.sun.xml.internal.ws.wsdl.ActionBasedOperationSignature;
import com.sun.xml.internal.ws.wsdl.DispatchException;
import com.sun.xml.internal.ws.wsdl.OperationDispatcher;
import com.sun.xml.internal.ws.wsdl.writer.WSDLGenerator;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.xml.ws.WebServiceFeature;

/* loaded from: rt.jar:com/sun/xml/internal/ws/db/DatabindingImpl.class */
public final class DatabindingImpl implements Databinding {
    AbstractSEIModelImpl seiModel;
    Map<Method, StubHandler> stubHandlers;
    Map<JavaMethodImpl, TieHandler> wsdlOpMap = new HashMap();
    Map<Method, TieHandler> tieHandlers = new HashMap();
    OperationDispatcher operationDispatcher;
    OperationDispatcher operationDispatcherNoWsdl;
    boolean clientConfig;
    Codec codec;
    MessageContextFactory packetFactory;

    public DatabindingImpl(DatabindingProviderImpl p2, DatabindingConfig config) {
        this.clientConfig = false;
        this.packetFactory = null;
        RuntimeModeler modeler = new RuntimeModeler(config);
        modeler.setClassLoader(config.getClassLoader());
        this.seiModel = modeler.buildRuntimeModel();
        WSDLPort wsdlport = config.getWsdlPort();
        this.packetFactory = new MessageContextFactory(this.seiModel.getWSBinding().getFeatures());
        this.clientConfig = isClientConfig(config);
        if (this.clientConfig) {
            initStubHandlers();
        }
        this.seiModel.setDatabinding(this);
        if (wsdlport != null) {
            freeze(wsdlport);
        }
        if (this.operationDispatcher == null) {
            this.operationDispatcherNoWsdl = new OperationDispatcher(null, this.seiModel.getWSBinding(), this.seiModel);
        }
        for (JavaMethodImpl jm : this.seiModel.getJavaMethods()) {
            if (!jm.isAsync()) {
                TieHandler th = new TieHandler(jm, this.seiModel.getWSBinding(), this.packetFactory);
                this.wsdlOpMap.put(jm, th);
                this.tieHandlers.put(th.getMethod(), th);
            }
        }
    }

    private boolean isClientConfig(DatabindingConfig config) {
        if (config.getContractClass() != null && config.getContractClass().isInterface()) {
            return config.getEndpointClass() == null || config.getEndpointClass().isInterface();
        }
        return false;
    }

    public void freeze(WSDLPort port) {
        if (this.clientConfig) {
            return;
        }
        synchronized (this) {
            if (this.operationDispatcher == null) {
                this.operationDispatcher = port == null ? null : new OperationDispatcher(port, this.seiModel.getWSBinding(), this.seiModel);
            }
        }
    }

    public SEIModel getModel() {
        return this.seiModel;
    }

    private void initStubHandlers() {
        this.stubHandlers = new HashMap();
        Map<ActionBasedOperationSignature, JavaMethodImpl> syncs = new HashMap<>();
        for (JavaMethodImpl m2 : this.seiModel.getJavaMethods()) {
            if (!m2.getMEP().isAsync) {
                StubHandler handler = new StubHandler(m2, this.packetFactory);
                syncs.put(m2.getOperationSignature(), m2);
                this.stubHandlers.put(m2.getMethod(), handler);
            }
        }
        for (JavaMethodImpl jm : this.seiModel.getJavaMethods()) {
            JavaMethodImpl sync = syncs.get(jm.getOperationSignature());
            if (jm.getMEP() == MEP.ASYNC_CALLBACK || jm.getMEP() == MEP.ASYNC_POLL) {
                Method m3 = jm.getMethod();
                StubAsyncHandler handler2 = new StubAsyncHandler(jm, sync, this.packetFactory);
                this.stubHandlers.put(m3, handler2);
            }
        }
    }

    JavaMethodImpl resolveJavaMethod(Packet req) throws DispatchException {
        WSDLOperationMapping wSDLOperationMapping;
        WSDLOperationMapping m2 = req.getWSDLOperationMapping();
        if (m2 == null) {
            synchronized (this) {
                if (this.operationDispatcher != null) {
                    wSDLOperationMapping = this.operationDispatcher.getWSDLOperationMapping(req);
                } else {
                    wSDLOperationMapping = this.operationDispatcherNoWsdl.getWSDLOperationMapping(req);
                }
                m2 = wSDLOperationMapping;
            }
        }
        return (JavaMethodImpl) m2.getJavaMethod();
    }

    public JavaCallInfo deserializeRequest(Packet req) {
        com.sun.xml.internal.ws.api.databinding.JavaCallInfo call = new com.sun.xml.internal.ws.api.databinding.JavaCallInfo();
        try {
            JavaMethodImpl wsdlOp = resolveJavaMethod(req);
            TieHandler tie = this.wsdlOpMap.get(wsdlOp);
            call.setMethod(tie.getMethod());
            Object[] args = tie.readRequest(req.getMessage());
            call.setParameters(args);
        } catch (DispatchException e2) {
            call.setException(e2);
        }
        return call;
    }

    public JavaCallInfo deserializeResponse(Packet res, JavaCallInfo call) {
        StubHandler stubHandler = this.stubHandlers.get(call.getMethod());
        try {
            return stubHandler.readResponse(res, call);
        } catch (Throwable e2) {
            call.setException(e2);
            return call;
        }
    }

    public WebServiceFeature[] getFeatures() {
        return null;
    }

    @Override // com.oracle.webservices.internal.api.databinding.Databinding
    public Packet serializeRequest(JavaCallInfo call) {
        StubHandler stubHandler = this.stubHandlers.get(call.getMethod());
        Packet p2 = stubHandler.createRequestPacket(call);
        p2.setState(Packet.State.ClientRequest);
        return p2;
    }

    @Override // com.oracle.webservices.internal.api.databinding.Databinding
    public Packet serializeResponse(JavaCallInfo call) {
        TieHandler th;
        Method method = call.getMethod();
        Message message = null;
        if (method != null && (th = this.tieHandlers.get(method)) != null) {
            return th.serializeResponse(call);
        }
        if (call.getException() instanceof DispatchException) {
            message = ((DispatchException) call.getException()).fault;
        }
        Packet p2 = (Packet) this.packetFactory.createContext(message);
        p2.setState(Packet.State.ServerResponse);
        return p2;
    }

    @Override // com.sun.xml.internal.ws.api.databinding.Databinding
    public ClientCallBridge getClientBridge(Method method) {
        return this.stubHandlers.get(method);
    }

    @Override // com.sun.xml.internal.ws.api.databinding.Databinding
    public void generateWSDL(WSDLGenInfo info) {
        WSDLGenerator wsdlGen = new WSDLGenerator(this.seiModel, info.getWsdlResolver(), this.seiModel.getWSBinding(), info.getContainer(), this.seiModel.getEndpointClass(), info.isInlineSchemas(), info.isSecureXmlProcessingDisabled(), info.getExtensions());
        wsdlGen.doGeneration();
    }

    @Override // com.sun.xml.internal.ws.api.databinding.Databinding
    public EndpointCallBridge getEndpointBridge(Packet req) throws DispatchException {
        JavaMethodImpl wsdlOp = resolveJavaMethod(req);
        return this.wsdlOpMap.get(wsdlOp);
    }

    Codec getCodec() {
        if (this.codec == null) {
            this.codec = ((BindingImpl) this.seiModel.getWSBinding()).createCodec();
        }
        return this.codec;
    }

    @Override // com.sun.xml.internal.ws.api.databinding.Databinding
    public ContentType encode(Packet packet, OutputStream out) throws IOException {
        return getCodec().encode(packet, out);
    }

    @Override // com.sun.xml.internal.ws.api.databinding.Databinding
    public void decode(InputStream in, String ct, Packet p2) throws IOException {
        getCodec().decode(in, ct, p2);
    }

    @Override // com.oracle.webservices.internal.api.databinding.Databinding
    public JavaCallInfo createJavaCallInfo(Method method, Object[] args) {
        return new com.sun.xml.internal.ws.api.databinding.JavaCallInfo(method, args);
    }

    @Override // com.oracle.webservices.internal.api.databinding.Databinding
    public JavaCallInfo deserializeResponse(MessageContext message, JavaCallInfo call) {
        return deserializeResponse((Packet) message, call);
    }

    @Override // com.oracle.webservices.internal.api.databinding.Databinding
    public JavaCallInfo deserializeRequest(MessageContext message) {
        return deserializeRequest((Packet) message);
    }

    @Override // com.sun.xml.internal.ws.api.databinding.Databinding
    public MessageContextFactory getMessageContextFactory() {
        return this.packetFactory;
    }
}
