package com.sun.corba.se.impl.transport;

import com.sun.corba.se.impl.encoding.BufferManagerReadStream;
import com.sun.corba.se.impl.encoding.CDRInputObject;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.impl.protocol.giopmsgheaders.LocateReplyOrReplyMessage;
import com.sun.corba.se.pept.encoding.InputObject;
import com.sun.corba.se.pept.protocol.MessageMediator;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
import com.sun.corba.se.spi.transport.CorbaConnection;
import com.sun.corba.se.spi.transport.CorbaResponseWaitingRoom;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.SystemException;

/* loaded from: rt.jar:com/sun/corba/se/impl/transport/CorbaResponseWaitingRoomImpl.class */
public class CorbaResponseWaitingRoomImpl implements CorbaResponseWaitingRoom {
    private ORB orb;
    private ORBUtilSystemException wrapper;
    private CorbaConnection connection;
    private final Map<Integer, OutCallDesc> out_calls = Collections.synchronizedMap(new HashMap());

    /* loaded from: rt.jar:com/sun/corba/se/impl/transport/CorbaResponseWaitingRoomImpl$OutCallDesc.class */
    static final class OutCallDesc {
        Object done = new Object();
        Thread thread;
        MessageMediator messageMediator;
        SystemException exception;
        InputObject inputObject;

        OutCallDesc() {
        }
    }

    public CorbaResponseWaitingRoomImpl(ORB orb, CorbaConnection corbaConnection) {
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_TRANSPORT);
        this.connection = corbaConnection;
    }

    @Override // com.sun.corba.se.pept.transport.ResponseWaitingRoom
    public void registerWaiter(MessageMediator messageMediator) {
        CorbaMessageMediator corbaMessageMediator = (CorbaMessageMediator) messageMediator;
        if (this.orb.transportDebugFlag) {
            dprint(".registerWaiter: " + opAndId(corbaMessageMediator));
        }
        Integer requestIdInteger = corbaMessageMediator.getRequestIdInteger();
        OutCallDesc outCallDesc = new OutCallDesc();
        outCallDesc.thread = Thread.currentThread();
        outCallDesc.messageMediator = corbaMessageMediator;
        this.out_calls.put(requestIdInteger, outCallDesc);
    }

    @Override // com.sun.corba.se.pept.transport.ResponseWaitingRoom
    public void unregisterWaiter(MessageMediator messageMediator) {
        CorbaMessageMediator corbaMessageMediator = (CorbaMessageMediator) messageMediator;
        if (this.orb.transportDebugFlag) {
            dprint(".unregisterWaiter: " + opAndId(corbaMessageMediator));
        }
        this.out_calls.remove(corbaMessageMediator.getRequestIdInteger());
    }

    @Override // com.sun.corba.se.pept.transport.ResponseWaitingRoom
    public InputObject waitForResponse(MessageMediator messageMediator) {
        InputObject inputObject;
        CorbaMessageMediator corbaMessageMediator = (CorbaMessageMediator) messageMediator;
        try {
            if (this.orb.transportDebugFlag) {
                dprint(".waitForResponse->: " + opAndId(corbaMessageMediator));
            }
            Integer requestIdInteger = corbaMessageMediator.getRequestIdInteger();
            if (corbaMessageMediator.isOneWay()) {
                if (this.orb.transportDebugFlag) {
                    dprint(".waitForResponse: one way - not waiting: " + opAndId(corbaMessageMediator));
                }
                return null;
            }
            OutCallDesc outCallDesc = this.out_calls.get(requestIdInteger);
            if (outCallDesc == null) {
                throw this.wrapper.nullOutCall(CompletionStatus.COMPLETED_MAYBE);
            }
            synchronized (outCallDesc.done) {
                while (outCallDesc.inputObject == null && outCallDesc.exception == null) {
                    try {
                        if (this.orb.transportDebugFlag) {
                            dprint(".waitForResponse: waiting: " + opAndId(corbaMessageMediator));
                        }
                        outCallDesc.done.wait();
                    } catch (InterruptedException e2) {
                    }
                }
                if (outCallDesc.exception != null) {
                    if (this.orb.transportDebugFlag) {
                        dprint(".waitForResponse: exception: " + opAndId(corbaMessageMediator));
                    }
                    throw outCallDesc.exception;
                }
                inputObject = outCallDesc.inputObject;
            }
            if (inputObject != null) {
                ((CDRInputObject) inputObject).unmarshalHeader();
            }
            if (this.orb.transportDebugFlag) {
                dprint(".waitForResponse<-: " + opAndId(corbaMessageMediator));
            }
            return inputObject;
        } finally {
            if (this.orb.transportDebugFlag) {
                dprint(".waitForResponse<-: " + opAndId(corbaMessageMediator));
            }
        }
    }

    @Override // com.sun.corba.se.pept.transport.ResponseWaitingRoom
    public void responseReceived(InputObject inputObject) {
        CDRInputObject cDRInputObject = (CDRInputObject) inputObject;
        LocateReplyOrReplyMessage locateReplyOrReplyMessage = (LocateReplyOrReplyMessage) cDRInputObject.getMessageHeader();
        Integer num = new Integer(locateReplyOrReplyMessage.getRequestId());
        OutCallDesc outCallDesc = this.out_calls.get(num);
        if (this.orb.transportDebugFlag) {
            dprint(".responseReceived: id/" + ((Object) num) + ": " + ((Object) locateReplyOrReplyMessage));
        }
        if (outCallDesc == null) {
            if (this.orb.transportDebugFlag) {
                dprint(".responseReceived: id/" + ((Object) num) + ": no waiter: " + ((Object) locateReplyOrReplyMessage));
                return;
            }
            return;
        }
        synchronized (outCallDesc.done) {
            CorbaMessageMediator corbaMessageMediator = (CorbaMessageMediator) outCallDesc.messageMediator;
            if (this.orb.transportDebugFlag) {
                dprint(".responseReceived: " + opAndId(corbaMessageMediator) + ": notifying waiters");
            }
            corbaMessageMediator.setReplyHeader(locateReplyOrReplyMessage);
            corbaMessageMediator.setInputObject(inputObject);
            cDRInputObject.setMessageMediator(corbaMessageMediator);
            outCallDesc.inputObject = inputObject;
            outCallDesc.done.notify();
        }
    }

    @Override // com.sun.corba.se.pept.transport.ResponseWaitingRoom
    public int numberRegistered() {
        return this.out_calls.size();
    }

    @Override // com.sun.corba.se.spi.transport.CorbaResponseWaitingRoom
    public void signalExceptionToAllWaiters(SystemException systemException) {
        if (this.orb.transportDebugFlag) {
            dprint(".signalExceptionToAllWaiters: " + ((Object) systemException));
        }
        synchronized (this.out_calls) {
            if (this.orb.transportDebugFlag) {
                dprint(".signalExceptionToAllWaiters: out_calls size :" + this.out_calls.size());
            }
            for (OutCallDesc outCallDesc : this.out_calls.values()) {
                if (this.orb.transportDebugFlag) {
                    dprint(".signalExceptionToAllWaiters: signaling " + ((Object) outCallDesc));
                }
                synchronized (outCallDesc.done) {
                    try {
                        CorbaMessageMediator corbaMessageMediator = (CorbaMessageMediator) outCallDesc.messageMediator;
                        CDRInputObject cDRInputObject = (CDRInputObject) corbaMessageMediator.getInputObject();
                        if (cDRInputObject != null) {
                            ((BufferManagerReadStream) cDRInputObject.getBufferManager()).cancelProcessing(corbaMessageMediator.getRequestId());
                        }
                        outCallDesc.inputObject = null;
                        outCallDesc.exception = systemException;
                        outCallDesc.done.notifyAll();
                    } catch (Exception e2) {
                        outCallDesc.inputObject = null;
                        outCallDesc.exception = systemException;
                        outCallDesc.done.notifyAll();
                    } catch (Throwable th) {
                        outCallDesc.inputObject = null;
                        outCallDesc.exception = systemException;
                        outCallDesc.done.notifyAll();
                        throw th;
                    }
                }
            }
        }
    }

    @Override // com.sun.corba.se.spi.transport.CorbaResponseWaitingRoom
    public MessageMediator getMessageMediator(int i2) {
        OutCallDesc outCallDesc = this.out_calls.get(new Integer(i2));
        if (outCallDesc == null) {
            return null;
        }
        return outCallDesc.messageMediator;
    }

    protected void dprint(String str) {
        ORBUtility.dprint("CorbaResponseWaitingRoomImpl", str);
    }

    protected String opAndId(CorbaMessageMediator corbaMessageMediator) {
        return ORBUtility.operationNameAndRequestId(corbaMessageMediator);
    }
}
