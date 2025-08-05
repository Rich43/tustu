package com.sun.corba.se.spi.orbutil.fsm;

/* loaded from: rt.jar:com/sun/corba/se/spi/orbutil/fsm/Guard.class */
public interface Guard {
    Result evaluate(FSM fsm, Input input);

    /* loaded from: rt.jar:com/sun/corba/se/spi/orbutil/fsm/Guard$Complement.class */
    public static final class Complement extends GuardBase {
        private Guard guard;

        public Complement(GuardBase guardBase) {
            super("not(" + guardBase.getName() + ")");
            this.guard = guardBase;
        }

        @Override // com.sun.corba.se.spi.orbutil.fsm.Guard
        public Result evaluate(FSM fsm, Input input) {
            return this.guard.evaluate(fsm, input).complement();
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/spi/orbutil/fsm/Guard$Result.class */
    public static final class Result {
        private String name;
        public static final Result ENABLED = new Result("ENABLED");
        public static final Result DISABLED = new Result("DISABLED");
        public static final Result DEFERED = new Result("DEFERED");

        private Result(String str) {
            this.name = str;
        }

        public static Result convert(boolean z2) {
            return z2 ? ENABLED : DISABLED;
        }

        public Result complement() {
            if (this == ENABLED) {
                return DISABLED;
            }
            if (this == DISABLED) {
                return ENABLED;
            }
            return DEFERED;
        }

        public String toString() {
            return "Guard.Result[" + this.name + "]";
        }
    }
}
