package javax.xml.stream;

import java.io.OutputStream;
import java.io.Writer;
import javax.xml.transform.Result;

/* loaded from: rt.jar:javax/xml/stream/XMLOutputFactory.class */
public abstract class XMLOutputFactory {
    public static final String IS_REPAIRING_NAMESPACES = "javax.xml.stream.isRepairingNamespaces";
    static final String DEFAULIMPL = "com.sun.xml.internal.stream.XMLOutputFactoryImpl";

    public abstract XMLStreamWriter createXMLStreamWriter(Writer writer) throws XMLStreamException;

    public abstract XMLStreamWriter createXMLStreamWriter(OutputStream outputStream) throws XMLStreamException;

    public abstract XMLStreamWriter createXMLStreamWriter(OutputStream outputStream, String str) throws XMLStreamException;

    public abstract XMLStreamWriter createXMLStreamWriter(Result result) throws XMLStreamException;

    public abstract XMLEventWriter createXMLEventWriter(Result result) throws XMLStreamException;

    public abstract XMLEventWriter createXMLEventWriter(OutputStream outputStream) throws XMLStreamException;

    public abstract XMLEventWriter createXMLEventWriter(OutputStream outputStream, String str) throws XMLStreamException;

    public abstract XMLEventWriter createXMLEventWriter(Writer writer) throws XMLStreamException;

    public abstract void setProperty(String str, Object obj) throws IllegalArgumentException;

    public abstract Object getProperty(String str) throws IllegalArgumentException;

    public abstract boolean isPropertySupported(String str);

    protected XMLOutputFactory() {
    }

    public static XMLOutputFactory newInstance() throws FactoryConfigurationError {
        return (XMLOutputFactory) FactoryFinder.find(XMLOutputFactory.class, DEFAULIMPL);
    }

    public static XMLOutputFactory newFactory() throws FactoryConfigurationError {
        return (XMLOutputFactory) FactoryFinder.find(XMLOutputFactory.class, DEFAULIMPL);
    }

    public static XMLInputFactory newInstance(String factoryId, ClassLoader classLoader) throws FactoryConfigurationError {
        return (XMLInputFactory) FactoryFinder.find(XMLInputFactory.class, factoryId, classLoader, null);
    }

    public static XMLOutputFactory newFactory(String factoryId, ClassLoader classLoader) throws FactoryConfigurationError {
        return (XMLOutputFactory) FactoryFinder.find(XMLOutputFactory.class, factoryId, classLoader, null);
    }
}
