package com.sun.corba.se.spi.orbutil.fsm;

/* loaded from: rt.jar:com/sun/corba/se/spi/orbutil/fsm/FSM.class */
public interface FSM {
    State getState();

    void doIt(Input input);
}
