package com.sun.org.apache.xml.internal.serialize;

import com.sun.org.apache.xerces.internal.dom.DOMMessageFormatter;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.MissingResourceException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serialize/SerializerFactoryImpl.class */
final class SerializerFactoryImpl extends SerializerFactory {
    private String _method;

    SerializerFactoryImpl(String method) throws MissingResourceException {
        this._method = method;
        if (!this._method.equals("xml") && !this._method.equals("html") && !this._method.equals("xhtml") && !this._method.equals("text")) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "MethodNotSupported", new Object[]{method});
            throw new IllegalArgumentException(msg);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serialize.SerializerFactory
    public Serializer makeSerializer(OutputFormat format) throws MissingResourceException {
        Serializer serializer = getSerializer(format);
        serializer.setOutputFormat(format);
        return serializer;
    }

    @Override // com.sun.org.apache.xml.internal.serialize.SerializerFactory
    public Serializer makeSerializer(Writer writer, OutputFormat format) throws MissingResourceException {
        Serializer serializer = getSerializer(format);
        serializer.setOutputCharStream(writer);
        return serializer;
    }

    @Override // com.sun.org.apache.xml.internal.serialize.SerializerFactory
    public Serializer makeSerializer(OutputStream output, OutputFormat format) throws MissingResourceException, UnsupportedEncodingException {
        Serializer serializer = getSerializer(format);
        serializer.setOutputByteStream(output);
        return serializer;
    }

    private Serializer getSerializer(OutputFormat format) throws MissingResourceException {
        if (this._method.equals("xml")) {
            return new XMLSerializer(format);
        }
        if (this._method.equals("html")) {
            return new HTMLSerializer(format);
        }
        if (this._method.equals("xhtml")) {
            return new XHTMLSerializer(format);
        }
        if (this._method.equals("text")) {
            return new TextSerializer();
        }
        String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "MethodNotSupported", new Object[]{this._method});
        throw new IllegalStateException(msg);
    }

    @Override // com.sun.org.apache.xml.internal.serialize.SerializerFactory
    protected String getSupportedMethod() {
        return this._method;
    }
}
