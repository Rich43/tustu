package com.sun.xml.internal.bind.v2.model.annotation;

import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlType;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/annotation/XmlTypeQuick.class */
final class XmlTypeQuick extends Quick implements XmlType {
    private final XmlType core;

    public XmlTypeQuick(Locatable upstream, XmlType core) {
        super(upstream);
        this.core = core;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Quick
    protected Annotation getAnnotation() {
        return this.core;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Quick
    protected Quick newInstance(Locatable upstream, Annotation core) {
        return new XmlTypeQuick(upstream, (XmlType) core);
    }

    @Override // java.lang.annotation.Annotation
    public Class<XmlType> annotationType() {
        return XmlType.class;
    }

    @Override // javax.xml.bind.annotation.XmlType
    public String name() {
        return this.core.name();
    }

    @Override // javax.xml.bind.annotation.XmlType
    public String namespace() {
        return this.core.namespace();
    }

    @Override // javax.xml.bind.annotation.XmlType
    public String[] propOrder() {
        return this.core.propOrder();
    }

    @Override // javax.xml.bind.annotation.XmlType
    public Class factoryClass() {
        return this.core.factoryClass();
    }

    @Override // javax.xml.bind.annotation.XmlType
    public String factoryMethod() {
        return this.core.factoryMethod();
    }
}
