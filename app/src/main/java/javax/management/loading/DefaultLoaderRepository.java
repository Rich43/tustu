package javax.management.loading;

import com.sun.jmx.defaults.JmxProperties;
import java.util.Iterator;
import java.util.logging.Level;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;

@Deprecated
/* loaded from: rt.jar:javax/management/loading/DefaultLoaderRepository.class */
public class DefaultLoaderRepository {
    public static Class<?> loadClass(String str) throws ClassNotFoundException {
        JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINEST, DefaultLoaderRepository.class.getName(), "loadClass", str);
        return load(null, str);
    }

    public static Class<?> loadClassWithout(ClassLoader classLoader, String str) throws ClassNotFoundException {
        JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINEST, DefaultLoaderRepository.class.getName(), "loadClassWithout", str);
        return load(classLoader, str);
    }

    private static Class<?> load(ClassLoader classLoader, String str) throws ClassNotFoundException {
        Iterator<MBeanServer> it = MBeanServerFactory.findMBeanServer(null).iterator();
        while (it.hasNext()) {
            try {
                return it.next().getClassLoaderRepository().loadClassWithout(classLoader, str);
            } catch (ClassNotFoundException e2) {
            }
        }
        throw new ClassNotFoundException(str);
    }
}
