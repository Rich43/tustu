package com.sun.corba.se.spi.orbutil.fsm;

/* compiled from: FSMTest.java */
/* loaded from: rt.jar:com/sun/corba/se/spi/orbutil/fsm/TestAction3.class */
class TestAction3 implements Action {
    private State oldState;
    private Input label;

    @Override // com.sun.corba.se.spi.orbutil.fsm.Action
    public void doIt(FSM fsm, Input input) {
        System.out.println("TestAction1:");
        System.out.println("\tlabel    = " + ((Object) this.label));
        System.out.println("\toldState = " + ((Object) this.oldState));
        if (this.label != input) {
            throw new Error("Unexcepted Input " + ((Object) input));
        }
    }

    public TestAction3(State state, Input input) {
        this.oldState = state;
        this.label = input;
    }
}
