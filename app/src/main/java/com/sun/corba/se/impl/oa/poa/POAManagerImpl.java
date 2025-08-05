package com.sun.corba.se.impl.oa.poa;

import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.spi.protocol.PIHandler;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.LocalObject;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAManager;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAManagerPackage.State;

/* loaded from: rt.jar:com/sun/corba/se/impl/oa/poa/POAManagerImpl.class */
public class POAManagerImpl extends LocalObject implements POAManager {
    private final POAFactory factory;
    private PIHandler pihandler;
    private State state;
    private Set poas = new HashSet(4);
    private int nInvocations = 0;
    private int nWaiters = 0;
    private int myId;
    private boolean debug;
    private boolean explicitStateChange;

    private String stateToString(State state) {
        switch (state.value()) {
            case 0:
                return "State[HOLDING]";
            case 1:
                return "State[ACTIVE]";
            case 2:
                return "State[DISCARDING]";
            case 3:
                return "State[INACTIVE]";
            default:
                return "State[UNKNOWN]";
        }
    }

    public String toString() {
        return "POAManagerImpl[myId=" + this.myId + " state=" + stateToString(this.state) + " nInvocations=" + this.nInvocations + " nWaiters=" + this.nWaiters + "]";
    }

    POAFactory getFactory() {
        return this.factory;
    }

    PIHandler getPIHandler() {
        return this.pihandler;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void countedWait() {
        try {
            if (this.debug) {
                ORBUtility.dprint(this, "Calling countedWait on POAManager " + ((Object) this) + " nWaiters=" + this.nWaiters);
            }
            this.nWaiters++;
            wait();
            this.nWaiters--;
            if (this.debug) {
                ORBUtility.dprint(this, "Exiting countedWait on POAManager " + ((Object) this) + " nWaiters=" + this.nWaiters);
            }
        } catch (InterruptedException e2) {
            this.nWaiters--;
            if (this.debug) {
                ORBUtility.dprint(this, "Exiting countedWait on POAManager " + ((Object) this) + " nWaiters=" + this.nWaiters);
            }
        } catch (Throwable th) {
            this.nWaiters--;
            if (this.debug) {
                ORBUtility.dprint(this, "Exiting countedWait on POAManager " + ((Object) this) + " nWaiters=" + this.nWaiters);
            }
            throw th;
        }
    }

    private void notifyWaiters() {
        if (this.debug) {
            ORBUtility.dprint(this, "Calling notifyWaiters on POAManager " + ((Object) this) + " nWaiters=" + this.nWaiters);
        }
        if (this.nWaiters > 0) {
            notifyAll();
        }
    }

    public int getManagerId() {
        return this.myId;
    }

    POAManagerImpl(POAFactory pOAFactory, PIHandler pIHandler) {
        this.myId = 0;
        this.factory = pOAFactory;
        pOAFactory.addPoaManager(this);
        this.pihandler = pIHandler;
        this.myId = pOAFactory.newPOAManagerId();
        this.state = State.HOLDING;
        this.debug = pOAFactory.getORB().poaDebugFlag;
        this.explicitStateChange = false;
        if (this.debug) {
            ORBUtility.dprint(this, "Creating POAManagerImpl " + ((Object) this));
        }
    }

    synchronized void addPOA(POA poa) {
        if (this.state.value() == 3) {
            throw this.factory.getWrapper().addPoaInactive(CompletionStatus.COMPLETED_NO);
        }
        this.poas.add(poa);
    }

    synchronized void removePOA(POA poa) {
        this.poas.remove(poa);
        if (this.poas.isEmpty()) {
            this.factory.removePoaManager(this);
        }
    }

    public short getORTState() {
        switch (this.state.value()) {
            case 0:
                return (short) 0;
            case 1:
                return (short) 1;
            case 2:
                return (short) 2;
            case 3:
                return (short) 3;
            default:
                return (short) 4;
        }
    }

    @Override // org.omg.PortableServer.POAManagerOperations
    public synchronized void activate() throws AdapterInactive {
        this.explicitStateChange = true;
        if (this.debug) {
            ORBUtility.dprint(this, "Calling activate on POAManager " + ((Object) this));
        }
        try {
            if (this.state.value() == 3) {
                throw new AdapterInactive();
            }
            this.state = State.ACTIVE;
            this.pihandler.adapterManagerStateChanged(this.myId, getORTState());
            notifyWaiters();
        } finally {
            if (this.debug) {
                ORBUtility.dprint(this, "Exiting activate on POAManager " + ((Object) this));
            }
        }
    }

    @Override // org.omg.PortableServer.POAManagerOperations
    public synchronized void hold_requests(boolean z2) throws AdapterInactive {
        this.explicitStateChange = true;
        if (this.debug) {
            ORBUtility.dprint(this, "Calling hold_requests on POAManager " + ((Object) this));
        }
        try {
            if (this.state.value() == 3) {
                throw new AdapterInactive();
            }
            this.state = State.HOLDING;
            this.pihandler.adapterManagerStateChanged(this.myId, getORTState());
            notifyWaiters();
            if (z2) {
                while (this.state.value() == 0 && this.nInvocations > 0) {
                    countedWait();
                }
            }
        } finally {
            if (this.debug) {
                ORBUtility.dprint(this, "Exiting hold_requests on POAManager " + ((Object) this));
            }
        }
    }

    @Override // org.omg.PortableServer.POAManagerOperations
    public synchronized void discard_requests(boolean z2) throws AdapterInactive {
        this.explicitStateChange = true;
        if (this.debug) {
            ORBUtility.dprint(this, "Calling hold_requests on POAManager " + ((Object) this));
        }
        try {
            if (this.state.value() == 3) {
                throw new AdapterInactive();
            }
            this.state = State.DISCARDING;
            this.pihandler.adapterManagerStateChanged(this.myId, getORTState());
            notifyWaiters();
            if (z2) {
                while (this.state.value() == 2 && this.nInvocations > 0) {
                    countedWait();
                }
            }
        } finally {
            if (this.debug) {
                ORBUtility.dprint(this, "Exiting hold_requests on POAManager " + ((Object) this));
            }
        }
    }

    @Override // org.omg.PortableServer.POAManagerOperations
    public void deactivate(boolean z2, boolean z3) throws AdapterInactive {
        this.explicitStateChange = true;
        try {
            synchronized (this) {
                if (this.debug) {
                    ORBUtility.dprint(this, "Calling deactivate on POAManager " + ((Object) this));
                }
                if (this.state.value() == 3) {
                    throw new AdapterInactive();
                }
                this.state = State.INACTIVE;
                this.pihandler.adapterManagerStateChanged(this.myId, getORTState());
                notifyWaiters();
            }
            POAManagerDeactivator pOAManagerDeactivator = new POAManagerDeactivator(this, z2, this.debug);
            if (z3) {
                pOAManagerDeactivator.run();
            } else {
                new Thread(pOAManagerDeactivator).start();
            }
            synchronized (this) {
                if (this.debug) {
                    ORBUtility.dprint(this, "Exiting deactivate on POAManager " + ((Object) this));
                }
            }
        } catch (Throwable th) {
            synchronized (this) {
                if (this.debug) {
                    ORBUtility.dprint(this, "Exiting deactivate on POAManager " + ((Object) this));
                }
                throw th;
            }
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/oa/poa/POAManagerImpl$POAManagerDeactivator.class */
    private class POAManagerDeactivator implements Runnable {
        private boolean etherealize_objects;
        private POAManagerImpl pmi;
        private boolean debug;

        POAManagerDeactivator(POAManagerImpl pOAManagerImpl, boolean z2, boolean z3) {
            this.etherealize_objects = z2;
            this.pmi = pOAManagerImpl;
            this.debug = z3;
        }

        @Override // java.lang.Runnable
        public void run() {
            Iterator it;
            try {
                synchronized (this.pmi) {
                    if (this.debug) {
                        ORBUtility.dprint(this, "Calling run with etherealize_objects=" + this.etherealize_objects + " pmi=" + ((Object) this.pmi));
                    }
                    while (this.pmi.nInvocations > 0) {
                        POAManagerImpl.this.countedWait();
                    }
                }
                if (this.etherealize_objects) {
                    synchronized (this.pmi) {
                        if (this.debug) {
                            ORBUtility.dprint(this, "run: Preparing to etherealize with pmi=" + ((Object) this.pmi));
                        }
                        it = new HashSet(this.pmi.poas).iterator();
                    }
                    while (it.hasNext()) {
                        ((POAImpl) it.next()).etherealizeAll();
                    }
                    synchronized (this.pmi) {
                        if (this.debug) {
                            ORBUtility.dprint(this, "run: removing POAManager and clearing poas with pmi=" + ((Object) this.pmi));
                        }
                        POAManagerImpl.this.factory.removePoaManager(this.pmi);
                        POAManagerImpl.this.poas.clear();
                    }
                }
                if (this.debug) {
                    synchronized (this.pmi) {
                        ORBUtility.dprint(this, "Exiting run");
                    }
                }
            } catch (Throwable th) {
                if (this.debug) {
                    synchronized (this.pmi) {
                        ORBUtility.dprint(this, "Exiting run");
                    }
                }
                throw th;
            }
        }
    }

    @Override // org.omg.PortableServer.POAManagerOperations
    public State get_state() {
        return this.state;
    }

    synchronized void checkIfActive() {
        try {
            if (this.debug) {
                ORBUtility.dprint(this, "Calling checkIfActive for POAManagerImpl " + ((Object) this));
            }
            checkState();
        } finally {
            if (this.debug) {
                ORBUtility.dprint(this, "Exiting checkIfActive for POAManagerImpl " + ((Object) this));
            }
        }
    }

    private void checkState() {
        while (this.state.value() != 1) {
            switch (this.state.value()) {
                case 0:
                    while (this.state.value() == 0) {
                        countedWait();
                    }
                    break;
                case 2:
                    throw this.factory.getWrapper().poaDiscarding();
                case 3:
                    throw this.factory.getWrapper().poaInactive();
            }
        }
    }

    synchronized void enter() {
        try {
            if (this.debug) {
                ORBUtility.dprint(this, "Calling enter for POAManagerImpl " + ((Object) this));
            }
            checkState();
            this.nInvocations++;
        } finally {
            if (this.debug) {
                ORBUtility.dprint(this, "Exiting enter for POAManagerImpl " + ((Object) this));
            }
        }
    }

    synchronized void exit() {
        try {
            if (this.debug) {
                ORBUtility.dprint(this, "Calling exit for POAManagerImpl " + ((Object) this));
            }
            this.nInvocations--;
            if (this.nInvocations == 0) {
                notifyWaiters();
            }
        } finally {
            if (this.debug) {
                ORBUtility.dprint(this, "Exiting exit for POAManagerImpl " + ((Object) this));
            }
        }
    }

    public synchronized void implicitActivation() {
        if (!this.explicitStateChange) {
            try {
                activate();
            } catch (AdapterInactive e2) {
            }
        }
    }
}
