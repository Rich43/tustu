package com.sun.xml.internal.bind.v2.model.impl;

import com.sun.xml.internal.bind.v2.model.core.MapPropertyInfo;
import com.sun.xml.internal.bind.v2.model.core.NonElement;
import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
import com.sun.xml.internal.bind.v2.model.core.TypeInfo;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/MapPropertyInfoImpl.class */
class MapPropertyInfoImpl<T, C, F, M> extends PropertyInfoImpl<T, C, F, M> implements MapPropertyInfo<T, C> {
    private final QName xmlName;
    private boolean nil;
    private final T keyType;
    private final T valueType;
    private NonElement<T, C> keyTypeInfo;
    private NonElement<T, C> valueTypeInfo;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !MapPropertyInfoImpl.class.desiredAssertionStatus();
    }

    public MapPropertyInfoImpl(ClassInfoImpl<T, C, F, M> ci, PropertySeed<T, C, F, M> seed) {
        super(ci, seed);
        XmlElementWrapper xe = (XmlElementWrapper) seed.readAnnotation(XmlElementWrapper.class);
        this.xmlName = calcXmlName(xe);
        this.nil = xe != null && xe.nillable();
        T raw = getRawType();
        T bt2 = nav().getBaseClass(raw, nav().asDecl(Map.class));
        if (!$assertionsDisabled && bt2 == null) {
            throw new AssertionError();
        }
        if (nav().isParameterizedType(bt2)) {
            this.keyType = nav().getTypeArgument(bt2, 0);
            this.valueType = nav().getTypeArgument(bt2, 1);
        } else {
            T tRef = nav().ref(Object.class);
            this.valueType = tRef;
            this.keyType = tRef;
        }
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.PropertyInfo
    /* renamed from: ref */
    public Collection<? extends TypeInfo<T, C>> ref2() {
        return Arrays.asList(getKeyType2(), getValueType2());
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.PropertyInfo
    public final PropertyKind kind() {
        return PropertyKind.MAP;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.MapPropertyInfo
    public QName getXmlName() {
        return this.xmlName;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.MapPropertyInfo
    public boolean isCollectionNillable() {
        return this.nil;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.MapPropertyInfo
    /* renamed from: getKeyType */
    public NonElement<T, C> getKeyType2() {
        if (this.keyTypeInfo == null) {
            this.keyTypeInfo = getTarget(this.keyType);
        }
        return this.keyTypeInfo;
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.MapPropertyInfo
    /* renamed from: getValueType */
    public NonElement<T, C> getValueType2() {
        if (this.valueTypeInfo == null) {
            this.valueTypeInfo = getTarget(this.valueType);
        }
        return this.valueTypeInfo;
    }

    public NonElement<T, C> getTarget(T type) {
        if ($assertionsDisabled || this.parent.builder != null) {
            return this.parent.builder.getTypeInfo(type, this);
        }
        throw new AssertionError((Object) "this method must be called during the build stage");
    }
}
