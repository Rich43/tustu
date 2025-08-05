package com.sun.corba.se.impl.oa.poa;

import com.sun.corba.se.impl.orbutil.concurrent.CondVar;
import com.sun.corba.se.spi.orbutil.fsm.Action;
import com.sun.corba.se.spi.orbutil.fsm.ActionBase;
import com.sun.corba.se.spi.orbutil.fsm.FSM;
import com.sun.corba.se.spi.orbutil.fsm.FSMImpl;
import com.sun.corba.se.spi.orbutil.fsm.Guard;
import com.sun.corba.se.spi.orbutil.fsm.GuardBase;
import com.sun.corba.se.spi.orbutil.fsm.Input;
import com.sun.corba.se.spi.orbutil.fsm.InputImpl;
import com.sun.corba.se.spi.orbutil.fsm.State;
import com.sun.corba.se.spi.orbutil.fsm.StateEngine;
import com.sun.corba.se.spi.orbutil.fsm.StateEngineFactory;
import com.sun.corba.se.spi.orbutil.fsm.StateImpl;
import org.omg.PortableServer.POAPackage.ObjectAlreadyActive;

/* loaded from: rt.jar:com/sun/corba/se/impl/oa/poa/AOMEntry.class */
public class AOMEntry extends FSMImpl {
    private final Thread[] etherealizer;
    private final int[] counter;
    private final CondVar wait;
    final POAImpl poa;
    public static final State INVALID = new StateImpl("Invalid");
    public static final State INCARN = new StateImpl("Incarnating") { // from class: com.sun.corba.se.impl.oa.poa.AOMEntry.1
        @Override // com.sun.corba.se.spi.orbutil.fsm.StateImpl, com.sun.corba.se.spi.orbutil.fsm.State
        public void postAction(FSM fsm) {
            ((AOMEntry) fsm).wait.broadcast();
        }
    };
    public static final State VALID = new StateImpl("Valid");
    public static final State ETHP = new StateImpl("EtherealizePending");
    public static final State ETH = new StateImpl("Etherealizing") { // from class: com.sun.corba.se.impl.oa.poa.AOMEntry.2
        @Override // com.sun.corba.se.spi.orbutil.fsm.StateImpl, com.sun.corba.se.spi.orbutil.fsm.State
        public void preAction(FSM fsm) {
            Thread thread = ((AOMEntry) fsm).etherealizer[0];
            if (thread != null) {
                thread.start();
            }
        }

        @Override // com.sun.corba.se.spi.orbutil.fsm.StateImpl, com.sun.corba.se.spi.orbutil.fsm.State
        public void postAction(FSM fsm) {
            ((AOMEntry) fsm).wait.broadcast();
        }
    };
    public static final State DESTROYED = new StateImpl("Destroyed");
    static final Input START_ETH = new InputImpl("startEtherealize");
    static final Input ETH_DONE = new InputImpl("etherealizeDone");
    static final Input INC_DONE = new InputImpl("incarnateDone");
    static final Input INC_FAIL = new InputImpl("incarnateFailure");
    static final Input ACTIVATE = new InputImpl("activateObject");
    static final Input ENTER = new InputImpl("enter");
    static final Input EXIT = new InputImpl("exit");
    private static Action incrementAction = new ActionBase("increment") { // from class: com.sun.corba.se.impl.oa.poa.AOMEntry.3
        @Override // com.sun.corba.se.spi.orbutil.fsm.Action
        public void doIt(FSM fsm, Input input) {
            int[] iArr = ((AOMEntry) fsm).counter;
            iArr[0] = iArr[0] + 1;
        }
    };
    private static Action decrementAction = new ActionBase("decrement") { // from class: com.sun.corba.se.impl.oa.poa.AOMEntry.4
        @Override // com.sun.corba.se.spi.orbutil.fsm.Action
        public void doIt(FSM fsm, Input input) {
            AOMEntry aOMEntry = (AOMEntry) fsm;
            if (aOMEntry.counter[0] > 0) {
                int[] iArr = aOMEntry.counter;
                iArr[0] = iArr[0] - 1;
                return;
            }
            throw aOMEntry.poa.lifecycleWrapper().aomEntryDecZero();
        }
    };
    private static Action throwIllegalStateExceptionAction = new ActionBase("throwIllegalStateException") { // from class: com.sun.corba.se.impl.oa.poa.AOMEntry.5
        @Override // com.sun.corba.se.spi.orbutil.fsm.Action
        public void doIt(FSM fsm, Input input) {
            throw new IllegalStateException("No transitions allowed from the DESTROYED state");
        }
    };
    private static Action oaaAction = new ActionBase("throwObjectAlreadyActive") { // from class: com.sun.corba.se.impl.oa.poa.AOMEntry.6
        @Override // com.sun.corba.se.spi.orbutil.fsm.Action
        public void doIt(FSM fsm, Input input) {
            throw new RuntimeException(new ObjectAlreadyActive());
        }
    };
    private static Guard waitGuard = new GuardBase("wait") { // from class: com.sun.corba.se.impl.oa.poa.AOMEntry.7
        @Override // com.sun.corba.se.spi.orbutil.fsm.Guard
        public Guard.Result evaluate(FSM fsm, Input input) {
            try {
                ((AOMEntry) fsm).wait.await();
            } catch (InterruptedException e2) {
            }
            return Guard.Result.DEFERED;
        }
    };
    private static GuardBase greaterZeroGuard = new CounterGuard(0);
    private static Guard zeroGuard = new Guard.Complement(greaterZeroGuard);
    private static GuardBase greaterOneGuard = new CounterGuard(1);
    private static Guard oneGuard = new Guard.Complement(greaterOneGuard);
    private static StateEngine engine = StateEngineFactory.create();

    static {
        engine.add(INVALID, ENTER, incrementAction, INCARN);
        engine.add(INVALID, ACTIVATE, null, VALID);
        engine.setDefault(INVALID);
        engine.add(INCARN, ENTER, waitGuard, null, INCARN);
        engine.add(INCARN, EXIT, null, INCARN);
        engine.add(INCARN, START_ETH, waitGuard, null, INCARN);
        engine.add(INCARN, INC_DONE, null, VALID);
        engine.add(INCARN, INC_FAIL, decrementAction, INVALID);
        engine.add(INCARN, ACTIVATE, oaaAction, INCARN);
        engine.add(VALID, ENTER, incrementAction, VALID);
        engine.add(VALID, EXIT, decrementAction, VALID);
        engine.add(VALID, START_ETH, greaterZeroGuard, null, ETHP);
        engine.add(VALID, START_ETH, zeroGuard, null, ETH);
        engine.add(VALID, ACTIVATE, oaaAction, VALID);
        engine.add(ETHP, ENTER, waitGuard, null, ETHP);
        engine.add(ETHP, START_ETH, null, ETHP);
        engine.add(ETHP, EXIT, greaterOneGuard, decrementAction, ETHP);
        engine.add(ETHP, EXIT, oneGuard, decrementAction, ETH);
        engine.add(ETHP, ACTIVATE, oaaAction, ETHP);
        engine.add(ETH, START_ETH, null, ETH);
        engine.add(ETH, ETH_DONE, null, DESTROYED);
        engine.add(ETH, ACTIVATE, oaaAction, ETH);
        engine.add(ETH, ENTER, waitGuard, null, ETH);
        engine.setDefault(DESTROYED, throwIllegalStateExceptionAction, DESTROYED);
        engine.done();
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/oa/poa/AOMEntry$CounterGuard.class */
    private static class CounterGuard extends GuardBase {
        private int value;

        public CounterGuard(int i2) {
            super("counter>" + i2);
            this.value = i2;
        }

        @Override // com.sun.corba.se.spi.orbutil.fsm.Guard
        public Guard.Result evaluate(FSM fsm, Input input) {
            return Guard.Result.convert(((AOMEntry) fsm).counter[0] > this.value);
        }
    }

    public AOMEntry(POAImpl pOAImpl) {
        super(engine, INVALID, pOAImpl.getORB().poaFSMDebugFlag);
        this.poa = pOAImpl;
        this.etherealizer = new Thread[1];
        this.etherealizer[0] = null;
        this.counter = new int[1];
        this.counter[0] = 0;
        this.wait = new CondVar(pOAImpl.poaMutex, pOAImpl.getORB().poaConcurrencyDebugFlag);
    }

    public void startEtherealize(Thread thread) {
        this.etherealizer[0] = thread;
        doIt(START_ETH);
    }

    public void etherealizeComplete() {
        doIt(ETH_DONE);
    }

    public void incarnateComplete() {
        doIt(INC_DONE);
    }

    public void incarnateFailure() {
        doIt(INC_FAIL);
    }

    public void activateObject() throws ObjectAlreadyActive {
        try {
            doIt(ACTIVATE);
        } catch (RuntimeException e2) {
            Throwable cause = e2.getCause();
            if (cause instanceof ObjectAlreadyActive) {
                throw ((ObjectAlreadyActive) cause);
            }
            throw e2;
        }
    }

    public void enter() {
        doIt(ENTER);
    }

    public void exit() {
        doIt(EXIT);
    }
}
