package com.sun.corba.se.impl.interceptors;

import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.spi.oa.ObjectAdapter;
import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA.SystemException;
import org.omg.PortableInterceptor.ClientRequestInterceptor;
import org.omg.PortableInterceptor.ForwardRequest;
import org.omg.PortableInterceptor.IORInterceptor;
import org.omg.PortableInterceptor.IORInterceptor_3_0;
import org.omg.PortableInterceptor.ObjectReferenceTemplate;
import org.omg.PortableInterceptor.ServerRequestInterceptor;

/* loaded from: rt.jar:com/sun/corba/se/impl/interceptors/InterceptorInvoker.class */
public class InterceptorInvoker {
    private ORB orb;
    private InterceptorList interceptorList;
    private boolean enabled;
    private PICurrent current;

    InterceptorInvoker(ORB orb, InterceptorList interceptorList, PICurrent pICurrent) {
        this.enabled = false;
        this.orb = orb;
        this.interceptorList = interceptorList;
        this.enabled = false;
        this.current = pICurrent;
    }

    void setEnabled(boolean z2) {
        this.enabled = z2;
    }

    void objectAdapterCreated(ObjectAdapter objectAdapter) {
        if (this.enabled) {
            IORInfoImpl iORInfoImpl = new IORInfoImpl(objectAdapter);
            IORInterceptor[] iORInterceptorArr = (IORInterceptor[]) this.interceptorList.getInterceptors(2);
            int length = iORInterceptorArr.length;
            for (int i2 = length - 1; i2 >= 0; i2--) {
                try {
                    iORInterceptorArr[i2].establish_components(iORInfoImpl);
                } catch (Exception e2) {
                }
            }
            iORInfoImpl.makeStateEstablished();
            for (int i3 = length - 1; i3 >= 0; i3--) {
                IORInterceptor iORInterceptor = iORInterceptorArr[i3];
                if (iORInterceptor instanceof IORInterceptor_3_0) {
                    ((IORInterceptor_3_0) iORInterceptor).components_established(iORInfoImpl);
                }
            }
            iORInfoImpl.makeStateDone();
        }
    }

    void adapterManagerStateChanged(int i2, short s2) {
        if (this.enabled) {
            IORInterceptor[] iORInterceptorArr = (IORInterceptor[]) this.interceptorList.getInterceptors(2);
            for (int length = iORInterceptorArr.length - 1; length >= 0; length--) {
                try {
                    IORInterceptor iORInterceptor = iORInterceptorArr[length];
                    if (iORInterceptor instanceof IORInterceptor_3_0) {
                        ((IORInterceptor_3_0) iORInterceptor).adapter_manager_state_changed(i2, s2);
                    }
                } catch (Exception e2) {
                }
            }
        }
    }

    void adapterStateChanged(ObjectReferenceTemplate[] objectReferenceTemplateArr, short s2) {
        if (this.enabled) {
            IORInterceptor[] iORInterceptorArr = (IORInterceptor[]) this.interceptorList.getInterceptors(2);
            for (int length = iORInterceptorArr.length - 1; length >= 0; length--) {
                try {
                    IORInterceptor iORInterceptor = iORInterceptorArr[length];
                    if (iORInterceptor instanceof IORInterceptor_3_0) {
                        ((IORInterceptor_3_0) iORInterceptor).adapter_state_changed(objectReferenceTemplateArr, s2);
                    }
                } catch (Exception e2) {
                }
            }
        }
    }

    void invokeClientInterceptorStartingPoint(ClientRequestInfoImpl clientRequestInfoImpl) {
        if (this.enabled) {
            try {
                this.current.pushSlotTable();
                clientRequestInfoImpl.setPICurrentPushed(true);
                clientRequestInfoImpl.setCurrentExecutionPoint(0);
                ClientRequestInterceptor[] clientRequestInterceptorArr = (ClientRequestInterceptor[]) this.interceptorList.getInterceptors(0);
                int length = clientRequestInterceptorArr.length;
                int i2 = length;
                boolean z2 = true;
                for (int i3 = 0; z2 && i3 < length; i3++) {
                    try {
                        clientRequestInterceptorArr[i3].send_request(clientRequestInfoImpl);
                    } catch (SystemException e2) {
                        i2 = i3;
                        clientRequestInfoImpl.setEndingPointCall(1);
                        clientRequestInfoImpl.setReplyStatus((short) 1);
                        clientRequestInfoImpl.setException(e2);
                        z2 = false;
                    } catch (ForwardRequest e3) {
                        i2 = i3;
                        clientRequestInfoImpl.setForwardRequest(e3);
                        clientRequestInfoImpl.setEndingPointCall(2);
                        clientRequestInfoImpl.setReplyStatus((short) 3);
                        updateClientRequestDispatcherForward(clientRequestInfoImpl);
                        z2 = false;
                    }
                }
                clientRequestInfoImpl.setFlowStackIndex(i2);
                this.current.resetSlotTable();
            } catch (Throwable th) {
                this.current.resetSlotTable();
                throw th;
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    void invokeClientInterceptorEndingPoint(ClientRequestInfoImpl clientRequestInfoImpl) {
        if (this.enabled) {
            try {
                clientRequestInfoImpl.setCurrentExecutionPoint(2);
                ClientRequestInterceptor[] clientRequestInterceptorArr = (ClientRequestInterceptor[]) this.interceptorList.getInterceptors(0);
                int flowStackIndex = clientRequestInfoImpl.getFlowStackIndex();
                int endingPointCall = clientRequestInfoImpl.getEndingPointCall();
                if (endingPointCall == 0 && clientRequestInfoImpl.getIsOneWay()) {
                    endingPointCall = 2;
                    clientRequestInfoImpl.setEndingPointCall(2);
                }
                for (int i2 = flowStackIndex - 1; i2 >= 0; i2--) {
                    try {
                    } catch (SystemException e2) {
                        endingPointCall = 1;
                        clientRequestInfoImpl.setEndingPointCall(1);
                        clientRequestInfoImpl.setReplyStatus((short) 1);
                        clientRequestInfoImpl.setException(e2);
                    } catch (ForwardRequest e3) {
                        endingPointCall = 2;
                        clientRequestInfoImpl.setEndingPointCall(2);
                        clientRequestInfoImpl.setReplyStatus((short) 3);
                        clientRequestInfoImpl.setForwardRequest(e3);
                        updateClientRequestDispatcherForward(clientRequestInfoImpl);
                    }
                    switch (endingPointCall) {
                        case 0:
                            clientRequestInterceptorArr[i2].receive_reply(clientRequestInfoImpl);
                        case 1:
                            clientRequestInterceptorArr[i2].receive_exception(clientRequestInfoImpl);
                        case 2:
                            clientRequestInterceptorArr[i2].receive_other(clientRequestInfoImpl);
                        default:
                    }
                }
            } finally {
                if (clientRequestInfoImpl != null && clientRequestInfoImpl.isPICurrentPushed()) {
                    this.current.popSlotTable();
                }
            }
        }
    }

    void invokeServerInterceptorStartingPoint(ServerRequestInfoImpl serverRequestInfoImpl) {
        if (this.enabled) {
            try {
                this.current.pushSlotTable();
                serverRequestInfoImpl.setSlotTable(this.current.getSlotTable());
                this.current.pushSlotTable();
                serverRequestInfoImpl.setCurrentExecutionPoint(0);
                ServerRequestInterceptor[] serverRequestInterceptorArr = (ServerRequestInterceptor[]) this.interceptorList.getInterceptors(1);
                int length = serverRequestInterceptorArr.length;
                int i2 = length;
                boolean z2 = true;
                for (int i3 = 0; z2 && i3 < length; i3++) {
                    try {
                        serverRequestInterceptorArr[i3].receive_request_service_contexts(serverRequestInfoImpl);
                    } catch (SystemException e2) {
                        i2 = i3;
                        serverRequestInfoImpl.setException(e2);
                        serverRequestInfoImpl.setIntermediatePointCall(1);
                        serverRequestInfoImpl.setEndingPointCall(1);
                        serverRequestInfoImpl.setReplyStatus((short) 1);
                        z2 = false;
                    } catch (ForwardRequest e3) {
                        i2 = i3;
                        serverRequestInfoImpl.setForwardRequest(e3);
                        serverRequestInfoImpl.setIntermediatePointCall(1);
                        serverRequestInfoImpl.setEndingPointCall(2);
                        serverRequestInfoImpl.setReplyStatus((short) 3);
                        z2 = false;
                    }
                }
                serverRequestInfoImpl.setFlowStackIndex(i2);
                this.current.popSlotTable();
            } catch (Throwable th) {
                this.current.popSlotTable();
                throw th;
            }
        }
    }

    void invokeServerInterceptorIntermediatePoint(ServerRequestInfoImpl serverRequestInfoImpl) {
        int intermediatePointCall = serverRequestInfoImpl.getIntermediatePointCall();
        if (this.enabled && intermediatePointCall != 1) {
            serverRequestInfoImpl.setCurrentExecutionPoint(1);
            for (ServerRequestInterceptor serverRequestInterceptor : (ServerRequestInterceptor[]) this.interceptorList.getInterceptors(1)) {
                try {
                    serverRequestInterceptor.receive_request(serverRequestInfoImpl);
                } catch (SystemException e2) {
                    serverRequestInfoImpl.setException(e2);
                    serverRequestInfoImpl.setEndingPointCall(1);
                    serverRequestInfoImpl.setReplyStatus((short) 1);
                    return;
                } catch (ForwardRequest e3) {
                    serverRequestInfoImpl.setForwardRequest(e3);
                    serverRequestInfoImpl.setEndingPointCall(2);
                    serverRequestInfoImpl.setReplyStatus((short) 3);
                    return;
                }
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    void invokeServerInterceptorEndingPoint(ServerRequestInfoImpl serverRequestInfoImpl) {
        if (this.enabled) {
            try {
                ServerRequestInterceptor[] serverRequestInterceptorArr = (ServerRequestInterceptor[]) this.interceptorList.getInterceptors(1);
                int flowStackIndex = serverRequestInfoImpl.getFlowStackIndex();
                int endingPointCall = serverRequestInfoImpl.getEndingPointCall();
                for (int i2 = flowStackIndex - 1; i2 >= 0; i2--) {
                    try {
                    } catch (SystemException e2) {
                        endingPointCall = 1;
                        serverRequestInfoImpl.setEndingPointCall(1);
                        serverRequestInfoImpl.setException(e2);
                        serverRequestInfoImpl.setReplyStatus((short) 1);
                    } catch (ForwardRequest e3) {
                        endingPointCall = 2;
                        serverRequestInfoImpl.setEndingPointCall(2);
                        serverRequestInfoImpl.setForwardRequest(e3);
                        serverRequestInfoImpl.setReplyStatus((short) 3);
                        serverRequestInfoImpl.setForwardRequestRaisedInEnding();
                    }
                    switch (endingPointCall) {
                        case 0:
                            serverRequestInterceptorArr[i2].send_reply(serverRequestInfoImpl);
                        case 1:
                            serverRequestInterceptorArr[i2].send_exception(serverRequestInfoImpl);
                        case 2:
                            serverRequestInterceptorArr[i2].send_other(serverRequestInfoImpl);
                        default:
                    }
                }
                serverRequestInfoImpl.setAlreadyExecuted(true);
                this.current.popSlotTable();
            } catch (Throwable th) {
                this.current.popSlotTable();
                throw th;
            }
        }
    }

    private void updateClientRequestDispatcherForward(ClientRequestInfoImpl clientRequestInfoImpl) {
        ForwardRequest forwardRequestException = clientRequestInfoImpl.getForwardRequestException();
        if (forwardRequestException != null) {
            clientRequestInfoImpl.setLocatedIOR(ORBUtility.getIOR(forwardRequestException.forward));
        }
    }
}
