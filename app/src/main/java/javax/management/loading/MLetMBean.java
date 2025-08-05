package javax.management.loading;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;
import javax.management.ServiceNotFoundException;

/* loaded from: rt.jar:javax/management/loading/MLetMBean.class */
public interface MLetMBean {
    Set<Object> getMBeansFromURL(String str) throws ServiceNotFoundException;

    Set<Object> getMBeansFromURL(URL url) throws ServiceNotFoundException;

    void addURL(URL url);

    void addURL(String str) throws ServiceNotFoundException;

    URL[] getURLs();

    URL getResource(String str);

    InputStream getResourceAsStream(String str);

    Enumeration<URL> getResources(String str) throws IOException;

    String getLibraryDirectory();

    void setLibraryDirectory(String str);
}
