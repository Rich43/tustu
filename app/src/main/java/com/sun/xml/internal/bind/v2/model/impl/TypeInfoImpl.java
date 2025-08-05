package com.sun.xml.internal.bind.v2.model.impl;

import com.sun.xml.internal.bind.api.impl.NameConverter;
import com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader;
import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
import com.sun.xml.internal.bind.v2.model.core.TypeInfo;
import com.sun.xml.internal.bind.v2.model.nav.Navigator;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/impl/TypeInfoImpl.class */
abstract class TypeInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> implements TypeInfo<TypeT, ClassDeclT>, Locatable {
    private final Locatable upstream;
    protected final TypeInfoSetImpl<TypeT, ClassDeclT, FieldT, MethodT> owner;
    protected ModelBuilder<TypeT, ClassDeclT, FieldT, MethodT> builder;

    protected TypeInfoImpl(ModelBuilder<TypeT, ClassDeclT, FieldT, MethodT> builder, Locatable upstream) {
        this.builder = builder;
        this.owner = builder.typeInfoSet;
        this.upstream = upstream;
    }

    public Locatable getUpstream() {
        return this.upstream;
    }

    void link() {
        this.builder = null;
    }

    protected final Navigator<TypeT, ClassDeclT, FieldT, MethodT> nav() {
        return this.owner.nav;
    }

    protected final AnnotationReader<TypeT, ClassDeclT, FieldT, MethodT> reader() {
        return this.owner.reader;
    }

    protected final QName parseElementName(ClassDeclT clazz) {
        XmlRootElement e2 = (XmlRootElement) reader().getClassAnnotation(XmlRootElement.class, clazz, this);
        if (e2 == null) {
            return null;
        }
        String local = e2.name();
        if (local.equals("##default")) {
            local = NameConverter.standard.toVariableName(nav().getClassShortName(clazz));
        }
        String nsUri = e2.namespace();
        if (nsUri.equals("##default")) {
            XmlSchema xs = (XmlSchema) reader().getPackageAnnotation(XmlSchema.class, clazz, this);
            if (xs != null) {
                nsUri = xs.namespace();
            } else {
                nsUri = this.builder.defaultNsUri;
            }
        }
        return new QName(nsUri.intern(), local.intern());
    }

    protected final QName parseTypeName(ClassDeclT clazz) {
        return parseTypeName(clazz, (XmlType) reader().getClassAnnotation(XmlType.class, clazz, this));
    }

    protected final QName parseTypeName(ClassDeclT clazz, XmlType t2) {
        String nsUri = "##default";
        String local = "##default";
        if (t2 != null) {
            nsUri = t2.namespace();
            local = t2.name();
        }
        if (local.length() == 0) {
            return null;
        }
        if (local.equals("##default")) {
            local = NameConverter.standard.toVariableName(nav().getClassShortName(clazz));
        }
        if (nsUri.equals("##default")) {
            XmlSchema xs = (XmlSchema) reader().getPackageAnnotation(XmlSchema.class, clazz, this);
            if (xs != null) {
                nsUri = xs.namespace();
            } else {
                nsUri = this.builder.defaultNsUri;
            }
        }
        return new QName(nsUri.intern(), local.intern());
    }
}
