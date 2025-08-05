package com.sun.xml.internal.bind.v2.model.runtime;

import com.sun.xml.internal.bind.v2.model.core.AttributePropertyInfo;
import com.sun.xml.internal.bind.v2.model.core.NonElement;
import java.lang.reflect.Type;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/runtime/RuntimeAttributePropertyInfo.class */
public interface RuntimeAttributePropertyInfo extends AttributePropertyInfo<Type, Class>, RuntimePropertyInfo, RuntimeNonElementRef {
    @Override // com.sun.xml.internal.bind.v2.model.core.AttributePropertyInfo, com.sun.xml.internal.bind.v2.model.core.NonElementRef
    /* renamed from: getTarget */
    NonElement<Type, Class> getTarget2();
}
