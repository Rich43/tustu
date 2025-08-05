package com.sun.xml.internal.ws.client.sei;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import com.sun.xml.internal.ws.api.client.WSPortInfo;
import com.sun.xml.internal.ws.api.databinding.Databinding;
import com.sun.xml.internal.ws.api.message.Header;
import com.sun.xml.internal.ws.api.message.Headers;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.MEP;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
import com.sun.xml.internal.ws.api.pipe.Fiber;
import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.api.server.Container;
import com.sun.xml.internal.ws.api.server.ContainerResolver;
import com.sun.xml.internal.ws.binding.BindingImpl;
import com.sun.xml.internal.ws.client.AsyncResponseImpl;
import com.sun.xml.internal.ws.client.RequestContext;
import com.sun.xml.internal.ws.client.ResponseContextReceiver;
import com.sun.xml.internal.ws.client.Stub;
import com.sun.xml.internal.ws.client.WSServiceDelegate;
import com.sun.xml.internal.ws.model.JavaMethodImpl;
import com.sun.xml.internal.ws.model.SOAPSEIModel;
import com.sun.xml.internal.ws.wsdl.OperationDispatcher;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/SEIStub.class */
public final class SEIStub extends Stub implements InvocationHandler {
    Databinding databinding;
    public final SOAPSEIModel seiModel;
    public final SOAPVersion soapVersion;
    private final Map<Method, MethodHandler> methodHandlers;

    @Deprecated
    public SEIStub(WSServiceDelegate owner, BindingImpl binding, SOAPSEIModel seiModel, Tube master, WSEndpointReference epr) {
        super(owner, master, binding, seiModel.getPort(), seiModel.getPort().getAddress(), epr);
        this.methodHandlers = new HashMap();
        this.seiModel = seiModel;
        this.soapVersion = binding.getSOAPVersion();
        this.databinding = seiModel.getDatabinding();
        initMethodHandlers();
    }

    public SEIStub(WSPortInfo portInfo, BindingImpl binding, SOAPSEIModel seiModel, WSEndpointReference epr) {
        super(portInfo, binding, seiModel.getPort().getAddress(), epr);
        this.methodHandlers = new HashMap();
        this.seiModel = seiModel;
        this.soapVersion = binding.getSOAPVersion();
        this.databinding = seiModel.getDatabinding();
        initMethodHandlers();
    }

    private void initMethodHandlers() {
        Map<WSDLBoundOperation, JavaMethodImpl> syncs = new HashMap<>();
        for (JavaMethodImpl m2 : this.seiModel.getJavaMethods()) {
            if (!m2.getMEP().isAsync) {
                SyncMethodHandler handler = new SyncMethodHandler(this, m2);
                syncs.put(m2.getOperation(), m2);
                this.methodHandlers.put(m2.getMethod(), handler);
            }
        }
        for (JavaMethodImpl jm : this.seiModel.getJavaMethods()) {
            syncs.get(jm.getOperation());
            if (jm.getMEP() == MEP.ASYNC_CALLBACK) {
                Method m3 = jm.getMethod();
                CallbackMethodHandler handler2 = new CallbackMethodHandler(this, m3, m3.getParameterTypes().length - 1);
                this.methodHandlers.put(m3, handler2);
            }
            if (jm.getMEP() == MEP.ASYNC_POLL) {
                Method m4 = jm.getMethod();
                PollingMethodHandler handler3 = new PollingMethodHandler(this, m4);
                this.methodHandlers.put(m4, handler3);
            }
        }
    }

    @Override // com.sun.xml.internal.ws.client.Stub
    @Nullable
    public OperationDispatcher getOperationDispatcher() {
        if (this.operationDispatcher == null && this.wsdlPort != null) {
            this.operationDispatcher = new OperationDispatcher(this.wsdlPort, this.binding, this.seiModel);
        }
        return this.operationDispatcher;
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        validateInputs(proxy, method);
        Container old = ContainerResolver.getDefault().enterContainer(this.owner.getContainer());
        try {
            MethodHandler handler = this.methodHandlers.get(method);
            if (handler != null) {
                Object objInvoke = handler.invoke(proxy, args);
                ContainerResolver.getDefault().exitContainer(old);
                return objInvoke;
            }
            try {
                try {
                    try {
                        Object objInvoke2 = method.invoke(this, args);
                        ContainerResolver.getDefault().exitContainer(old);
                        return objInvoke2;
                    } catch (InvocationTargetException e2) {
                        throw e2.getCause();
                    }
                } catch (IllegalArgumentException e3) {
                    throw new AssertionError(e3);
                }
            } catch (IllegalAccessException e4) {
                throw new AssertionError(e4);
            }
        } catch (Throwable th) {
            ContainerResolver.getDefault().exitContainer(old);
            throw th;
        }
    }

    private void validateInputs(Object proxy, Method method) {
        if (proxy == null || !Proxy.isProxyClass(proxy.getClass())) {
            throw new IllegalStateException("Passed object is not proxy!");
        }
        Class<?> declaringClass = method.getDeclaringClass();
        if (method == null || declaringClass == null || Modifier.isStatic(method.getModifiers())) {
            throw new IllegalStateException("Invoking static method is not allowed!");
        }
    }

    public final Packet doProcess(Packet request, RequestContext rc, ResponseContextReceiver receiver) {
        return super.process(request, rc, receiver);
    }

    public final void doProcessAsync(AsyncResponseImpl<?> receiver, Packet request, RequestContext rc, Fiber.CompletionCallback callback) {
        super.processAsync(receiver, request, rc, callback);
    }

    @Override // com.sun.xml.internal.ws.client.Stub
    @NotNull
    protected final QName getPortName() {
        return this.wsdlPort.getName();
    }

    @Override // com.sun.xml.internal.ws.developer.WSBindingProvider
    public void setOutboundHeaders(Object... headers) {
        if (headers == null) {
            throw new IllegalArgumentException();
        }
        Header[] hl = new Header[headers.length];
        for (int i2 = 0; i2 < hl.length; i2++) {
            if (headers[i2] == null) {
                throw new IllegalArgumentException();
            }
            hl[i2] = Headers.create(this.seiModel.getBindingContext(), headers[i2]);
        }
        super.setOutboundHeaders(hl);
    }
}
