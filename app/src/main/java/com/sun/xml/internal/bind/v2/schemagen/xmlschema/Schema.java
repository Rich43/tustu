package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlElement("schema")
/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/xmlschema/Schema.class */
public interface Schema extends SchemaTop, TypedXmlWriter {
    @XmlElement
    Annotation annotation();

    @XmlElement("import")
    Import _import();

    @XmlAttribute
    Schema targetNamespace(String str);

    @XmlAttribute(ns = "http://www.w3.org/XML/1998/namespace")
    Schema lang(String str);

    @XmlAttribute
    Schema id(String str);

    @XmlAttribute
    Schema elementFormDefault(String str);

    @XmlAttribute
    Schema attributeFormDefault(String str);

    @XmlAttribute
    Schema blockDefault(String[] strArr);

    @XmlAttribute
    Schema blockDefault(String str);

    @XmlAttribute
    Schema finalDefault(String[] strArr);

    @XmlAttribute
    Schema finalDefault(String str);

    @XmlAttribute
    Schema version(String str);
}
