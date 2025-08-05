package com.sun.corba.se.impl.orbutil.fsm;

import com.sun.corba.se.spi.orbutil.fsm.Action;
import com.sun.corba.se.spi.orbutil.fsm.FSM;
import com.sun.corba.se.spi.orbutil.fsm.Guard;
import com.sun.corba.se.spi.orbutil.fsm.GuardBase;
import com.sun.corba.se.spi.orbutil.fsm.Input;
import com.sun.corba.se.spi.orbutil.fsm.State;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/fsm/GuardedAction.class */
public class GuardedAction {
    private static Guard trueGuard = new GuardBase("true") { // from class: com.sun.corba.se.impl.orbutil.fsm.GuardedAction.1
        @Override // com.sun.corba.se.spi.orbutil.fsm.Guard
        public Guard.Result evaluate(FSM fsm, Input input) {
            return Guard.Result.ENABLED;
        }
    };
    private Guard guard;
    private Action action;
    private State nextState;

    public GuardedAction(Action action, State state) {
        this.guard = trueGuard;
        this.action = action;
        this.nextState = state;
    }

    public GuardedAction(Guard guard, Action action, State state) {
        this.guard = guard;
        this.action = action;
        this.nextState = state;
    }

    public String toString() {
        return "GuardedAction[action=" + ((Object) this.action) + " guard=" + ((Object) this.guard) + " nextState=" + ((Object) this.nextState) + "]";
    }

    public Action getAction() {
        return this.action;
    }

    public Guard getGuard() {
        return this.guard;
    }

    public State getNextState() {
        return this.nextState;
    }
}
