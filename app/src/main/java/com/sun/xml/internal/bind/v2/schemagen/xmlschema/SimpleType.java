package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlElement("simpleType")
/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/xmlschema/SimpleType.class */
public interface SimpleType extends Annotated, SimpleDerivation, TypedXmlWriter {
    @XmlAttribute("final")
    SimpleType _final(String str);

    @XmlAttribute("final")
    SimpleType _final(String[] strArr);

    @XmlAttribute
    SimpleType name(String str);
}
