package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlElement("attribute")
/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/xmlschema/TopLevelAttribute.class */
public interface TopLevelAttribute extends Annotated, AttributeType, FixedOrDefault, TypedXmlWriter {
    @XmlAttribute
    TopLevelAttribute name(String str);
}
