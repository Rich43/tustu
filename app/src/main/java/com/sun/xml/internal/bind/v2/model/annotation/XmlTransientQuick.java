package com.sun.xml.internal.bind.v2.model.annotation;

import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlTransient;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/annotation/XmlTransientQuick.class */
final class XmlTransientQuick extends Quick implements XmlTransient {
    private final XmlTransient core;

    public XmlTransientQuick(Locatable upstream, XmlTransient core) {
        super(upstream);
        this.core = core;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Quick
    protected Annotation getAnnotation() {
        return this.core;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Quick
    protected Quick newInstance(Locatable upstream, Annotation core) {
        return new XmlTransientQuick(upstream, (XmlTransient) core);
    }

    @Override // java.lang.annotation.Annotation
    public Class<XmlTransient> annotationType() {
        return XmlTransient.class;
    }
}
