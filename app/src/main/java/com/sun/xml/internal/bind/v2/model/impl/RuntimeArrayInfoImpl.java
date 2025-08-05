package com.sun.xml.internal.bind.v2.model.impl;

import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
import com.sun.xml.internal.bind.v2.model.core.NonElement;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeArrayInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement;
import com.sun.xml.internal.bind.v2.runtime.Transducer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/RuntimeArrayInfoImpl.class */
final class RuntimeArrayInfoImpl extends ArrayInfoImpl<Type, Class, Field, Method> implements RuntimeArrayInfo {
    RuntimeArrayInfoImpl(RuntimeModelBuilder builder, Locatable upstream, Class arrayType) {
        super(builder, upstream, arrayType);
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.ArrayInfoImpl, com.sun.xml.internal.bind.v2.model.core.TypeInfo
    /* renamed from: getType */
    public Type getType2() {
        return (Class) super.getType2();
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.ArrayInfoImpl, com.sun.xml.internal.bind.v2.model.core.ArrayInfo
    /* renamed from: getItemType */
    public NonElement<Type, Class> getItemType2() {
        return (RuntimeNonElement) super.getItemType2();
    }

    @Override // com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement
    public <V> Transducer<V> getTransducer() {
        return null;
    }
}
