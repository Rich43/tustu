package javax.xml.stream.events;

/* loaded from: rt.jar:javax/xml/stream/events/StartDocument.class */
public interface StartDocument extends XMLEvent {
    String getSystemId();

    String getCharacterEncodingScheme();

    boolean encodingSet();

    boolean isStandalone();

    boolean standaloneSet();

    String getVersion();
}
