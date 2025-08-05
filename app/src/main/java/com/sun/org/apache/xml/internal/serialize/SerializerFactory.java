package com.sun.org.apache.xml.internal.serialize;

import com.sun.org.apache.xerces.internal.utils.ObjectFactory;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serialize/SerializerFactory.class */
public abstract class SerializerFactory {
    public static final String FactoriesProperty = "com.sun.org.apache.xml.internal.serialize.factories";
    private static final Map<String, SerializerFactory> _factories = Collections.synchronizedMap(new HashMap());

    protected abstract String getSupportedMethod();

    public abstract Serializer makeSerializer(OutputFormat outputFormat);

    public abstract Serializer makeSerializer(Writer writer, OutputFormat outputFormat);

    public abstract Serializer makeSerializer(OutputStream outputStream, OutputFormat outputFormat) throws UnsupportedEncodingException;

    static {
        SerializerFactory factory = new SerializerFactoryImpl("xml");
        registerSerializerFactory(factory);
        SerializerFactory factory2 = new SerializerFactoryImpl("html");
        registerSerializerFactory(factory2);
        SerializerFactory factory3 = new SerializerFactoryImpl("xhtml");
        registerSerializerFactory(factory3);
        SerializerFactory factory4 = new SerializerFactoryImpl("text");
        registerSerializerFactory(factory4);
        String list = com.sun.org.apache.xerces.internal.utils.SecuritySupport.getSystemProperty(FactoriesProperty);
        if (list != null) {
            StringTokenizer token = new StringTokenizer(list, " ;,:");
            while (token.hasMoreTokens()) {
                String className = token.nextToken();
                try {
                    SerializerFactory factory5 = (SerializerFactory) ObjectFactory.newInstance(className, true);
                    if (_factories.containsKey(factory5.getSupportedMethod())) {
                        _factories.put(factory5.getSupportedMethod(), factory5);
                    }
                } catch (Exception e2) {
                }
            }
        }
    }

    public static void registerSerializerFactory(SerializerFactory factory) {
        synchronized (_factories) {
            String method = factory.getSupportedMethod();
            _factories.put(method, factory);
        }
    }

    public static SerializerFactory getSerializerFactory(String method) {
        return _factories.get(method);
    }
}
