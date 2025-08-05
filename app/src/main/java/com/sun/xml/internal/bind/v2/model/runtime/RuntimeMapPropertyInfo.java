package com.sun.xml.internal.bind.v2.model.runtime;

import com.sun.xml.internal.bind.v2.model.core.MapPropertyInfo;
import com.sun.xml.internal.bind.v2.model.core.NonElement;
import java.lang.reflect.Type;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/runtime/RuntimeMapPropertyInfo.class */
public interface RuntimeMapPropertyInfo extends RuntimePropertyInfo, MapPropertyInfo<Type, Class> {
    @Override // com.sun.xml.internal.bind.v2.model.core.MapPropertyInfo
    /* renamed from: getKeyType, reason: merged with bridge method [inline-methods] */
    NonElement<Type, Class> getKeyType2();

    @Override // com.sun.xml.internal.bind.v2.model.core.MapPropertyInfo
    /* renamed from: getValueType, reason: merged with bridge method [inline-methods] */
    NonElement<Type, Class> getValueType2();
}
