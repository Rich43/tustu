package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlElement("complexContent")
/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/xmlschema/ComplexContent.class */
public interface ComplexContent extends Annotated, TypedXmlWriter {
    @XmlElement
    ComplexExtension extension();

    @XmlElement
    ComplexRestriction restriction();

    @XmlAttribute
    ComplexContent mixed(boolean z2);
}
