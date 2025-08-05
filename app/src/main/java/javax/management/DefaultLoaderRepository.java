package javax.management;

@Deprecated
/* loaded from: rt.jar:javax/management/DefaultLoaderRepository.class */
public class DefaultLoaderRepository {
    public static Class<?> loadClass(String str) throws ClassNotFoundException {
        return javax.management.loading.DefaultLoaderRepository.loadClass(str);
    }

    public static Class<?> loadClassWithout(ClassLoader classLoader, String str) throws ClassNotFoundException {
        return javax.management.loading.DefaultLoaderRepository.loadClassWithout(classLoader, str);
    }
}
