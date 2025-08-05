package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlElement("complexType")
/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/xmlschema/ComplexType.class */
public interface ComplexType extends Annotated, ComplexTypeModel, TypedXmlWriter {
    @XmlAttribute("final")
    ComplexType _final(String[] strArr);

    @XmlAttribute("final")
    ComplexType _final(String str);

    @XmlAttribute
    ComplexType block(String[] strArr);

    @XmlAttribute
    ComplexType block(String str);

    @XmlAttribute("abstract")
    ComplexType _abstract(boolean z2);

    @XmlAttribute
    ComplexType name(String str);
}
