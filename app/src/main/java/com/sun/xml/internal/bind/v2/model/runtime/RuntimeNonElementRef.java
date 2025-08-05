package com.sun.xml.internal.bind.v2.model.runtime;

import com.sun.xml.internal.bind.v2.model.core.NonElement;
import com.sun.xml.internal.bind.v2.model.core.NonElementRef;
import com.sun.xml.internal.bind.v2.model.core.PropertyInfo;
import com.sun.xml.internal.bind.v2.runtime.Transducer;
import java.lang.reflect.Type;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/runtime/RuntimeNonElementRef.class */
public interface RuntimeNonElementRef extends NonElementRef<Type, Class> {
    @Override // com.sun.xml.internal.bind.v2.model.core.NonElementRef
    /* renamed from: getTarget, reason: merged with bridge method [inline-methods] */
    NonElement<Type, Class> getTarget2();

    @Override // com.sun.xml.internal.bind.v2.model.core.NonElementRef
    /* renamed from: getSource, reason: merged with bridge method [inline-methods] */
    PropertyInfo<Type, Class> getSource2();

    Transducer getTransducer();
}
