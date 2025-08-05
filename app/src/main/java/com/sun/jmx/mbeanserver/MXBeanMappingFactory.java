package com.sun.jmx.mbeanserver;

import java.lang.reflect.Type;
import javax.management.openmbean.OpenDataException;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/MXBeanMappingFactory.class */
public abstract class MXBeanMappingFactory {
    public static final MXBeanMappingFactory DEFAULT = new DefaultMXBeanMappingFactory();

    public abstract MXBeanMapping mappingForType(Type type, MXBeanMappingFactory mXBeanMappingFactory) throws OpenDataException;

    protected MXBeanMappingFactory() {
    }
}
