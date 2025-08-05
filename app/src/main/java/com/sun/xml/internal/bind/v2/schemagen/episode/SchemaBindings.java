package com.sun.xml.internal.bind.v2.schemagen.episode;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/episode/SchemaBindings.class */
public interface SchemaBindings extends TypedXmlWriter {
    @XmlAttribute
    void map(boolean z2);

    @XmlElement("package")
    Package _package();
}
