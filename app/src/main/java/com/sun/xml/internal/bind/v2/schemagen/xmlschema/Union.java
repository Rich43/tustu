package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;
import javax.xml.namespace.QName;

@XmlElement("union")
/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/xmlschema/Union.class */
public interface Union extends Annotated, SimpleTypeHost, TypedXmlWriter {
    @XmlAttribute
    Union memberTypes(QName[] qNameArr);
}
