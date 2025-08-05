package com.sun.xml.internal.bind.v2.model.annotation;

import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlValue;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/annotation/XmlValueQuick.class */
final class XmlValueQuick extends Quick implements XmlValue {
    private final XmlValue core;

    public XmlValueQuick(Locatable upstream, XmlValue core) {
        super(upstream);
        this.core = core;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Quick
    protected Annotation getAnnotation() {
        return this.core;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Quick
    protected Quick newInstance(Locatable upstream, Annotation core) {
        return new XmlValueQuick(upstream, (XmlValue) core);
    }

    @Override // java.lang.annotation.Annotation
    public Class<XmlValue> annotationType() {
        return XmlValue.class;
    }
}
