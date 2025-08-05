package com.sun.xml.internal.bind.v2.model.runtime;

import com.sun.xml.internal.bind.v2.model.core.NonElement;
import com.sun.xml.internal.bind.v2.model.core.PropertyInfo;
import com.sun.xml.internal.bind.v2.model.core.TypeRef;
import java.lang.reflect.Type;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/runtime/RuntimeTypeRef.class */
public interface RuntimeTypeRef extends TypeRef<Type, Class>, RuntimeNonElementRef {
    @Override // com.sun.xml.internal.bind.v2.model.core.NonElementRef
    /* renamed from: getTarget */
    NonElement<Type, Class> getTarget2();

    @Override // com.sun.xml.internal.bind.v2.model.core.NonElementRef
    /* renamed from: getSource */
    PropertyInfo<Type, Class> getSource2();
}
