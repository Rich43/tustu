package com.sun.xml.internal.bind.v2.model.annotation;

import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlElementDecl;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/annotation/XmlElementDeclQuick.class */
final class XmlElementDeclQuick extends Quick implements XmlElementDecl {
    private final XmlElementDecl core;

    public XmlElementDeclQuick(Locatable upstream, XmlElementDecl core) {
        super(upstream);
        this.core = core;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Quick
    protected Annotation getAnnotation() {
        return this.core;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Quick
    protected Quick newInstance(Locatable upstream, Annotation core) {
        return new XmlElementDeclQuick(upstream, (XmlElementDecl) core);
    }

    @Override // java.lang.annotation.Annotation
    public Class<XmlElementDecl> annotationType() {
        return XmlElementDecl.class;
    }

    @Override // javax.xml.bind.annotation.XmlElementDecl
    public String name() {
        return this.core.name();
    }

    @Override // javax.xml.bind.annotation.XmlElementDecl
    public Class scope() {
        return this.core.scope();
    }

    @Override // javax.xml.bind.annotation.XmlElementDecl
    public String namespace() {
        return this.core.namespace();
    }

    @Override // javax.xml.bind.annotation.XmlElementDecl
    public String defaultValue() {
        return this.core.defaultValue();
    }

    @Override // javax.xml.bind.annotation.XmlElementDecl
    public String substitutionHeadNamespace() {
        return this.core.substitutionHeadNamespace();
    }

    @Override // javax.xml.bind.annotation.XmlElementDecl
    public String substitutionHeadName() {
        return this.core.substitutionHeadName();
    }
}
