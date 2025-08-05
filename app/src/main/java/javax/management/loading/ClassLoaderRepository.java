package javax.management.loading;

/* loaded from: rt.jar:javax/management/loading/ClassLoaderRepository.class */
public interface ClassLoaderRepository {
    Class<?> loadClass(String str) throws ClassNotFoundException;

    Class<?> loadClassWithout(ClassLoader classLoader, String str) throws ClassNotFoundException;

    Class<?> loadClassBefore(ClassLoader classLoader, String str) throws ClassNotFoundException;
}
