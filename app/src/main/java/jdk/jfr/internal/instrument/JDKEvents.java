package jdk.jfr.internal.instrument;

import java.util.ArrayList;
import java.util.function.Supplier;
import jdk.jfr.events.ActiveRecordingEvent;
import jdk.jfr.events.ActiveSettingEvent;
import jdk.jfr.events.ErrorThrownEvent;
import jdk.jfr.events.ExceptionStatisticsEvent;
import jdk.jfr.events.ExceptionThrownEvent;
import jdk.jfr.events.FileForceEvent;
import jdk.jfr.events.FileReadEvent;
import jdk.jfr.events.FileWriteEvent;
import jdk.jfr.events.SocketReadEvent;
import jdk.jfr.events.SocketWriteEvent;
import jdk.jfr.internal.JVM;
import jdk.jfr.internal.LogLevel;
import jdk.jfr.internal.LogTag;
import jdk.jfr.internal.Logger;
import jdk.jfr.internal.RequestEngine;
import jdk.jfr.internal.SecuritySupport;

/* loaded from: jfr.jar:jdk/jfr/internal/instrument/JDKEvents.class */
public final class JDKEvents {
    private static final Class<?>[] eventClasses = {FileForceEvent.class, FileReadEvent.class, FileWriteEvent.class, SocketReadEvent.class, SocketWriteEvent.class, ExceptionThrownEvent.class, ExceptionStatisticsEvent.class, ErrorThrownEvent.class, ActiveSettingEvent.class, ActiveRecordingEvent.class};
    private static final Class<?>[] instrumentationClasses = {FileInputStreamInstrumentor.class, FileOutputStreamInstrumentor.class, RandomAccessFileInstrumentor.class, FileChannelImplInstrumentor.class, SocketInputStreamInstrumentor.class, SocketOutputStreamInstrumentor.class, SocketChannelImplInstrumentor.class};
    private static final Class<?>[] targetClasses = new Class[instrumentationClasses.length];
    private static final JVM jvm = JVM.getJVM();
    private static final Runnable emitExceptionStatistics = JDKEvents::emitExceptionStatistics;
    private static boolean initializationTriggered;

    public static synchronized void initialize() {
        try {
            if (!initializationTriggered) {
                for (Class<?> cls : eventClasses) {
                    SecuritySupport.registerEvent(cls);
                }
                initializationTriggered = true;
                RequestEngine.addTrustedJDKHook(ExceptionStatisticsEvent.class, emitExceptionStatistics);
            }
        } catch (Exception e2) {
            Logger.log(LogTag.JFR_SYSTEM, LogLevel.WARN, "Could not initialize JDK events. " + e2.getMessage());
        }
    }

    public static void addInstrumentation() {
        try {
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < instrumentationClasses.length; i2++) {
                Class<?> cls = Class.forName(((JIInstrumentationTarget) instrumentationClasses[i2].getAnnotation(JIInstrumentationTarget.class)).value());
                targetClasses[i2] = cls;
                arrayList.add(cls);
            }
            arrayList.add(Throwable.class);
            arrayList.add(Error.class);
            Logger.log(LogTag.JFR_SYSTEM, LogLevel.INFO, "Retransformed JDK classes");
            jvm.retransformClasses((Class[]) arrayList.toArray(new Class[arrayList.size()]));
        } catch (Exception e2) {
            Logger.log(LogTag.JFR_SYSTEM, LogLevel.WARN, "Could not add instrumentation for JDK events. " + e2.getMessage());
        }
    }

    private static void emitExceptionStatistics() {
        ExceptionStatisticsEvent exceptionStatisticsEvent = new ExceptionStatisticsEvent();
        exceptionStatisticsEvent.throwables = ThrowableTracer.numThrowables();
        exceptionStatisticsEvent.commit();
    }

    public static byte[] retransformCallback(Class<?> cls, byte[] bArr) throws Throwable {
        if (Throwable.class == cls) {
            Logger.log(LogTag.JFR_SYSTEM, LogLevel.TRACE, "Instrumenting java.lang.Throwable");
            return ConstructorTracerWriter.generateBytes(Throwable.class, bArr);
        }
        if (Error.class == cls) {
            Logger.log(LogTag.JFR_SYSTEM, LogLevel.TRACE, "Instrumenting java.lang.Error");
            return ConstructorTracerWriter.generateBytes(Error.class, bArr);
        }
        for (int i2 = 0; i2 < targetClasses.length; i2++) {
            if (targetClasses[i2].equals(cls)) {
                Class<?> cls2 = instrumentationClasses[i2];
                Logger.log(LogTag.JFR_SYSTEM, LogLevel.TRACE, (Supplier<String>) () -> {
                    return "Processing instrumentation class: " + ((Object) cls2);
                });
                return new JIClassInstrumentation(instrumentationClasses[i2], cls, bArr).getNewBytes();
            }
        }
        return bArr;
    }

    public static void remove() {
        RequestEngine.removeHook(JDKEvents::emitExceptionStatistics);
    }
}
