package jdk.internal.dynalink.beans;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import sun.reflect.CallerSensitive;

/* loaded from: nashorn.jar:jdk/internal/dynalink/beans/CallerSensitiveDetector.class */
public class CallerSensitiveDetector {
    private static final DetectionStrategy DETECTION_STRATEGY = getDetectionStrategy();

    static boolean isCallerSensitive(AccessibleObject ao2) {
        return DETECTION_STRATEGY.isCallerSensitive(ao2);
    }

    private static DetectionStrategy getDetectionStrategy() {
        try {
            return new PrivilegedDetectionStrategy();
        } catch (Throwable th) {
            return new UnprivilegedDetectionStrategy();
        }
    }

    /* loaded from: nashorn.jar:jdk/internal/dynalink/beans/CallerSensitiveDetector$DetectionStrategy.class */
    private static abstract class DetectionStrategy {
        abstract boolean isCallerSensitive(AccessibleObject accessibleObject);

        private DetectionStrategy() {
        }
    }

    /* loaded from: nashorn.jar:jdk/internal/dynalink/beans/CallerSensitiveDetector$PrivilegedDetectionStrategy.class */
    private static class PrivilegedDetectionStrategy extends DetectionStrategy {
        private static final Class<? extends Annotation> CALLER_SENSITIVE_ANNOTATION_CLASS = CallerSensitive.class;

        private PrivilegedDetectionStrategy() {
            super();
        }

        @Override // jdk.internal.dynalink.beans.CallerSensitiveDetector.DetectionStrategy
        boolean isCallerSensitive(AccessibleObject ao2) {
            return ao2.getAnnotation(CALLER_SENSITIVE_ANNOTATION_CLASS) != null;
        }
    }

    /* loaded from: nashorn.jar:jdk/internal/dynalink/beans/CallerSensitiveDetector$UnprivilegedDetectionStrategy.class */
    private static class UnprivilegedDetectionStrategy extends DetectionStrategy {
        private static final String CALLER_SENSITIVE_ANNOTATION_STRING = "@sun.reflect.CallerSensitive()";

        private UnprivilegedDetectionStrategy() {
            super();
        }

        @Override // jdk.internal.dynalink.beans.CallerSensitiveDetector.DetectionStrategy
        boolean isCallerSensitive(AccessibleObject o2) {
            for (Annotation a2 : o2.getAnnotations()) {
                if (String.valueOf(a2).equals(CALLER_SENSITIVE_ANNOTATION_STRING)) {
                    return true;
                }
            }
            return false;
        }
    }
}
