package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;
import javax.xml.namespace.QName;

@XmlElement("element")
/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/xmlschema/TopLevelElement.class */
public interface TopLevelElement extends Element, TypedXmlWriter {
    @XmlAttribute("final")
    TopLevelElement _final(String[] strArr);

    @XmlAttribute("final")
    TopLevelElement _final(String str);

    @XmlAttribute("abstract")
    TopLevelElement _abstract(boolean z2);

    @XmlAttribute
    TopLevelElement substitutionGroup(QName qName);

    @XmlAttribute
    TopLevelElement name(String str);
}
