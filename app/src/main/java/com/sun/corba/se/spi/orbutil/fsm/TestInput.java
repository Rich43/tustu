package com.sun.corba.se.spi.orbutil.fsm;

/* compiled from: FSMTest.java */
/* loaded from: rt.jar:com/sun/corba/se/spi/orbutil/fsm/TestInput.class */
class TestInput {
    Input value;
    String msg;

    TestInput(Input input, String str) {
        this.value = input;
        this.msg = str;
    }

    public String toString() {
        return "Input " + ((Object) this.value) + " : " + this.msg;
    }

    public Input getInput() {
        return this.value;
    }
}
