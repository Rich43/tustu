package com.sun.xml.internal.bind;

import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import java.lang.reflect.Field;
import javax.xml.bind.JAXBException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/InternalAccessorFactory.class */
public interface InternalAccessorFactory extends AccessorFactory {
    Accessor createFieldAccessor(Class cls, Field field, boolean z2, boolean z3) throws JAXBException;
}
