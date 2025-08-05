package javax.xml.stream.events;

/* loaded from: rt.jar:javax/xml/stream/events/Characters.class */
public interface Characters extends XMLEvent {
    String getData();

    boolean isWhiteSpace();

    boolean isCData();

    boolean isIgnorableWhiteSpace();
}
