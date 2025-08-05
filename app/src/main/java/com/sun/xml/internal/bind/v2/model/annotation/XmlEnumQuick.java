package com.sun.xml.internal.bind.v2.model.annotation;

import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlEnum;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/annotation/XmlEnumQuick.class */
final class XmlEnumQuick extends Quick implements XmlEnum {
    private final XmlEnum core;

    public XmlEnumQuick(Locatable upstream, XmlEnum core) {
        super(upstream);
        this.core = core;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Quick
    protected Annotation getAnnotation() {
        return this.core;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Quick
    protected Quick newInstance(Locatable upstream, Annotation core) {
        return new XmlEnumQuick(upstream, (XmlEnum) core);
    }

    @Override // java.lang.annotation.Annotation
    public Class<XmlEnum> annotationType() {
        return XmlEnum.class;
    }

    @Override // javax.xml.bind.annotation.XmlEnum
    public Class value() {
        return this.core.value();
    }
}
