package com.sun.xml.internal.bind.v2.model.runtime;

import com.sun.xml.internal.bind.v2.model.core.NonElement;
import com.sun.xml.internal.bind.v2.model.core.ValuePropertyInfo;
import java.lang.reflect.Type;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/runtime/RuntimeValuePropertyInfo.class */
public interface RuntimeValuePropertyInfo extends ValuePropertyInfo<Type, Class>, RuntimePropertyInfo, RuntimeNonElementRef {
    @Override // com.sun.xml.internal.bind.v2.model.core.NonElementRef
    /* renamed from: getTarget */
    NonElement<Type, Class> getTarget2();
}
