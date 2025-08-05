package jdk.jfr.internal;

import java.io.IOException;
import java.lang.Thread;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.function.Supplier;
import jdk.jfr.RecordingState;

/* loaded from: jfr.jar:jdk/jfr/internal/ShutdownHook.class */
final class ShutdownHook implements Runnable {
    private final PlatformRecorder recorder;
    Object tlabDummyObject;

    ShutdownHook(PlatformRecorder platformRecorder) {
        this.recorder = platformRecorder;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.tlabDummyObject = new Object();
        for (PlatformRecording platformRecording : this.recorder.getRecordings()) {
            if (platformRecording.getDumpOnExit() && platformRecording.getState() == RecordingState.RUNNING) {
                dump(platformRecording);
            }
        }
        this.recorder.destroy();
    }

    private void dump(PlatformRecording platformRecording) {
        try {
            WriteableUserPath destination = platformRecording.getDestination();
            if (destination == null) {
                destination = makeDumpOnExitPath(platformRecording);
                platformRecording.setDestination(destination);
            }
            if (destination != null) {
                platformRecording.stop("Dump on exit");
            }
        } catch (Exception e2) {
            Logger.log(LogTag.JFR, LogLevel.DEBUG, (Supplier<String>) () -> {
                return "Could not dump recording " + platformRecording.getName() + " on exit.";
            });
        }
    }

    private WriteableUserPath makeDumpOnExitPath(final PlatformRecording platformRecording) {
        try {
            final String strMakeFilename = Utils.makeFilename(platformRecording.getRecording());
            return (WriteableUserPath) AccessController.doPrivileged(new PrivilegedExceptionAction<WriteableUserPath>() { // from class: jdk.jfr.internal.ShutdownHook.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public WriteableUserPath run() throws Exception {
                    return new WriteableUserPath(platformRecording.getDumpOnExitDirectory().toPath().resolve(strMakeFilename));
                }
            }, platformRecording.getNoDestinationDumpOnExitAccessControlContext());
        } catch (PrivilegedActionException e2) {
            Throwable cause = e2.getCause();
            if (cause instanceof SecurityException) {
                Logger.log(LogTag.JFR, LogLevel.WARN, "Not allowed to create dump path for recording " + platformRecording.getId() + " on exit.");
            }
            if (cause instanceof IOException) {
                Logger.log(LogTag.JFR, LogLevel.WARN, "Could not dump " + platformRecording.getId() + " on exit.");
                return null;
            }
            return null;
        }
    }

    /* loaded from: jfr.jar:jdk/jfr/internal/ShutdownHook$ExceptionHandler.class */
    static final class ExceptionHandler implements Thread.UncaughtExceptionHandler {
        ExceptionHandler() {
        }

        @Override // java.lang.Thread.UncaughtExceptionHandler
        public void uncaughtException(Thread thread, Throwable th) {
            JVM.getJVM().uncaughtException(thread, th);
        }
    }
}
