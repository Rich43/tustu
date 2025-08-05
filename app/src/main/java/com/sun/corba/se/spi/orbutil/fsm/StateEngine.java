package com.sun.corba.se.spi.orbutil.fsm;

/* loaded from: rt.jar:com/sun/corba/se/spi/orbutil/fsm/StateEngine.class */
public interface StateEngine {
    StateEngine add(State state, Input input, Guard guard, Action action, State state2) throws IllegalStateException;

    StateEngine add(State state, Input input, Action action, State state2) throws IllegalStateException;

    StateEngine setDefault(State state, Action action, State state2) throws IllegalStateException;

    StateEngine setDefault(State state, State state2) throws IllegalStateException;

    StateEngine setDefault(State state) throws IllegalStateException;

    void setDefaultAction(Action action) throws IllegalStateException;

    void done() throws IllegalStateException;

    FSM makeFSM(State state) throws IllegalStateException;
}
