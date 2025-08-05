package com.sun.xml.internal.bind.v2.model.annotation;

import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlSchemaType;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/annotation/XmlSchemaTypeQuick.class */
final class XmlSchemaTypeQuick extends Quick implements XmlSchemaType {
    private final XmlSchemaType core;

    public XmlSchemaTypeQuick(Locatable upstream, XmlSchemaType core) {
        super(upstream);
        this.core = core;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Quick
    protected Annotation getAnnotation() {
        return this.core;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Quick
    protected Quick newInstance(Locatable upstream, Annotation core) {
        return new XmlSchemaTypeQuick(upstream, (XmlSchemaType) core);
    }

    @Override // java.lang.annotation.Annotation
    public Class<XmlSchemaType> annotationType() {
        return XmlSchemaType.class;
    }

    @Override // javax.xml.bind.annotation.XmlSchemaType
    public String name() {
        return this.core.name();
    }

    @Override // javax.xml.bind.annotation.XmlSchemaType
    public Class type() {
        return this.core.type();
    }

    @Override // javax.xml.bind.annotation.XmlSchemaType
    public String namespace() {
        return this.core.namespace();
    }
}
