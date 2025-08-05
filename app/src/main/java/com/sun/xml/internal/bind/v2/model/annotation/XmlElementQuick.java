package com.sun.xml.internal.bind.v2.model.annotation;

import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlElement;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/annotation/XmlElementQuick.class */
final class XmlElementQuick extends Quick implements XmlElement {
    private final XmlElement core;

    public XmlElementQuick(Locatable upstream, XmlElement core) {
        super(upstream);
        this.core = core;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Quick
    protected Annotation getAnnotation() {
        return this.core;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Quick
    protected Quick newInstance(Locatable upstream, Annotation core) {
        return new XmlElementQuick(upstream, (XmlElement) core);
    }

    @Override // java.lang.annotation.Annotation
    public Class<XmlElement> annotationType() {
        return XmlElement.class;
    }

    @Override // javax.xml.bind.annotation.XmlElement
    public String name() {
        return this.core.name();
    }

    @Override // javax.xml.bind.annotation.XmlElement
    public Class type() {
        return this.core.type();
    }

    @Override // javax.xml.bind.annotation.XmlElement
    public String namespace() {
        return this.core.namespace();
    }

    @Override // javax.xml.bind.annotation.XmlElement
    public String defaultValue() {
        return this.core.defaultValue();
    }

    @Override // javax.xml.bind.annotation.XmlElement
    public boolean required() {
        return this.core.required();
    }

    @Override // javax.xml.bind.annotation.XmlElement
    public boolean nillable() {
        return this.core.nillable();
    }
}
