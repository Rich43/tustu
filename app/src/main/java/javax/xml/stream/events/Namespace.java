package javax.xml.stream.events;

/* loaded from: rt.jar:javax/xml/stream/events/Namespace.class */
public interface Namespace extends Attribute {
    String getPrefix();

    String getNamespaceURI();

    boolean isDefaultNamespaceDeclaration();
}
