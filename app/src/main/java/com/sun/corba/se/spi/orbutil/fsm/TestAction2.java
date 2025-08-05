package com.sun.corba.se.spi.orbutil.fsm;

/* compiled from: FSMTest.java */
/* loaded from: rt.jar:com/sun/corba/se/spi/orbutil/fsm/TestAction2.class */
class TestAction2 implements Action {
    private State oldState;
    private State newState;

    @Override // com.sun.corba.se.spi.orbutil.fsm.Action
    public void doIt(FSM fsm, Input input) {
        System.out.println("TestAction2:");
        System.out.println("\toldState = " + ((Object) this.oldState));
        System.out.println("\tnewState = " + ((Object) this.newState));
        System.out.println("\tinput    = " + ((Object) input));
        if (this.oldState != fsm.getState()) {
            throw new Error("Unexpected old State " + ((Object) fsm.getState()));
        }
    }

    public TestAction2(State state, State state2) {
        this.oldState = state;
        this.newState = state2;
    }
}
