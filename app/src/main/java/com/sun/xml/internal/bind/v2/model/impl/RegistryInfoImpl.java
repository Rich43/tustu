package com.sun.xml.internal.bind.v2.model.impl;

import com.sun.xml.internal.bind.v2.ContextFactory;
import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
import com.sun.xml.internal.bind.v2.model.annotation.MethodLocatable;
import com.sun.xml.internal.bind.v2.model.core.RegistryInfo;
import com.sun.xml.internal.bind.v2.model.core.TypeInfo;
import com.sun.xml.internal.bind.v2.model.nav.Navigator;
import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationException;
import com.sun.xml.internal.bind.v2.runtime.Location;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlElementDecl;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/RegistryInfoImpl.class */
final class RegistryInfoImpl<T, C, F, M> implements Locatable, RegistryInfo<T, C> {
    final C registryClass;
    private final Locatable upstream;
    private final Navigator<T, C, F, M> nav;
    private final Set<TypeInfo<T, C>> references = new LinkedHashSet();

    RegistryInfoImpl(ModelBuilder<T, C, F, M> builder, Locatable upstream, C registryClass) {
        this.nav = builder.nav;
        this.registryClass = registryClass;
        this.upstream = upstream;
        builder.registries.put(getPackageName(), this);
        if (this.nav.getDeclaredField(registryClass, ContextFactory.USE_JAXB_PROPERTIES) != null) {
            builder.reportError(new IllegalAnnotationException(Messages.MISSING_JAXB_PROPERTIES.format(getPackageName()), this));
            return;
        }
        for (M m2 : this.nav.getDeclaredMethods(registryClass)) {
            XmlElementDecl em = (XmlElementDecl) builder.reader.getMethodAnnotation(XmlElementDecl.class, m2, this);
            if (em == null) {
                if (this.nav.getMethodName(m2).startsWith("create")) {
                    this.references.add(builder.getTypeInfo(this.nav.getReturnType(m2), new MethodLocatable(this, m2, this.nav)));
                }
            } else {
                try {
                    ElementInfoImpl<T, C, F, M> ei = builder.createElementInfo(this, m2);
                    builder.typeInfoSet.add(ei, builder);
                    this.references.add(ei);
                } catch (IllegalAnnotationException e2) {
                    builder.reportError(e2);
                }
            }
        }
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Locatable
    public Locatable getUpstream() {
        return this.upstream;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Locatable
    public Location getLocation() {
        return this.nav.getClassLocation(this.registryClass);
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.RegistryInfo
    public Set<TypeInfo<T, C>> getReferences() {
        return this.references;
    }

    public String getPackageName() {
        return this.nav.getPackageName(this.registryClass);
    }

    @Override // com.sun.xml.internal.bind.v2.model.core.RegistryInfo
    public C getClazz() {
        return this.registryClass;
    }
}
