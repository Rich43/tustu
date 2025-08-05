package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlElement("documentation")
/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/xmlschema/Documentation.class */
public interface Documentation extends TypedXmlWriter {
    @XmlAttribute
    Documentation source(String str);

    @XmlAttribute(ns = "http://www.w3.org/XML/1998/namespace")
    Documentation lang(String str);
}
