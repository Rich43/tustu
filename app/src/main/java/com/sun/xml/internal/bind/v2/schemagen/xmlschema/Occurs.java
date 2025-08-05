package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/xmlschema/Occurs.class */
public interface Occurs extends TypedXmlWriter {
    @XmlAttribute
    Occurs minOccurs(int i2);

    @XmlAttribute
    Occurs maxOccurs(String str);

    @XmlAttribute
    Occurs maxOccurs(int i2);
}
