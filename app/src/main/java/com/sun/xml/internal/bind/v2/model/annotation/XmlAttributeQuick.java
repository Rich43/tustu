package com.sun.xml.internal.bind.v2.model.annotation;

import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlAttribute;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/annotation/XmlAttributeQuick.class */
final class XmlAttributeQuick extends Quick implements XmlAttribute {
    private final XmlAttribute core;

    public XmlAttributeQuick(Locatable upstream, XmlAttribute core) {
        super(upstream);
        this.core = core;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Quick
    protected Annotation getAnnotation() {
        return this.core;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Quick
    protected Quick newInstance(Locatable upstream, Annotation core) {
        return new XmlAttributeQuick(upstream, (XmlAttribute) core);
    }

    @Override // java.lang.annotation.Annotation
    public Class<XmlAttribute> annotationType() {
        return XmlAttribute.class;
    }

    @Override // javax.xml.bind.annotation.XmlAttribute
    public String name() {
        return this.core.name();
    }

    @Override // javax.xml.bind.annotation.XmlAttribute
    public String namespace() {
        return this.core.namespace();
    }

    @Override // javax.xml.bind.annotation.XmlAttribute
    public boolean required() {
        return this.core.required();
    }
}
