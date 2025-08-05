package com.sun.corba.se.spi.orbutil.fsm;

import com.sun.corba.se.spi.orbutil.fsm.Guard;

/* compiled from: FSMTest.java */
/* loaded from: rt.jar:com/sun/corba/se/spi/orbutil/fsm/NegateGuard.class */
class NegateGuard implements Guard {
    Guard guard;

    public NegateGuard(Guard guard) {
        this.guard = guard;
    }

    @Override // com.sun.corba.se.spi.orbutil.fsm.Guard
    public Guard.Result evaluate(FSM fsm, Input input) {
        return this.guard.evaluate(fsm, input).complement();
    }
}
