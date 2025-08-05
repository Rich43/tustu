package sun.misc;

import java.util.Hashtable;

/* loaded from: rt.jar:sun/misc/Signal.class */
public final class Signal {
    private static Hashtable<Signal, SignalHandler> handlers = new Hashtable<>(4);
    private static Hashtable<Integer, Signal> signals = new Hashtable<>(4);
    private int number;
    private String name;

    private static native int findSignal(String str);

    private static native long handle0(int i2, long j2);

    private static native void raise0(int i2);

    public int getNumber() {
        return this.number;
    }

    public String getName() {
        return this.name;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Signal)) {
            return false;
        }
        Signal signal = (Signal) obj;
        return this.name.equals(signal.name) && this.number == signal.number;
    }

    public int hashCode() {
        return this.number;
    }

    public String toString() {
        return "SIG" + this.name;
    }

    public Signal(String str) {
        this.number = findSignal(str);
        this.name = str;
        if (this.number < 0) {
            throw new IllegalArgumentException("Unknown signal: " + str);
        }
    }

    public static synchronized SignalHandler handle(Signal signal, SignalHandler signalHandler) throws IllegalArgumentException {
        long handler = signalHandler instanceof NativeSignalHandler ? ((NativeSignalHandler) signalHandler).getHandler() : 2L;
        long jHandle0 = handle0(signal.number, handler);
        if (jHandle0 == -1) {
            throw new IllegalArgumentException("Signal already used by VM or OS: " + ((Object) signal));
        }
        signals.put(Integer.valueOf(signal.number), signal);
        synchronized (handlers) {
            SignalHandler signalHandler2 = handlers.get(signal);
            handlers.remove(signal);
            if (handler == 2) {
                handlers.put(signal, signalHandler);
            }
            if (jHandle0 == 0) {
                return SignalHandler.SIG_DFL;
            }
            if (jHandle0 == 1) {
                return SignalHandler.SIG_IGN;
            }
            if (jHandle0 == 2) {
                return signalHandler2;
            }
            return new NativeSignalHandler(jHandle0);
        }
    }

    public static void raise(Signal signal) throws IllegalArgumentException {
        if (handlers.get(signal) == null) {
            throw new IllegalArgumentException("Unhandled signal: " + ((Object) signal));
        }
        raise0(signal.number);
    }

    private static void dispatch(int i2) {
        final Signal signal = signals.get(Integer.valueOf(i2));
        final SignalHandler signalHandler = handlers.get(signal);
        Runnable runnable = new Runnable() { // from class: sun.misc.Signal.1
            @Override // java.lang.Runnable
            public void run() {
                signalHandler.handle(signal);
            }
        };
        if (signalHandler != null) {
            new Thread(runnable, ((Object) signal) + " handler").start();
        }
    }
}
