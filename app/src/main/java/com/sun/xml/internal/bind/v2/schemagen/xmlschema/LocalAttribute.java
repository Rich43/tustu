package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;
import javax.xml.namespace.QName;

@XmlElement("attribute")
/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/xmlschema/LocalAttribute.class */
public interface LocalAttribute extends Annotated, AttributeType, FixedOrDefault, TypedXmlWriter {
    @XmlAttribute
    LocalAttribute form(String str);

    @XmlAttribute
    LocalAttribute name(String str);

    @XmlAttribute
    LocalAttribute ref(QName qName);

    @XmlAttribute
    LocalAttribute use(String str);
}
