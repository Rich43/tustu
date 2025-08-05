package com.sun.corba.se.spi.orbutil.fsm;

import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.impl.orbutil.fsm.StateEngineImpl;

/* loaded from: rt.jar:com/sun/corba/se/spi/orbutil/fsm/FSMImpl.class */
public class FSMImpl implements FSM {
    private boolean debug;
    private State state;
    private StateEngineImpl stateEngine;

    public FSMImpl(StateEngine stateEngine, State state) {
        this(stateEngine, state, false);
    }

    public FSMImpl(StateEngine stateEngine, State state, boolean z2) {
        this.state = state;
        this.stateEngine = (StateEngineImpl) stateEngine;
        this.debug = z2;
    }

    @Override // com.sun.corba.se.spi.orbutil.fsm.FSM
    public State getState() {
        return this.state;
    }

    @Override // com.sun.corba.se.spi.orbutil.fsm.FSM
    public void doIt(Input input) {
        this.stateEngine.doIt(this, input, this.debug);
    }

    public void internalSetState(State state) {
        if (this.debug) {
            ORBUtility.dprint(this, "Calling internalSetState with nextState = " + ((Object) state));
        }
        this.state = state;
        if (this.debug) {
            ORBUtility.dprint(this, "Exiting internalSetState with state = " + ((Object) this.state));
        }
    }
}
