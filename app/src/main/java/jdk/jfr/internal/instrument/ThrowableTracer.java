package jdk.jfr.internal.instrument;

import java.util.concurrent.atomic.AtomicLong;
import jdk.jfr.events.ErrorThrownEvent;
import jdk.jfr.events.ExceptionThrownEvent;

/* loaded from: jfr.jar:jdk/jfr/internal/instrument/ThrowableTracer.class */
public final class ThrowableTracer {
    private static AtomicLong numThrowables = new AtomicLong(0);

    public static void traceError(Error error, String str) {
        if (error instanceof OutOfMemoryError) {
            return;
        }
        ErrorThrownEvent errorThrownEvent = new ErrorThrownEvent();
        errorThrownEvent.message = str;
        errorThrownEvent.thrownClass = error.getClass();
        errorThrownEvent.commit();
        ExceptionThrownEvent exceptionThrownEvent = new ExceptionThrownEvent();
        exceptionThrownEvent.message = str;
        exceptionThrownEvent.thrownClass = error.getClass();
        exceptionThrownEvent.commit();
        numThrowables.incrementAndGet();
    }

    public static void traceThrowable(Throwable th, String str) {
        ExceptionThrownEvent exceptionThrownEvent = new ExceptionThrownEvent();
        exceptionThrownEvent.message = str;
        exceptionThrownEvent.thrownClass = th.getClass();
        exceptionThrownEvent.commit();
        numThrowables.incrementAndGet();
    }

    public static long numThrowables() {
        return numThrowables.get();
    }
}
