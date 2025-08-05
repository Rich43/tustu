package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/xmlschema/Element.class */
public interface Element extends Annotated, ComplexTypeHost, FixedOrDefault, SimpleTypeHost, TypedXmlWriter {
    @XmlAttribute
    Element type(QName qName);

    @XmlAttribute
    Element block(String[] strArr);

    @XmlAttribute
    Element block(String str);

    @XmlAttribute
    Element nillable(boolean z2);
}
