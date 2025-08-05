package com.sun.xml.internal.ws.server.sei;

import com.oracle.webservices.internal.api.databinding.JavaCallInfo;
import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.model.SEIModel;
import com.sun.xml.internal.ws.api.pipe.NextAction;
import com.sun.xml.internal.ws.model.AbstractSEIModelImpl;
import com.sun.xml.internal.ws.wsdl.DispatchException;
import java.lang.reflect.InvocationTargetException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/SEIInvokerTube.class */
public class SEIInvokerTube extends com.sun.xml.internal.ws.server.InvokerTube {
    private final WSBinding binding;
    private final AbstractSEIModelImpl model;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SEIInvokerTube.class.desiredAssertionStatus();
    }

    public SEIInvokerTube(AbstractSEIModelImpl model, com.sun.xml.internal.ws.api.server.Invoker invoker, WSBinding binding) {
        super(invoker);
        this.binding = binding;
        this.model = model;
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Tube
    @NotNull
    public NextAction processRequest(@NotNull Packet req) {
        JavaCallInfo call = this.model.getDatabinding().deserializeRequest(req);
        if (call.getException() == null) {
            try {
                if (req.getMessage().isOneWay(this.model.getPort()) && req.transportBackChannel != null) {
                    req.transportBackChannel.close();
                }
                Object ret = getInvoker(req).invoke(req, call.getMethod(), call.getParameters());
                call.setReturnValue(ret);
            } catch (InvocationTargetException e2) {
                call.setException(e2);
            } catch (Exception e3) {
                call.setException(e3);
            }
        } else if (call.getException() instanceof DispatchException) {
            DispatchException e4 = (DispatchException) call.getException();
            return doReturnWith(req.createServerResponse(e4.fault, this.model.getPort(), (SEIModel) null, this.binding));
        }
        Packet res = req.relateServerResponse((Packet) this.model.getDatabinding().serializeResponse(call), req.endpoint.getPort(), this.model, req.endpoint.getBinding());
        if ($assertionsDisabled || res != null) {
            return doReturnWith(res);
        }
        throw new AssertionError();
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Tube
    @NotNull
    public NextAction processResponse(@NotNull Packet response) {
        return doReturnWith(response);
    }

    @Override // com.sun.xml.internal.ws.api.pipe.Tube
    @NotNull
    public NextAction processException(@NotNull Throwable t2) {
        return doThrow(t2);
    }
}
