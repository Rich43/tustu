package com.sun.xml.internal.bind.v2.model.runtime;

import com.sun.xml.internal.bind.v2.model.core.ArrayInfo;
import com.sun.xml.internal.bind.v2.model.core.NonElement;
import java.lang.reflect.Type;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/runtime/RuntimeArrayInfo.class */
public interface RuntimeArrayInfo extends ArrayInfo<Type, Class>, RuntimeNonElement {
    @Override // com.sun.xml.internal.bind.v2.model.core.TypeInfo
    /* renamed from: getType, reason: merged with bridge method [inline-methods] */
    Type getType2();

    @Override // com.sun.xml.internal.bind.v2.model.core.ArrayInfo
    /* renamed from: getItemType, reason: merged with bridge method [inline-methods] */
    NonElement<Type, Class> getItemType2();
}
