package com.sun.xml.internal.bind.v2.schemagen.xmlschema;

import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlElement("import")
/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/xmlschema/Import.class */
public interface Import extends Annotated, TypedXmlWriter {
    @XmlAttribute
    Import namespace(String str);

    @XmlAttribute
    Import schemaLocation(String str);
}
