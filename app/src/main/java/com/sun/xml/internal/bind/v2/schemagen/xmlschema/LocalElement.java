package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;
import javax.xml.namespace.QName;

@XmlElement("element")
/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/xmlschema/LocalElement.class */
public interface LocalElement extends Element, Occurs, TypedXmlWriter {
    @XmlAttribute
    LocalElement form(String str);

    @XmlAttribute
    LocalElement name(String str);

    @XmlAttribute
    LocalElement ref(QName qName);
}
