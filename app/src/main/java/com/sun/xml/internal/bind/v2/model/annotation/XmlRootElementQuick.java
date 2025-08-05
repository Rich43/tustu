package com.sun.xml.internal.bind.v2.model.annotation;

import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlRootElement;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/annotation/XmlRootElementQuick.class */
final class XmlRootElementQuick extends Quick implements XmlRootElement {
    private final XmlRootElement core;

    public XmlRootElementQuick(Locatable upstream, XmlRootElement core) {
        super(upstream);
        this.core = core;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Quick
    protected Annotation getAnnotation() {
        return this.core;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Quick
    protected Quick newInstance(Locatable upstream, Annotation core) {
        return new XmlRootElementQuick(upstream, (XmlRootElement) core);
    }

    @Override // java.lang.annotation.Annotation
    public Class<XmlRootElement> annotationType() {
        return XmlRootElement.class;
    }

    @Override // javax.xml.bind.annotation.XmlRootElement
    public String name() {
        return this.core.name();
    }

    @Override // javax.xml.bind.annotation.XmlRootElement
    public String namespace() {
        return this.core.namespace();
    }
}
