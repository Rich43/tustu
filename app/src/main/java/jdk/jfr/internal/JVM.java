package jdk.jfr.internal;

import java.util.List;
import jdk.jfr.Event;

/* loaded from: jfr.jar:jdk/jfr/internal/JVM.class */
public final class JVM {
    private static final JVM jvm = new JVM();
    static final Object FILE_DELTA_CHANGE = new Object();
    static final long RESERVED_CLASS_ID_LIMIT = 400;
    private volatile boolean recording;
    private volatile boolean nativeOK;

    private static native void registerNatives();

    public native void beginRecording();

    public static native long counterTime();

    public native boolean emitEvent(long j2, long j3, long j4);

    public native void endRecording();

    public native List<Class<? extends Event>> getAllEventClasses();

    public native long getUnloadedEventClassCount();

    public static native long getClassId(Class<?> cls);

    public static native long getClassIdNonIntrinsic(Class<?> cls);

    public native String getPid();

    public native long getStackTraceId(int i2);

    public native long getThreadId(Thread thread);

    public native long getTicksFrequency();

    public static native void log(int i2, int i3, String str);

    public static native boolean shouldLog(int i2);

    public static native void subscribeLogLevel(LogTag logTag, int i2);

    public native synchronized void retransformClasses(Class<?>[] clsArr);

    public native void setEnabled(long j2, boolean z2);

    public native void setFileNotification(long j2);

    public native void setGlobalBufferCount(long j2) throws IllegalStateException, IllegalArgumentException;

    public native void setGlobalBufferSize(long j2) throws IllegalArgumentException;

    public native void setMemorySize(long j2) throws IllegalArgumentException;

    public native void setMethodSamplingInterval(long j2, long j3);

    public native void setOutput(String str);

    public native void setForceInstrumentation(boolean z2);

    public native void setSampleThreads(boolean z2) throws IllegalStateException;

    public native void setCompressedIntegers(boolean z2) throws IllegalStateException;

    public native void setStackDepth(int i2) throws IllegalStateException, IllegalArgumentException;

    public native void setStackTraceEnabled(long j2, boolean z2);

    public native void setThreadBufferSize(long j2) throws IllegalStateException, IllegalArgumentException;

    public native boolean setThreshold(long j2, long j3);

    public native void storeMetadataDescriptor(byte[] bArr);

    public native boolean getAllowedToDoEventRetransforms();

    private native boolean createJFR(boolean z2) throws IllegalStateException;

    private native boolean destroyJFR();

    public native boolean isAvailable();

    public native double getTimeConversionFactor();

    public native long getTypeId(Class<?> cls);

    public static native Object getEventWriter();

    public static native EventWriter newEventWriter();

    public static native boolean flush(EventWriter eventWriter, int i2, int i3);

    public native void setRepositoryLocation(String str);

    public native void abort(String str);

    public static native boolean addStringConstant(boolean z2, long j2, String str);

    public native long getEpochAddress();

    public native void uncaughtException(Thread thread, Throwable th);

    public native boolean setCutoff(long j2, long j3);

    public native void emitOldObjectSamples(long j2, boolean z2);

    public native boolean shouldRotateDisk();

    static {
        registerNatives();
        Options.ensureInitialized();
        EventHandlerProxyCreator.ensureInitialized();
    }

    public static JVM getJVM() {
        return jvm;
    }

    private JVM() {
    }

    public void endRecording_() {
        endRecording();
        this.recording = false;
    }

    public void beginRecording_() {
        beginRecording();
        this.recording = true;
    }

    public boolean isRecording() {
        return this.recording;
    }

    public boolean createFailedNativeJFR() throws IllegalStateException {
        return createJFR(true);
    }

    public void createNativeJFR() {
        this.nativeOK = createJFR(false);
    }

    public boolean destroyNativeJFR() {
        boolean zDestroyJFR = destroyJFR();
        this.nativeOK = !zDestroyJFR;
        return zDestroyJFR;
    }

    public boolean hasNativeJFR() {
        return this.nativeOK;
    }
}
