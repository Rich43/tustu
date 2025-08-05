package com.sun.xml.internal.bind.v2.model.runtime;

import com.sun.xml.internal.bind.v2.model.core.PropertyInfo;
import com.sun.xml.internal.bind.v2.model.core.TypeInfo;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import java.lang.reflect.Type;
import java.util.Collection;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/runtime/RuntimePropertyInfo.class */
public interface RuntimePropertyInfo extends PropertyInfo<Type, Class> {
    @Override // com.sun.xml.internal.bind.v2.model.core.PropertyInfo
    Collection<? extends TypeInfo<Type, Class>> ref();

    Accessor getAccessor();

    boolean elementOnlyContent();

    Type getRawType();

    Type getIndividualType();
}
