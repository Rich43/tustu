package com.sun.xml.internal.bind.v2.schemagen.episode;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.xml.internal.txw2.TypedXmlWriter;
import com.sun.xml.internal.txw2.annotation.XmlAttribute;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlElement("bindings")
/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/schemagen/episode/Bindings.class */
public interface Bindings extends TypedXmlWriter {
    @XmlElement
    Bindings bindings();

    @XmlElement(Constants.ATTRNAME_CLASS)
    Klass klass();

    Klass typesafeEnumClass();

    @XmlElement
    SchemaBindings schemaBindings();

    @XmlAttribute
    void scd(String str);

    @XmlAttribute
    void version(String str);
}
