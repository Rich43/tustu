package com.sun.corba.se.spi.orbutil.fsm;

import com.sun.corba.se.impl.orbutil.fsm.GuardedAction;
import com.sun.corba.se.impl.orbutil.fsm.NameBase;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* loaded from: rt.jar:com/sun/corba/se/spi/orbutil/fsm/StateImpl.class */
public class StateImpl extends NameBase implements State {
    private Action defaultAction;
    private State defaultNextState;
    private Map inputToGuardedActions;

    public StateImpl(String str) {
        super(str);
        this.defaultAction = null;
        this.inputToGuardedActions = new HashMap();
    }

    public void preAction(FSM fsm) {
    }

    public void postAction(FSM fsm) {
    }

    public State getDefaultNextState() {
        return this.defaultNextState;
    }

    public void setDefaultNextState(State state) {
        this.defaultNextState = state;
    }

    public Action getDefaultAction() {
        return this.defaultAction;
    }

    public void setDefaultAction(Action action) {
        this.defaultAction = action;
    }

    public void addGuardedAction(Input input, GuardedAction guardedAction) {
        Set hashSet = (Set) this.inputToGuardedActions.get(input);
        if (hashSet == null) {
            hashSet = new HashSet();
            this.inputToGuardedActions.put(input, hashSet);
        }
        hashSet.add(guardedAction);
    }

    public Set getGuardedActions(Input input) {
        return (Set) this.inputToGuardedActions.get(input);
    }
}
