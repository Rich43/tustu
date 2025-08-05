package java.lang;

import sun.misc.Signal;
import sun.misc.SignalHandler;

/* loaded from: rt.jar:java/lang/Terminator.class */
class Terminator {
    private static SignalHandler handler = null;

    Terminator() {
    }

    static void setup() {
        if (handler != null) {
            return;
        }
        SignalHandler signalHandler = new SignalHandler() { // from class: java.lang.Terminator.1
            @Override // sun.misc.SignalHandler
            public void handle(Signal signal) {
                Shutdown.exit(signal.getNumber() + 128);
            }
        };
        handler = signalHandler;
        try {
            Signal.handle(new Signal("INT"), signalHandler);
        } catch (IllegalArgumentException e2) {
        }
        try {
            Signal.handle(new Signal("TERM"), signalHandler);
        } catch (IllegalArgumentException e3) {
        }
    }

    static void teardown() {
    }
}
