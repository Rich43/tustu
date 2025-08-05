package javax.xml.stream;

/* loaded from: rt.jar:javax/xml/stream/Location.class */
public interface Location {
    int getLineNumber();

    int getColumnNumber();

    int getCharacterOffset();

    String getPublicId();

    String getSystemId();
}
