package com.sun.xml.internal.ws.policy.privateutil;

import com.sun.istack.internal.logging.Logger;
import java.lang.reflect.Field;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/privateutil/PolicyLogger.class */
public final class PolicyLogger extends Logger {
    private static final String POLICY_PACKAGE_ROOT = "com.sun.xml.internal.ws.policy";

    private PolicyLogger(String policyLoggerName, String className) {
        super(policyLoggerName, className);
    }

    public static PolicyLogger getLogger(Class<?> componentClass) {
        String componentClassName = componentClass.getName();
        if (componentClassName.startsWith(POLICY_PACKAGE_ROOT)) {
            return new PolicyLogger(getLoggingSubsystemName() + componentClassName.substring(POLICY_PACKAGE_ROOT.length()), componentClassName);
        }
        return new PolicyLogger(getLoggingSubsystemName() + "." + componentClassName, componentClassName);
    }

    private static String getLoggingSubsystemName() {
        String loggingSubsystemName = "wspolicy";
        try {
            Class jaxwsConstants = Class.forName("com.sun.xml.internal.ws.util.Constants");
            Field loggingDomainField = jaxwsConstants.getField("LoggingDomain");
            Object loggingDomain = loggingDomainField.get(null);
            loggingSubsystemName = loggingDomain.toString().concat(".wspolicy");
        } catch (RuntimeException e2) {
        } catch (Exception e3) {
        }
        return loggingSubsystemName;
    }
}
