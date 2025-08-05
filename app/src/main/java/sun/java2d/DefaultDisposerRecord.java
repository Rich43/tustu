package sun.java2d;

/* loaded from: rt.jar:sun/java2d/DefaultDisposerRecord.class */
public class DefaultDisposerRecord implements DisposerRecord {
    private long dataPointer;
    private long disposerMethodPointer;

    public static native void invokeNativeDispose(long j2, long j3);

    public DefaultDisposerRecord(long j2, long j3) {
        this.disposerMethodPointer = j2;
        this.dataPointer = j3;
    }

    @Override // sun.java2d.DisposerRecord
    public void dispose() {
        invokeNativeDispose(this.disposerMethodPointer, this.dataPointer);
    }

    public long getDataPointer() {
        return this.dataPointer;
    }

    public long getDisposerMethodPointer() {
        return this.disposerMethodPointer;
    }
}
