package com.sun.corba.se.spi.orbutil.fsm;

/* compiled from: FSMTest.java */
/* loaded from: rt.jar:com/sun/corba/se/spi/orbutil/fsm/TestAction1.class */
class TestAction1 implements Action {
    private State oldState;
    private Input label;
    private State newState;

    @Override // com.sun.corba.se.spi.orbutil.fsm.Action
    public void doIt(FSM fsm, Input input) {
        System.out.println("TestAction1:");
        System.out.println("\tlabel    = " + ((Object) this.label));
        System.out.println("\toldState = " + ((Object) this.oldState));
        System.out.println("\tnewState = " + ((Object) this.newState));
        if (this.label != input) {
            throw new Error("Unexcepted Input " + ((Object) input));
        }
        if (this.oldState != fsm.getState()) {
            throw new Error("Unexpected old State " + ((Object) fsm.getState()));
        }
    }

    public TestAction1(State state, Input input, State state2) {
        this.oldState = state;
        this.newState = state2;
        this.label = input;
    }
}
