package com.sun.xml.internal.bind.v2.model.runtime;

import com.sun.xml.internal.bind.v2.model.core.Element;
import com.sun.xml.internal.bind.v2.model.core.ReferencePropertyInfo;
import java.lang.reflect.Type;
import java.util.Set;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/runtime/RuntimeReferencePropertyInfo.class */
public interface RuntimeReferencePropertyInfo extends ReferencePropertyInfo<Type, Class>, RuntimePropertyInfo {
    @Override // com.sun.xml.internal.bind.v2.model.core.ReferencePropertyInfo
    Set<? extends Element<Type, Class>> getElements();
}
