package jdk.internal.util.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import sun.util.spi.XmlPropertiesProvider;

/* loaded from: rt.jar:jdk/internal/util/xml/BasicXmlPropertiesProvider.class */
public class BasicXmlPropertiesProvider extends XmlPropertiesProvider {
    @Override // sun.util.spi.XmlPropertiesProvider
    public void load(Properties properties, InputStream inputStream) throws IOException {
        new PropertiesDefaultHandler().load(properties, inputStream);
    }

    @Override // sun.util.spi.XmlPropertiesProvider
    public void store(Properties properties, OutputStream outputStream, String str, String str2) throws IOException {
        new PropertiesDefaultHandler().store(properties, outputStream, str, str2);
    }
}
