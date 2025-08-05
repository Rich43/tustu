package sun.util.spi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/* loaded from: rt.jar:sun/util/spi/XmlPropertiesProvider.class */
public abstract class XmlPropertiesProvider {
    public abstract void load(Properties properties, InputStream inputStream) throws IOException;

    public abstract void store(Properties properties, OutputStream outputStream, String str, String str2) throws IOException;

    protected XmlPropertiesProvider() {
    }
}
