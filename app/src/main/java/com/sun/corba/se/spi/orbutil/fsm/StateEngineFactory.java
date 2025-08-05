package com.sun.corba.se.spi.orbutil.fsm;

import com.sun.corba.se.impl.orbutil.fsm.StateEngineImpl;

/* loaded from: rt.jar:com/sun/corba/se/spi/orbutil/fsm/StateEngineFactory.class */
public class StateEngineFactory {
    private StateEngineFactory() {
    }

    public static StateEngine create() {
        return new StateEngineImpl();
    }
}
