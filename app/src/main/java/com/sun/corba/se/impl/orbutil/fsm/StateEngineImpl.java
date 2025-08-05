package com.sun.corba.se.impl.orbutil.fsm;

import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.spi.orbutil.fsm.Action;
import com.sun.corba.se.spi.orbutil.fsm.ActionBase;
import com.sun.corba.se.spi.orbutil.fsm.FSM;
import com.sun.corba.se.spi.orbutil.fsm.FSMImpl;
import com.sun.corba.se.spi.orbutil.fsm.Guard;
import com.sun.corba.se.spi.orbutil.fsm.Input;
import com.sun.corba.se.spi.orbutil.fsm.State;
import com.sun.corba.se.spi.orbutil.fsm.StateEngine;
import com.sun.corba.se.spi.orbutil.fsm.StateImpl;
import java.util.Iterator;
import java.util.Set;
import org.omg.CORBA.INTERNAL;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/fsm/StateEngineImpl.class */
public class StateEngineImpl implements StateEngine {
    private static Action emptyAction = new ActionBase("Empty") { // from class: com.sun.corba.se.impl.orbutil.fsm.StateEngineImpl.1
        @Override // com.sun.corba.se.spi.orbutil.fsm.Action
        public void doIt(FSM fsm, Input input) {
        }
    };
    private boolean initializing = true;
    private Action defaultAction = new ActionBase("Invalid Transition") { // from class: com.sun.corba.se.impl.orbutil.fsm.StateEngineImpl.2
        @Override // com.sun.corba.se.spi.orbutil.fsm.Action
        public void doIt(FSM fsm, Input input) {
            throw new INTERNAL("Invalid transition attempted from " + ((Object) fsm.getState()) + " under " + ((Object) input));
        }
    };

    @Override // com.sun.corba.se.spi.orbutil.fsm.StateEngine
    public StateEngine add(State state, Input input, Guard guard, Action action, State state2) throws IllegalStateException, IllegalArgumentException {
        mustBeInitializing();
        ((StateImpl) state).addGuardedAction(input, new GuardedAction(guard, action, state2));
        return this;
    }

    @Override // com.sun.corba.se.spi.orbutil.fsm.StateEngine
    public StateEngine add(State state, Input input, Action action, State state2) throws IllegalStateException, IllegalArgumentException {
        mustBeInitializing();
        ((StateImpl) state).addGuardedAction(input, new GuardedAction(action, state2));
        return this;
    }

    @Override // com.sun.corba.se.spi.orbutil.fsm.StateEngine
    public StateEngine setDefault(State state, Action action, State state2) throws IllegalStateException, IllegalArgumentException {
        mustBeInitializing();
        StateImpl stateImpl = (StateImpl) state;
        stateImpl.setDefaultAction(action);
        stateImpl.setDefaultNextState(state2);
        return this;
    }

    @Override // com.sun.corba.se.spi.orbutil.fsm.StateEngine
    public StateEngine setDefault(State state, State state2) throws IllegalStateException, IllegalArgumentException {
        return setDefault(state, emptyAction, state2);
    }

    @Override // com.sun.corba.se.spi.orbutil.fsm.StateEngine
    public StateEngine setDefault(State state) throws IllegalStateException, IllegalArgumentException {
        return setDefault(state, state);
    }

    @Override // com.sun.corba.se.spi.orbutil.fsm.StateEngine
    public void done() throws IllegalStateException {
        mustBeInitializing();
        this.initializing = false;
    }

    @Override // com.sun.corba.se.spi.orbutil.fsm.StateEngine
    public void setDefaultAction(Action action) throws IllegalStateException {
        mustBeInitializing();
        this.defaultAction = action;
    }

    public void doIt(FSM fsm, Input input, boolean z2) {
        if (z2) {
            ORBUtility.dprint(this, "doIt enter: currentState = " + ((Object) fsm.getState()) + " in = " + ((Object) input));
        }
        try {
            innerDoIt(fsm, input, z2);
            if (z2) {
                ORBUtility.dprint(this, "doIt exit");
            }
        } catch (Throwable th) {
            if (z2) {
                ORBUtility.dprint(this, "doIt exit");
            }
            throw th;
        }
    }

    private StateImpl getDefaultNextState(StateImpl stateImpl) {
        StateImpl stateImpl2 = (StateImpl) stateImpl.getDefaultNextState();
        if (stateImpl2 == null) {
            stateImpl2 = stateImpl;
        }
        return stateImpl2;
    }

    private Action getDefaultAction(StateImpl stateImpl) {
        Action defaultAction = stateImpl.getDefaultAction();
        if (defaultAction == null) {
            defaultAction = this.defaultAction;
        }
        return defaultAction;
    }

    private void innerDoIt(FSM fsm, Input input, boolean z2) {
        boolean z3;
        StateImpl defaultNextState;
        Action defaultAction;
        if (z2) {
            ORBUtility.dprint(this, "Calling innerDoIt with input " + ((Object) input));
        }
        do {
            z3 = false;
            StateImpl stateImpl = (StateImpl) fsm.getState();
            defaultNextState = getDefaultNextState(stateImpl);
            defaultAction = getDefaultAction(stateImpl);
            if (z2) {
                ORBUtility.dprint(this, "currentState      = " + ((Object) stateImpl));
                ORBUtility.dprint(this, "in                = " + ((Object) input));
                ORBUtility.dprint(this, "default nextState = " + ((Object) defaultNextState));
                ORBUtility.dprint(this, "default action    = " + ((Object) defaultAction));
            }
            Set guardedActions = stateImpl.getGuardedActions(input);
            if (guardedActions != null) {
                Iterator it = guardedActions.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    GuardedAction guardedAction = (GuardedAction) it.next();
                    Guard.Result resultEvaluate = guardedAction.getGuard().evaluate(fsm, input);
                    if (z2) {
                        ORBUtility.dprint(this, "doIt: evaluated " + ((Object) guardedAction) + " with result " + ((Object) resultEvaluate));
                    }
                    if (resultEvaluate == Guard.Result.ENABLED) {
                        defaultNextState = (StateImpl) guardedAction.getNextState();
                        defaultAction = guardedAction.getAction();
                        if (z2) {
                            ORBUtility.dprint(this, "nextState = " + ((Object) defaultNextState));
                            ORBUtility.dprint(this, "action    = " + ((Object) defaultAction));
                        }
                    } else if (resultEvaluate == Guard.Result.DEFERED) {
                        z3 = true;
                        break;
                    }
                }
            }
        } while (z3);
        performStateTransition(fsm, input, defaultNextState, defaultAction, z2);
    }

    /* JADX WARN: Removed duplicated region for block: B:59:0x0163  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void performStateTransition(com.sun.corba.se.spi.orbutil.fsm.FSM r5, com.sun.corba.se.spi.orbutil.fsm.Input r6, com.sun.corba.se.spi.orbutil.fsm.StateImpl r7, com.sun.corba.se.spi.orbutil.fsm.Action r8, boolean r9) {
        /*
            Method dump skipped, instructions count: 382
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.corba.se.impl.orbutil.fsm.StateEngineImpl.performStateTransition(com.sun.corba.se.spi.orbutil.fsm.FSM, com.sun.corba.se.spi.orbutil.fsm.Input, com.sun.corba.se.spi.orbutil.fsm.StateImpl, com.sun.corba.se.spi.orbutil.fsm.Action, boolean):void");
    }

    @Override // com.sun.corba.se.spi.orbutil.fsm.StateEngine
    public FSM makeFSM(State state) throws IllegalStateException {
        mustNotBeInitializing();
        return new FSMImpl(this, state);
    }

    private void mustBeInitializing() throws IllegalStateException {
        if (!this.initializing) {
            throw new IllegalStateException("Invalid method call after initialization completed");
        }
    }

    private void mustNotBeInitializing() throws IllegalStateException {
        if (this.initializing) {
            throw new IllegalStateException("Invalid method call before initialization completed");
        }
    }
}
