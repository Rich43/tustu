package sun.misc;

/* loaded from: rt.jar:sun/misc/NativeSignalHandler.class */
final class NativeSignalHandler implements SignalHandler {
    private final long handler;

    private static native void handle0(int i2, long j2);

    long getHandler() {
        return this.handler;
    }

    NativeSignalHandler(long j2) {
        this.handler = j2;
    }

    @Override // sun.misc.SignalHandler
    public void handle(Signal signal) {
        handle0(signal.getNumber(), this.handler);
    }
}
