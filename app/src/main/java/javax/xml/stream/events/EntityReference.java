package javax.xml.stream.events;

/* loaded from: rt.jar:javax/xml/stream/events/EntityReference.class */
public interface EntityReference extends XMLEvent {
    EntityDeclaration getDeclaration();

    String getName();
}
