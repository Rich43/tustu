package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlElement;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/xmlschema/SimpleDerivation.class */
public interface SimpleDerivation extends TypedXmlWriter {
    @XmlElement
    SimpleRestriction restriction();

    @XmlElement
    Union union();

    @XmlElement
    List list();
}
