package jdk.jfr;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import jdk.jfr.internal.JVM;
import jdk.jfr.internal.JVMSupport;
import jdk.jfr.internal.LogLevel;
import jdk.jfr.internal.LogTag;
import jdk.jfr.internal.Logger;
import jdk.jfr.internal.MetadataRepository;
import jdk.jfr.internal.Options;
import jdk.jfr.internal.PlatformRecorder;
import jdk.jfr.internal.PlatformRecording;
import jdk.jfr.internal.Repository;
import jdk.jfr.internal.RequestEngine;
import jdk.jfr.internal.Utils;

/* loaded from: jfr.jar:jdk/jfr/FlightRecorder.class */
public final class FlightRecorder {
    private static volatile FlightRecorder platformRecorder;
    private static volatile boolean initialized;
    private final PlatformRecorder internal;

    private FlightRecorder(PlatformRecorder platformRecorder2) {
        this.internal = platformRecorder2;
    }

    public List<Recording> getRecordings() {
        ArrayList arrayList = new ArrayList();
        Iterator<PlatformRecording> it = this.internal.getRecordings().iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().getRecording());
        }
        return Collections.unmodifiableList(arrayList);
    }

    public Recording takeSnapshot() {
        Recording recording = new Recording();
        recording.setName("Snapshot");
        this.internal.fillWithRecordedData(recording.getInternal(), null);
        return recording;
    }

    public static void register(Class<? extends Event> cls) {
        Objects.requireNonNull(cls);
        if (JVMSupport.isNotAvailable()) {
            return;
        }
        Utils.ensureValidEventSubclass(cls);
        MetadataRepository.getInstance().register(cls);
    }

    public static void unregister(Class<? extends Event> cls) {
        Objects.requireNonNull(cls);
        if (JVMSupport.isNotAvailable()) {
            return;
        }
        Utils.ensureValidEventSubclass(cls);
        MetadataRepository.getInstance().unregister(cls);
    }

    public static FlightRecorder getFlightRecorder() throws IllegalStateException, SecurityException {
        synchronized (PlatformRecorder.class) {
            Utils.checkAccessFlightRecorder();
            JVMSupport.ensureWithIllegalStateException();
            if (platformRecorder == null) {
                try {
                    try {
                        platformRecorder = new FlightRecorder(new PlatformRecorder());
                        initialized = true;
                        Logger.log(LogTag.JFR, LogLevel.INFO, "Flight Recorder initialized");
                        Logger.log(LogTag.JFR, LogLevel.DEBUG, "maxchunksize: " + Options.getMaxChunkSize() + " bytes");
                        Logger.log(LogTag.JFR, LogLevel.DEBUG, "memorysize: " + Options.getMemorySize() + " bytes");
                        Logger.log(LogTag.JFR, LogLevel.DEBUG, "globalbuffersize: " + Options.getGlobalBufferSize() + " bytes");
                        Logger.log(LogTag.JFR, LogLevel.DEBUG, "globalbuffercount: " + Options.getGlobalBufferCount());
                        Logger.log(LogTag.JFR, LogLevel.DEBUG, "dumppath: " + ((Object) Options.getDumpPath()));
                        Logger.log(LogTag.JFR, LogLevel.DEBUG, "samplethreads: " + Options.getSampleThreads());
                        Logger.log(LogTag.JFR, LogLevel.DEBUG, "stackdepth: " + Options.getStackDepth());
                        Logger.log(LogTag.JFR, LogLevel.DEBUG, "threadbuffersize: " + Options.getThreadBufferSize());
                        Logger.log(LogTag.JFR, LogLevel.INFO, "Created repository " + Repository.getRepository().getRepositoryPath().toString());
                        PlatformRecorder.notifyRecorderInitialized(platformRecorder);
                    } catch (Exception e2) {
                        throw new IllegalStateException("Can't create Flight Recorder. " + e2.getMessage(), e2);
                    }
                } catch (IllegalStateException e3) {
                    throw e3;
                }
            }
        }
        return platformRecorder;
    }

    public static void addPeriodicEvent(Class<? extends Event> cls, Runnable runnable) throws SecurityException {
        Objects.requireNonNull(cls);
        Objects.requireNonNull(runnable);
        if (JVMSupport.isNotAvailable()) {
            return;
        }
        Utils.ensureValidEventSubclass(cls);
        Utils.checkRegisterPermission();
        RequestEngine.addHook(AccessController.getContext(), EventType.getEventType(cls).getPlatformEventType(), runnable);
    }

    public static boolean removePeriodicEvent(Runnable runnable) throws SecurityException {
        Objects.requireNonNull(runnable);
        Utils.checkRegisterPermission();
        if (JVMSupport.isNotAvailable()) {
            return false;
        }
        return RequestEngine.removeHook(runnable);
    }

    public List<EventType> getEventTypes() {
        return Collections.unmodifiableList(MetadataRepository.getInstance().getRegisteredEventTypes());
    }

    public static void addListener(FlightRecorderListener flightRecorderListener) throws SecurityException {
        Objects.requireNonNull(flightRecorderListener);
        Utils.checkAccessFlightRecorder();
        if (JVMSupport.isNotAvailable()) {
            return;
        }
        PlatformRecorder.addListener(flightRecorderListener);
    }

    public static boolean removeListener(FlightRecorderListener flightRecorderListener) throws SecurityException {
        Objects.requireNonNull(flightRecorderListener);
        Utils.checkAccessFlightRecorder();
        if (JVMSupport.isNotAvailable()) {
            return false;
        }
        return PlatformRecorder.removeListener(flightRecorderListener);
    }

    public static boolean isAvailable() {
        if (JVMSupport.isNotAvailable()) {
            return false;
        }
        return JVM.getJVM().isAvailable();
    }

    public static boolean isInitialized() {
        return initialized;
    }

    PlatformRecorder getInternal() {
        return this.internal;
    }
}
