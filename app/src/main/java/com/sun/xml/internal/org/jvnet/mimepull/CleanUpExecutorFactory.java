package com.sun.xml.internal.org.jvnet.mimepull;

import java.util.concurrent.Executor;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/CleanUpExecutorFactory.class */
public abstract class CleanUpExecutorFactory {
    private static final String DEFAULT_PROPERTY_NAME = CleanUpExecutorFactory.class.getName();

    public abstract Executor getExecutor();

    protected CleanUpExecutorFactory() {
    }

    public static CleanUpExecutorFactory newInstance() {
        try {
            return (CleanUpExecutorFactory) FactoryFinder.find(DEFAULT_PROPERTY_NAME);
        } catch (Exception e2) {
            return null;
        }
    }
}
