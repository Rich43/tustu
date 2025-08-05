package jdk.internal.org.xml.sax;

import java.io.IOException;

/* loaded from: rt.jar:jdk/internal/org/xml/sax/EntityResolver.class */
public interface EntityResolver {
    InputSource resolveEntity(String str, String str2) throws IOException, SAXException;
}
