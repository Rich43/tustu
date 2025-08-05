package com.sun.corba.se.spi.orbutil.fsm;

/* compiled from: FSMTest.java */
/* loaded from: rt.jar:com/sun/corba/se/spi/orbutil/fsm/MyFSM.class */
class MyFSM extends FSMImpl {
    public int counter;

    public MyFSM(StateEngine stateEngine) {
        super(stateEngine, FSMTest.STATE1);
        this.counter = 0;
    }
}
