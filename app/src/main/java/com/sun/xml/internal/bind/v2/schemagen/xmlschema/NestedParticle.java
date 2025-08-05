package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlElement;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/xmlschema/NestedParticle.class */
public interface NestedParticle extends TypedXmlWriter {
    @XmlElement
    LocalElement element();

    @XmlElement
    Any any();

    @XmlElement
    ExplicitGroup sequence();

    @XmlElement
    ExplicitGroup choice();
}
