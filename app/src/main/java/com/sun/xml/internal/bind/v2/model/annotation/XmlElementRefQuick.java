package com.sun.xml.internal.bind.v2.model.annotation;

import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlElementRef;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/annotation/XmlElementRefQuick.class */
final class XmlElementRefQuick extends Quick implements XmlElementRef {
    private final XmlElementRef core;

    public XmlElementRefQuick(Locatable upstream, XmlElementRef core) {
        super(upstream);
        this.core = core;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Quick
    protected Annotation getAnnotation() {
        return this.core;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Quick
    protected Quick newInstance(Locatable upstream, Annotation core) {
        return new XmlElementRefQuick(upstream, (XmlElementRef) core);
    }

    @Override // java.lang.annotation.Annotation
    public Class<XmlElementRef> annotationType() {
        return XmlElementRef.class;
    }

    @Override // javax.xml.bind.annotation.XmlElementRef
    public String name() {
        return this.core.name();
    }

    @Override // javax.xml.bind.annotation.XmlElementRef
    public Class type() {
        return this.core.type();
    }

    @Override // javax.xml.bind.annotation.XmlElementRef
    public String namespace() {
        return this.core.namespace();
    }

    @Override // javax.xml.bind.annotation.XmlElementRef
    public boolean required() {
        return this.core.required();
    }
}
