package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/xmlschema/SimpleRestrictionModel.class */
public interface SimpleRestrictionModel extends SimpleTypeHost, TypedXmlWriter {
    @XmlAttribute
    SimpleRestrictionModel base(QName qName);

    @XmlElement
    NoFixedFacet enumeration();
}
