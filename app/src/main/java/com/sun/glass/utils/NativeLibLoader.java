package com.sun.glass.utils;

import com.sun.glass.ui.Platform;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.File;
import java.net.URI;
import java.security.AccessController;
import java.util.HashSet;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:com/sun/glass/utils/NativeLibLoader.class */
public class NativeLibLoader {
    private static final HashSet<String> loaded = new HashSet<>();
    private static boolean verbose = false;
    private static File libDir = null;
    private static String libPrefix = "";
    private static String libSuffix = "";

    static {
        AccessController.doPrivileged(() -> {
            verbose = Boolean.getBoolean("javafx.verbose");
            return null;
        });
    }

    public static synchronized void loadLibrary(String libname) {
        if (!loaded.contains(libname)) {
            loadLibraryInternal(libname);
            loaded.add(libname);
        }
    }

    private static String[] initializePath(String propname) {
        String ldpath = System.getProperty(propname, "");
        String ps = File.pathSeparator;
        int ldlen = ldpath.length();
        int n2 = 0;
        for (int i2 = ldpath.indexOf(ps); i2 >= 0; i2 = ldpath.indexOf(ps, i2 + 1)) {
            n2++;
        }
        String[] paths = new String[n2 + 1];
        int i3 = 0;
        int n3 = 0;
        int iIndexOf = ldpath.indexOf(ps);
        while (true) {
            int j2 = iIndexOf;
            if (j2 >= 0) {
                if (j2 - i3 > 0) {
                    int i4 = n3;
                    n3++;
                    paths[i4] = ldpath.substring(i3, j2);
                } else if (j2 - i3 == 0) {
                    int i5 = n3;
                    n3++;
                    paths[i5] = ".";
                }
                i3 = j2 + 1;
                iIndexOf = ldpath.indexOf(ps, i3);
            } else {
                paths[n3] = ldpath.substring(i3, ldlen);
                return paths;
            }
        }
    }

    private static void loadLibraryInternal(String libraryName) {
        try {
            loadLibraryFullPath(libraryName);
        } catch (UnsatisfiedLinkError ex) {
            String[] libPath = initializePath("java.library.path");
            for (String path : libPath) {
                try {
                    if (!path.endsWith(File.separator)) {
                        path = path + File.separator;
                    }
                    String fileName = System.mapLibraryName(libraryName);
                    File libFile = new File(path + fileName);
                    System.load(libFile.getAbsolutePath());
                    if (verbose) {
                        System.err.println("Loaded " + libFile.getAbsolutePath() + " from java.library.path");
                        return;
                    }
                    return;
                } catch (UnsatisfiedLinkError e2) {
                }
            }
            try {
                System.loadLibrary(libraryName);
                if (verbose) {
                    System.err.println("WARNING: " + ex.toString());
                    System.err.println("    using System.loadLibrary(" + libraryName + ") as a fallback");
                }
            } catch (UnsatisfiedLinkError e3) {
                if ("iOS".equals(System.getProperty("os.name")) && libraryName.contains(LanguageTag.SEP)) {
                    try {
                        System.loadLibrary(libraryName.replace(LanguageTag.SEP, "_"));
                        return;
                    } catch (UnsatisfiedLinkError ex3) {
                        throw ex3;
                    }
                }
                throw ex;
            }
        }
    }

    private static void loadLibraryFullPath(String libraryName) {
        try {
            if (libDir == null) {
                String classUrlString = NativeLibLoader.class.getResource("NativeLibLoader.class").toString();
                if (!classUrlString.startsWith("jar:file:") || classUrlString.indexOf(33) == -1) {
                    throw new UnsatisfiedLinkError("Invalid URL for class: " + classUrlString);
                }
                String tmpStr = classUrlString.substring(4, classUrlString.lastIndexOf(33));
                int lastIndexOfSlash = Math.max(tmpStr.lastIndexOf(47), tmpStr.lastIndexOf(92));
                String osName = System.getProperty("os.name");
                String relativeDir = null;
                if (osName.startsWith("Windows")) {
                    relativeDir = "../../bin";
                } else if (osName.startsWith(Platform.MAC)) {
                    relativeDir = Constants.ATTRVAL_PARENT;
                } else if (osName.startsWith("Linux")) {
                    relativeDir = "../" + System.getProperty("os.arch");
                }
                String libDirUrlString = tmpStr.substring(0, lastIndexOfSlash) + "/" + relativeDir;
                libDir = new File(new URI(libDirUrlString).getPath());
                if (osName.startsWith("Windows")) {
                    libPrefix = "";
                    libSuffix = ".dll";
                } else if (osName.startsWith(Platform.MAC)) {
                    libPrefix = "lib";
                    libSuffix = ".dylib";
                } else if (osName.startsWith("Linux")) {
                    libPrefix = "lib";
                    libSuffix = ".so";
                }
            }
            File libFile = new File(libDir, libPrefix + libraryName + libSuffix);
            String libFileName = libFile.getCanonicalPath();
            try {
                System.load(libFileName);
                if (verbose) {
                    System.err.println("Loaded " + libFile.getAbsolutePath() + " from relative path");
                }
            } catch (UnsatisfiedLinkError ex) {
                throw ex;
            }
        } catch (Exception e2) {
            throw ((UnsatisfiedLinkError) new UnsatisfiedLinkError().initCause(e2));
        }
    }
}
