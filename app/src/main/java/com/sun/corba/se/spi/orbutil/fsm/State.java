package com.sun.corba.se.spi.orbutil.fsm;

/* loaded from: rt.jar:com/sun/corba/se/spi/orbutil/fsm/State.class */
public interface State {
    void preAction(FSM fsm);

    void postAction(FSM fsm);
}
