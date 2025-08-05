package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlElement("appinfo")
/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/xmlschema/Appinfo.class */
public interface Appinfo extends TypedXmlWriter {
    @XmlAttribute
    Appinfo source(String str);
}
