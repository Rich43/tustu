package com.sun.xml.internal.bind.v2.model.runtime;

import com.sun.xml.internal.bind.v2.model.core.ClassInfo;
import com.sun.xml.internal.bind.v2.model.core.ElementInfo;
import com.sun.xml.internal.bind.v2.model.core.ElementPropertyInfo;
import com.sun.xml.internal.bind.v2.model.core.NonElement;
import java.lang.reflect.Type;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/runtime/RuntimeElementInfo.class */
public interface RuntimeElementInfo extends ElementInfo<Type, Class>, RuntimeElement {
    @Override // com.sun.xml.internal.bind.v2.model.core.Element
    /* renamed from: getScope, reason: merged with bridge method [inline-methods] */
    ClassInfo<Type, Class> getScope2();

    @Override // com.sun.xml.internal.bind.v2.model.core.ElementInfo
    /* renamed from: getProperty, reason: merged with bridge method [inline-methods] */
    ElementPropertyInfo<Type, Class> getProperty2();

    @Override // com.sun.xml.internal.bind.v2.model.core.ElementInfo, com.sun.xml.internal.bind.v2.model.core.TypeInfo
    /* renamed from: getType */
    Type getType2();

    @Override // com.sun.xml.internal.bind.v2.model.core.ElementInfo
    /* renamed from: getContentType, reason: merged with bridge method [inline-methods] */
    NonElement<Type, Class> getContentType2();
}
