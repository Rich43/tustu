package javax.xml.transform;

/* loaded from: rt.jar:javax/xml/transform/SourceLocator.class */
public interface SourceLocator {
    String getPublicId();

    String getSystemId();

    int getLineNumber();

    int getColumnNumber();
}
