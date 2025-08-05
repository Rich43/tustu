package com.sun.org.apache.xml.internal.serialize;

import java.io.OutputStream;
import java.io.Writer;
import java.util.MissingResourceException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serialize/XHTMLSerializer.class */
public class XHTMLSerializer extends HTMLSerializer {
    public XHTMLSerializer() {
        super(true, new OutputFormat("xhtml", (String) null, false));
    }

    public XHTMLSerializer(OutputFormat format) {
        super(true, format != null ? format : new OutputFormat("xhtml", (String) null, false));
    }

    public XHTMLSerializer(Writer writer, OutputFormat format) {
        super(true, format != null ? format : new OutputFormat("xhtml", (String) null, false));
        setOutputCharStream(writer);
    }

    public XHTMLSerializer(OutputStream output, OutputFormat format) {
        super(true, format != null ? format : new OutputFormat("xhtml", (String) null, false));
        setOutputByteStream(output);
    }

    @Override // com.sun.org.apache.xml.internal.serialize.HTMLSerializer, com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer, com.sun.org.apache.xml.internal.serialize.Serializer
    public void setOutputFormat(OutputFormat format) throws MissingResourceException {
        super.setOutputFormat(format != null ? format : new OutputFormat("xhtml", (String) null, false));
    }
}
