package javax.xml.stream;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.DTD;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.EntityDeclaration;
import javax.xml.stream.events.EntityReference;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.ProcessingInstruction;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;

/* loaded from: rt.jar:javax/xml/stream/XMLEventFactory.class */
public abstract class XMLEventFactory {
    static final String JAXPFACTORYID = "javax.xml.stream.XMLEventFactory";
    static final String DEFAULIMPL = "com.sun.xml.internal.stream.events.XMLEventFactoryImpl";

    public abstract void setLocation(Location location);

    public abstract Attribute createAttribute(String str, String str2, String str3, String str4);

    public abstract Attribute createAttribute(String str, String str2);

    public abstract Attribute createAttribute(QName qName, String str);

    public abstract Namespace createNamespace(String str);

    public abstract Namespace createNamespace(String str, String str2);

    public abstract StartElement createStartElement(QName qName, Iterator it, Iterator it2);

    public abstract StartElement createStartElement(String str, String str2, String str3);

    public abstract StartElement createStartElement(String str, String str2, String str3, Iterator it, Iterator it2);

    public abstract StartElement createStartElement(String str, String str2, String str3, Iterator it, Iterator it2, NamespaceContext namespaceContext);

    public abstract EndElement createEndElement(QName qName, Iterator it);

    public abstract EndElement createEndElement(String str, String str2, String str3);

    public abstract EndElement createEndElement(String str, String str2, String str3, Iterator it);

    public abstract Characters createCharacters(String str);

    public abstract Characters createCData(String str);

    public abstract Characters createSpace(String str);

    public abstract Characters createIgnorableSpace(String str);

    public abstract StartDocument createStartDocument();

    public abstract StartDocument createStartDocument(String str, String str2, boolean z2);

    public abstract StartDocument createStartDocument(String str, String str2);

    public abstract StartDocument createStartDocument(String str);

    public abstract EndDocument createEndDocument();

    public abstract EntityReference createEntityReference(String str, EntityDeclaration entityDeclaration);

    public abstract Comment createComment(String str);

    public abstract ProcessingInstruction createProcessingInstruction(String str, String str2);

    public abstract DTD createDTD(String str);

    protected XMLEventFactory() {
    }

    public static XMLEventFactory newInstance() throws FactoryConfigurationError {
        return (XMLEventFactory) FactoryFinder.find(XMLEventFactory.class, DEFAULIMPL);
    }

    public static XMLEventFactory newFactory() throws FactoryConfigurationError {
        return (XMLEventFactory) FactoryFinder.find(XMLEventFactory.class, DEFAULIMPL);
    }

    public static XMLEventFactory newInstance(String factoryId, ClassLoader classLoader) throws FactoryConfigurationError {
        return (XMLEventFactory) FactoryFinder.find(XMLEventFactory.class, factoryId, classLoader, null);
    }

    public static XMLEventFactory newFactory(String factoryId, ClassLoader classLoader) throws FactoryConfigurationError {
        return (XMLEventFactory) FactoryFinder.find(XMLEventFactory.class, factoryId, classLoader, null);
    }
}
