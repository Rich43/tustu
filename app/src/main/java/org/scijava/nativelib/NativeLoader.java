package org.scijava.nativelib;

import java.io.IOException;

/* loaded from: jssc.jar:org/scijava/nativelib/NativeLoader.class */
public class NativeLoader {
    private static JniExtractor jniExtractor;

    static {
        jniExtractor = null;
        try {
            if (NativeLoader.class.getClassLoader() == ClassLoader.getSystemClassLoader()) {
                jniExtractor = new DefaultJniExtractor(null);
            } else {
                jniExtractor = new WebappJniExtractor("Classloader");
            }
        } catch (IOException e2) {
            throw new ExceptionInInitializerError(e2);
        }
    }

    public static void loadLibrary(String libName, String... searchPaths) throws IOException {
        try {
            System.loadLibrary(libName);
        } catch (UnsatisfiedLinkError e2) {
            if (NativeLibraryUtil.loadNativeLibrary(jniExtractor, libName, searchPaths)) {
            } else {
                throw new IOException("Couldn't load library library " + libName, e2);
            }
        }
    }

    public static void extractRegistered() throws IOException {
        jniExtractor.extractRegistered();
    }

    public static JniExtractor getJniExtractor() {
        return jniExtractor;
    }

    public static void setJniExtractor(JniExtractor jniExtractor2) {
        jniExtractor = jniExtractor2;
    }
}
