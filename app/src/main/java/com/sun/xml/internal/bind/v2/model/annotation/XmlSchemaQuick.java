package com.sun.xml.internal.bind.v2.model.annotation;

import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/annotation/XmlSchemaQuick.class */
final class XmlSchemaQuick extends Quick implements XmlSchema {
    private final XmlSchema core;

    public XmlSchemaQuick(Locatable upstream, XmlSchema core) {
        super(upstream);
        this.core = core;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Quick
    protected Annotation getAnnotation() {
        return this.core;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Quick
    protected Quick newInstance(Locatable upstream, Annotation core) {
        return new XmlSchemaQuick(upstream, (XmlSchema) core);
    }

    @Override // java.lang.annotation.Annotation
    public Class<XmlSchema> annotationType() {
        return XmlSchema.class;
    }

    @Override // javax.xml.bind.annotation.XmlSchema
    public String location() {
        return this.core.location();
    }

    @Override // javax.xml.bind.annotation.XmlSchema
    public String namespace() {
        return this.core.namespace();
    }

    @Override // javax.xml.bind.annotation.XmlSchema
    public XmlNs[] xmlns() {
        return this.core.xmlns();
    }

    @Override // javax.xml.bind.annotation.XmlSchema
    public XmlNsForm elementFormDefault() {
        return this.core.elementFormDefault();
    }

    @Override // javax.xml.bind.annotation.XmlSchema
    public XmlNsForm attributeFormDefault() {
        return this.core.attributeFormDefault();
    }
}
