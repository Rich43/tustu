package jdk.internal.org.xml.sax;

/* loaded from: rt.jar:jdk/internal/org/xml/sax/Locator.class */
public interface Locator {
    String getPublicId();

    String getSystemId();

    int getLineNumber();

    int getColumnNumber();
}
