package com.sun.xml.internal.bind.v2.model.impl;

import com.sun.xml.internal.bind.v2.model.core.Adapter;
import com.sun.xml.internal.bind.v2.model.core.ClassInfo;
import com.sun.xml.internal.bind.v2.model.core.ElementPropertyInfo;
import com.sun.xml.internal.bind.v2.model.core.NonElement;
import com.sun.xml.internal.bind.v2.model.core.PropertyInfo;
import com.sun.xml.internal.bind.v2.model.core.TypeInfo;
import com.sun.xml.internal.bind.v2.model.core.TypeRef;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeClassInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeElementInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeElementPropertyInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeRef;
import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationException;
import com.sun.xml.internal.bind.v2.runtime.Transducer;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/RuntimeElementInfoImpl.class */
final class RuntimeElementInfoImpl extends ElementInfoImpl<Type, Class, Field, Method> implements RuntimeElementInfo {
    private final Class<? extends XmlAdapter> adapterType;

    public RuntimeElementInfoImpl(RuntimeModelBuilder modelBuilder, RegistryInfoImpl registry, Method method) throws IllegalAnnotationException {
        super(modelBuilder, registry, method);
        Adapter<Type, Class> a2 = getProperty2().getAdapter();
        if (a2 != null) {
            this.adapterType = a2.adapterType;
        } else {
            this.adapterType = null;
        }
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.ElementInfoImpl
    protected ElementInfoImpl<Type, Class, Field, Method>.PropertyImpl createPropertyImpl() {
        return new RuntimePropertyImpl();
    }

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/RuntimeElementInfoImpl$RuntimePropertyImpl.class */
    class RuntimePropertyImpl extends ElementInfoImpl<Type, Class, Field, Method>.PropertyImpl implements RuntimeElementPropertyInfo, RuntimeTypeRef {
        RuntimePropertyImpl() {
            super();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo
        public Accessor getAccessor() {
            if (RuntimeElementInfoImpl.this.adapterType == null) {
                return Accessor.JAXB_ELEMENT_VALUE;
            }
            return Accessor.JAXB_ELEMENT_VALUE.adapt((Class) getAdapter().defaultType, RuntimeElementInfoImpl.this.adapterType);
        }

        @Override // com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo
        public Type getRawType() {
            return Collection.class;
        }

        @Override // com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo
        public Type getIndividualType() {
            return RuntimeElementInfoImpl.this.getContentType2().getType2();
        }

        @Override // com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo
        public boolean elementOnlyContent() {
            return false;
        }

        @Override // com.sun.xml.internal.bind.v2.model.impl.ElementInfoImpl.PropertyImpl, com.sun.xml.internal.bind.v2.model.core.ElementPropertyInfo
        public List<? extends TypeRef<Type, Class>> getTypes() {
            return Collections.singletonList(this);
        }

        @Override // com.sun.xml.internal.bind.v2.model.impl.ElementInfoImpl.PropertyImpl, com.sun.xml.internal.bind.v2.model.core.PropertyInfo
        /* renamed from: ref, reason: merged with bridge method [inline-methods] */
        public Collection<? extends TypeInfo<Type, Class>> ref2() {
            return super.ref2();
        }

        @Override // com.sun.xml.internal.bind.v2.model.impl.ElementInfoImpl.PropertyImpl, com.sun.xml.internal.bind.v2.model.core.NonElementRef
        /* renamed from: getTarget */
        public NonElement<Type, Class> getTarget2() {
            return (RuntimeNonElement) super.getTarget2();
        }

        @Override // com.sun.xml.internal.bind.v2.model.impl.ElementInfoImpl.PropertyImpl, com.sun.xml.internal.bind.v2.model.core.NonElementRef
        /* renamed from: getSource */
        public PropertyInfo<Type, Class> getSource2() {
            return this;
        }

        @Override // com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElementRef
        public Transducer getTransducer() {
            return RuntimeModelBuilder.createTransducer(this);
        }
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.ElementInfoImpl, com.sun.xml.internal.bind.v2.model.core.ElementInfo
    /* renamed from: getProperty */
    public ElementPropertyInfo<Type, Class> getProperty2() {
        return (RuntimeElementPropertyInfo) super.getProperty2();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.xml.internal.bind.v2.model.impl.ElementInfoImpl, com.sun.xml.internal.bind.v2.model.core.TypeInfo
    /* renamed from: getType */
    public Type getType2() {
        return (Class) Utils.REFLECTION_NAVIGATOR.erasure(super.getType2());
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.ElementInfoImpl, com.sun.xml.internal.bind.v2.model.core.Element
    /* renamed from: getScope */
    public ClassInfo<Type, Class> getScope2() {
        return (RuntimeClassInfo) super.getScope2();
    }

    @Override // com.sun.xml.internal.bind.v2.model.impl.ElementInfoImpl, com.sun.xml.internal.bind.v2.model.core.ElementInfo
    /* renamed from: getContentType */
    public NonElement<Type, Class> getContentType2() {
        return (RuntimeNonElement) super.getContentType2();
    }
}
