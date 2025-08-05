package javax.xml.stream.events;

/* loaded from: rt.jar:javax/xml/stream/events/EntityDeclaration.class */
public interface EntityDeclaration extends XMLEvent {
    String getPublicId();

    String getSystemId();

    String getName();

    String getNotationName();

    String getReplacementText();

    String getBaseURI();
}
