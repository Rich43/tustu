package com.sun.corba.se.spi.orbutil.fsm;

import com.sun.corba.se.spi.orbutil.fsm.Guard;
import com.sun.org.apache.xml.internal.serializer.SerializerConstants;

/* loaded from: rt.jar:com/sun/corba/se/spi/orbutil/fsm/FSMTest.class */
public class FSMTest {
    public static final State STATE1 = new StateImpl("1");
    public static final State STATE2 = new StateImpl("2");
    public static final State STATE3 = new StateImpl("3");
    public static final State STATE4 = new StateImpl("4");
    public static final Input INPUT1 = new InputImpl("1");
    public static final Input INPUT2 = new InputImpl("2");
    public static final Input INPUT3 = new InputImpl("3");
    public static final Input INPUT4 = new InputImpl("4");
    private Guard counterGuard = new Guard() { // from class: com.sun.corba.se.spi.orbutil.fsm.FSMTest.1
        @Override // com.sun.corba.se.spi.orbutil.fsm.Guard
        public Guard.Result evaluate(FSM fsm, Input input) {
            return Guard.Result.convert(((MyFSM) fsm).counter < 3);
        }
    };

    private static void add1(StateEngine stateEngine, State state, Input input, State state2) throws IllegalStateException {
        stateEngine.add(state, input, new TestAction1(state, input, state2), state2);
    }

    private static void add2(StateEngine stateEngine, State state, State state2) throws IllegalStateException {
        stateEngine.setDefault(state, new TestAction2(state, state2), state2);
    }

    public static void main(String[] strArr) throws IllegalStateException {
        TestAction3 testAction3 = new TestAction3(STATE3, INPUT1);
        StateEngine stateEngineCreate = StateEngineFactory.create();
        add1(stateEngineCreate, STATE1, INPUT1, STATE1);
        add2(stateEngineCreate, STATE1, STATE2);
        add1(stateEngineCreate, STATE2, INPUT1, STATE2);
        add1(stateEngineCreate, STATE2, INPUT2, STATE2);
        add1(stateEngineCreate, STATE2, INPUT3, STATE1);
        add1(stateEngineCreate, STATE2, INPUT4, STATE3);
        stateEngineCreate.add(STATE3, INPUT1, testAction3, STATE3);
        stateEngineCreate.add(STATE3, INPUT1, testAction3, STATE4);
        add1(stateEngineCreate, STATE3, INPUT2, STATE1);
        add1(stateEngineCreate, STATE3, INPUT3, STATE2);
        add1(stateEngineCreate, STATE3, INPUT4, STATE2);
        MyFSM myFSM = new MyFSM(stateEngineCreate);
        TestInput testInput = new TestInput(INPUT1, SerializerConstants.XMLVERSION11);
        TestInput testInput2 = new TestInput(INPUT1, "1.2");
        new TestInput(INPUT2, "2.1");
        TestInput testInput3 = new TestInput(INPUT2, "2.2");
        TestInput testInput4 = new TestInput(INPUT3, "3.1");
        TestInput testInput5 = new TestInput(INPUT3, "3.2");
        TestInput testInput6 = new TestInput(INPUT3, "3.3");
        TestInput testInput7 = new TestInput(INPUT4, "4.1");
        myFSM.doIt(testInput.getInput());
        myFSM.doIt(testInput2.getInput());
        myFSM.doIt(testInput7.getInput());
        myFSM.doIt(testInput.getInput());
        myFSM.doIt(testInput3.getInput());
        myFSM.doIt(testInput4.getInput());
        myFSM.doIt(testInput6.getInput());
        myFSM.doIt(testInput7.getInput());
        myFSM.doIt(testInput7.getInput());
        myFSM.doIt(testInput7.getInput());
        myFSM.doIt(testInput3.getInput());
        myFSM.doIt(testInput5.getInput());
        myFSM.doIt(testInput7.getInput());
        myFSM.doIt(testInput.getInput());
        myFSM.doIt(testInput2.getInput());
        myFSM.doIt(testInput.getInput());
        myFSM.doIt(testInput.getInput());
        myFSM.doIt(testInput.getInput());
        myFSM.doIt(testInput.getInput());
        myFSM.doIt(testInput.getInput());
    }
}
