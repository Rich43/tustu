package jdk.jfr.internal;

import java.lang.reflect.Modifier;
import jdk.jfr.Event;
import jdk.jfr.internal.handlers.EventHandler;
import jdk.jfr.internal.instrument.JDKEvents;

/* loaded from: jfr.jar:jdk/jfr/internal/JVMUpcalls.class */
final class JVMUpcalls {
    JVMUpcalls() {
    }

    static byte[] onRetransform(long j2, boolean z2, Class<?> cls, byte[] bArr) throws Throwable {
        try {
            if (Event.class.isAssignableFrom(cls) && !Modifier.isAbstract(cls.getModifiers())) {
                if (Utils.getHandler(cls.asSubclass(Event.class)) == null) {
                    Logger.log(LogTag.JFR_SYSTEM, LogLevel.INFO, "No event handler found for " + cls.getName() + ". Ignoring instrumentation request.");
                    return bArr;
                }
                Logger.log(LogTag.JFR_SYSTEM, LogLevel.INFO, "Adding instrumentation to event class " + cls.getName() + " using retransform");
                byte[] bArrBuildInstrumented = new EventInstrumentation(cls.getSuperclass(), bArr, j2).buildInstrumented();
                ASMToolkit.logASM(cls.getName(), bArrBuildInstrumented);
                return bArrBuildInstrumented;
            }
            return JDKEvents.retransformCallback(cls, bArr);
        } catch (Throwable th) {
            Logger.log(LogTag.JFR_SYSTEM, LogLevel.WARN, "Unexpected error when adding instrumentation to event class " + cls.getName());
            return bArr;
        }
    }

    static byte[] bytesForEagerInstrumentation(long j2, boolean z2, Class<?> cls, byte[] bArr) throws Throwable {
        if (JVMSupport.isNotAvailable()) {
            return bArr;
        }
        try {
            EventInstrumentation eventInstrumentation = new EventInstrumentation(cls, bArr, j2);
            String eventName = eventInstrumentation.getEventName();
            if (!z2 && ((!MetadataRepository.getInstance().isEnabled(eventInstrumentation.getEventName()) && !eventInstrumentation.isEnabled()) || !eventInstrumentation.isRegistered())) {
                Logger.log(LogTag.JFR_SYSTEM, LogLevel.INFO, "Skipping instrumentation for event type " + eventName + " since event was disabled on class load");
                return bArr;
            }
            eventInstrumentation.setGuardHandler(true);
            Logger.log(LogTag.JFR_SYSTEM, LogLevel.INFO, "Adding " + (z2 ? "forced " : "") + "instrumentation for event type " + eventName + " during initial class load");
            new EventHandlerCreator(j2, eventInstrumentation.getSettingInfos(), eventInstrumentation.getFieldInfos()).makeEventHandlerClass();
            byte[] bArrBuildInstrumented = eventInstrumentation.buildInstrumented();
            ASMToolkit.logASM(eventInstrumentation.getClassName() + "(" + j2 + ")", bArrBuildInstrumented);
            return bArrBuildInstrumented;
        } catch (Throwable th) {
            Logger.log(LogTag.JFR_SYSTEM, LogLevel.WARN, "Unexpected error when adding instrumentation for event type <Unknown>");
            return bArr;
        }
    }

    static Thread createRecorderThread(ThreadGroup threadGroup, ClassLoader classLoader) {
        return SecuritySupport.createRecorderThread(threadGroup, classLoader);
    }

    static Class<? extends EventHandler> getEventHandlerProxyClass() {
        return EventHandlerProxyCreator.proxyClass;
    }
}
