package com.sun.xml.internal.bind;

import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.xml.bind.JAXBException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/AccessorFactory.class */
public interface AccessorFactory {
    Accessor createFieldAccessor(Class cls, Field field, boolean z2) throws JAXBException;

    Accessor createPropertyAccessor(Class cls, Method method, Method method2) throws JAXBException;
}
