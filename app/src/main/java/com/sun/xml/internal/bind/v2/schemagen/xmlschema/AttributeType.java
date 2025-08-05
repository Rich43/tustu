package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/xmlschema/AttributeType.class */
public interface AttributeType extends SimpleTypeHost, TypedXmlWriter {
    @XmlAttribute
    AttributeType type(QName qName);
}
