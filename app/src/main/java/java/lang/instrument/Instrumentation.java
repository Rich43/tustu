package java.lang.instrument;

import java.util.jar.JarFile;

/* loaded from: rt.jar:java/lang/instrument/Instrumentation.class */
public interface Instrumentation {
    void addTransformer(ClassFileTransformer classFileTransformer, boolean z2);

    void addTransformer(ClassFileTransformer classFileTransformer);

    boolean removeTransformer(ClassFileTransformer classFileTransformer);

    boolean isRetransformClassesSupported();

    void retransformClasses(Class<?>... clsArr) throws UnmodifiableClassException;

    boolean isRedefineClassesSupported();

    void redefineClasses(ClassDefinition... classDefinitionArr) throws ClassNotFoundException, UnmodifiableClassException;

    boolean isModifiableClass(Class<?> cls);

    Class[] getAllLoadedClasses();

    Class[] getInitiatedClasses(ClassLoader classLoader);

    long getObjectSize(Object obj);

    void appendToBootstrapClassLoaderSearch(JarFile jarFile);

    void appendToSystemClassLoaderSearch(JarFile jarFile);

    boolean isNativeMethodPrefixSupported();

    void setNativeMethodPrefix(ClassFileTransformer classFileTransformer, String str);
}
