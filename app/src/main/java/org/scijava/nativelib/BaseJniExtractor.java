package org.scijava.nativelib;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;
import org.icepdf.core.util.PdfOps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: jssc.jar:org/scijava/nativelib/BaseJniExtractor.class */
public abstract class BaseJniExtractor implements JniExtractor {
    private static final Logger LOGGER = LoggerFactory.getLogger("org.scijava.nativelib.BaseJniExtractor");
    protected static final String JAVA_TMPDIR = "java.io.tmpdir";
    protected static final String ALTR_TMPDIR = "./tmplib";
    protected static final String TMP_PREFIX = "nativelib-loader_";
    private static final String LEFTOVER_MIN_AGE = "org.scijava.nativelib.leftoverMinAgeMs";
    private static final long LEFTOVER_MIN_AGE_DEFAULT = 300000;
    private Class<?> libraryJarClass;
    private String[] nativeResourcePaths;

    public abstract File getNativeDir();

    public abstract File getJniDir();

    public BaseJniExtractor() throws IOException {
        init(null);
    }

    public BaseJniExtractor(Class<?> libraryJarClass) throws IOException {
        init(libraryJarClass);
    }

    private void init(Class<?> libraryJarClass) {
        this.libraryJarClass = libraryJarClass;
        String mxSysInfo = MxSysInfo.getMxSysInfo();
        if (mxSysInfo != null) {
            this.nativeResourcePaths = new String[]{NativeLibraryUtil.DEFAULT_SEARCH_PATH, "META-INF/lib/" + mxSysInfo + "/", "META-INF/lib/"};
        } else {
            this.nativeResourcePaths = new String[]{NativeLibraryUtil.DEFAULT_SEARCH_PATH, "META-INF/lib/"};
        }
        deleteLeftoverFiles();
    }

    private static boolean deleteRecursively(File directory) {
        File[] list;
        if (directory == null || (list = directory.listFiles()) == null) {
            return true;
        }
        for (File file : list) {
            if (file.isFile()) {
                if (!file.delete()) {
                    return false;
                }
            } else if (file.isDirectory() && !deleteRecursively(file)) {
                return false;
            }
        }
        return directory.delete();
    }

    protected static File getTempDir() throws IOException {
        File tmpDir = new File(System.getProperty(JAVA_TMPDIR, ALTR_TMPDIR));
        if (!tmpDir.isDirectory()) {
            tmpDir.mkdirs();
            if (!tmpDir.isDirectory()) {
                throw new IOException("Unable to create temporary directory " + ((Object) tmpDir));
            }
        }
        File tempFile = File.createTempFile(TMP_PREFIX, "");
        tempFile.delete();
        return tempFile;
    }

    @Override // org.scijava.nativelib.JniExtractor
    public File extractJni(String libPath, String libname) throws IOException {
        String altLibName;
        String mappedlibName = System.mapLibraryName(libname);
        debug("mappedLib is " + mappedlibName);
        if (null == this.libraryJarClass) {
            this.libraryJarClass = getClass();
        }
        String combinedPath = ((libPath.equals("") || libPath.endsWith("/")) ? libPath : libPath + "/") + mappedlibName;
        URL lib = this.libraryJarClass.getClassLoader().getResource(combinedPath);
        if (null == lib) {
            if (mappedlibName.endsWith(".jnilib")) {
                altLibName = mappedlibName.substring(0, mappedlibName.length() - 7) + ".dylib";
            } else if (mappedlibName.endsWith(".dylib")) {
                altLibName = mappedlibName.substring(0, mappedlibName.length() - 6) + ".jnilib";
            } else {
                altLibName = null;
            }
            if (altLibName != null) {
                lib = getClass().getClassLoader().getResource(libPath + altLibName);
                if (lib != null) {
                    mappedlibName = altLibName;
                }
            }
        }
        if (null != lib) {
            debug("URL is " + lib.toString());
            debug("URL path is " + lib.getPath());
            return extractResource(getJniDir(), lib, mappedlibName);
        }
        debug("Couldn't find resource " + combinedPath);
        return null;
    }

    @Override // org.scijava.nativelib.JniExtractor
    public void extractRegistered() throws IOException {
        debug("Extracting libraries registered in classloader " + ((Object) getClass().getClassLoader()));
        for (String nativeResourcePath : this.nativeResourcePaths) {
            Enumeration<URL> resources = getClass().getClassLoader().getResources(nativeResourcePath + "AUTOEXTRACT.LIST");
            while (resources.hasMoreElements()) {
                URL res = resources.nextElement2();
                extractLibrariesFromResource(res);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x00b5, code lost:
    
        throw new java.io.IOException("Couldn't find native library " + r0 + "on the classpath");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void extractLibrariesFromResource(java.net.URL r8) throws java.io.IOException {
        /*
            r7 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            java.lang.String r1 = "Extracting libraries listed in "
            java.lang.StringBuilder r0 = r0.append(r1)
            r1 = r8
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            debug(r0)
            r0 = 0
            r9 = r0
            java.io.BufferedReader r0 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> Lc4
            r1 = r0
            java.io.InputStreamReader r2 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> Lc4
            r3 = r2
            r4 = r8
            java.io.InputStream r4 = r4.openStream()     // Catch: java.lang.Throwable -> Lc4
            java.lang.String r5 = "UTF-8"
            r3.<init>(r4, r5)     // Catch: java.lang.Throwable -> Lc4
            r1.<init>(r2)     // Catch: java.lang.Throwable -> Lc4
            r9 = r0
        L2d:
            r0 = r9
            java.lang.String r0 = r0.readLine()     // Catch: java.lang.Throwable -> Lc4
            r1 = r0
            r10 = r1
            if (r0 == 0) goto Lb9
            r0 = 0
            r11 = r0
            r0 = r7
            java.lang.String[] r0 = r0.nativeResourcePaths     // Catch: java.lang.Throwable -> Lc4
            r12 = r0
            r0 = r12
            int r0 = r0.length     // Catch: java.lang.Throwable -> Lc4
            r13 = r0
            r0 = 0
            r14 = r0
        L47:
            r0 = r14
            r1 = r13
            if (r0 >= r1) goto L82
            r0 = r12
            r1 = r14
            r0 = r0[r1]     // Catch: java.lang.Throwable -> Lc4
            r15 = r0
            r0 = r7
            java.lang.Class r0 = r0.getClass()     // Catch: java.lang.Throwable -> Lc4
            java.lang.ClassLoader r0 = r0.getClassLoader()     // Catch: java.lang.Throwable -> Lc4
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lc4
            r2 = r1
            r2.<init>()     // Catch: java.lang.Throwable -> Lc4
            r2 = r15
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch: java.lang.Throwable -> Lc4
            r2 = r10
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch: java.lang.Throwable -> Lc4
            java.lang.String r1 = r1.toString()     // Catch: java.lang.Throwable -> Lc4
            java.net.URL r0 = r0.getResource(r1)     // Catch: java.lang.Throwable -> Lc4
            r11 = r0
            r0 = r11
            if (r0 == 0) goto L7c
            goto L82
        L7c:
            int r14 = r14 + 1
            goto L47
        L82:
            r0 = r11
            if (r0 == 0) goto L96
            r0 = r7
            r1 = r7
            java.io.File r1 = r1.getNativeDir()     // Catch: java.lang.Throwable -> Lc4
            r2 = r11
            r3 = r10
            java.io.File r0 = r0.extractResource(r1, r2, r3)     // Catch: java.lang.Throwable -> Lc4
            goto Lb6
        L96:
            java.io.IOException r0 = new java.io.IOException     // Catch: java.lang.Throwable -> Lc4
            r1 = r0
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lc4
            r3 = r2
            r3.<init>()     // Catch: java.lang.Throwable -> Lc4
            java.lang.String r3 = "Couldn't find native library "
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Throwable -> Lc4
            r3 = r10
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Throwable -> Lc4
            java.lang.String r3 = "on the classpath"
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Throwable -> Lc4
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Throwable -> Lc4
            r1.<init>(r2)     // Catch: java.lang.Throwable -> Lc4
            throw r0     // Catch: java.lang.Throwable -> Lc4
        Lb6:
            goto L2d
        Lb9:
            r0 = r9
            if (r0 == 0) goto Ld1
            r0 = r9
            r0.close()
            goto Ld1
        Lc4:
            r16 = move-exception
            r0 = r9
            if (r0 == 0) goto Lce
            r0 = r9
            r0.close()
        Lce:
            r0 = r16
            throw r0
        Ld1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.scijava.nativelib.BaseJniExtractor.extractLibrariesFromResource(java.net.URL):void");
    }

    File extractResource(File dir, URL resource, String outputName) throws IOException {
        InputStream in = null;
        try {
            in = resource.openStream();
            File outfile = new File(getJniDir(), outputName);
            debug("Extracting '" + ((Object) resource) + "' to '" + outfile.getAbsolutePath() + PdfOps.SINGLE_QUOTE_TOKEN);
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(outfile);
                copy(in, out);
                if (out != null) {
                    out.close();
                }
                outfile.deleteOnExit();
                if (in != null) {
                    in.close();
                }
                return outfile;
            } catch (Throwable th) {
                if (out != null) {
                    out.close();
                }
                throw th;
            }
        } catch (Throwable th2) {
            if (in != null) {
                in.close();
            }
            throw th2;
        }
    }

    void deleteLeftoverFiles() {
        File tmpDirectory = new File(System.getProperty(JAVA_TMPDIR, ALTR_TMPDIR));
        File[] folders = tmpDirectory.listFiles(new FilenameFilter() { // from class: org.scijava.nativelib.BaseJniExtractor.1
            @Override // java.io.FilenameFilter
            public boolean accept(File dir, String name) {
                return name.startsWith(BaseJniExtractor.TMP_PREFIX);
            }
        });
        if (folders == null) {
            return;
        }
        long leftoverMinAge = getLeftoverMinAge();
        for (File folder : folders) {
            long age = System.currentTimeMillis() - folder.lastModified();
            if (age < leftoverMinAge) {
                debug("Not deleting leftover folder " + ((Object) folder) + ": is " + age + "ms old");
            } else {
                debug("Deleting leftover folder: " + ((Object) folder));
                deleteRecursively(folder);
            }
        }
    }

    long getLeftoverMinAge() {
        try {
            return Long.parseLong(System.getProperty(LEFTOVER_MIN_AGE, String.valueOf(LEFTOVER_MIN_AGE_DEFAULT)));
        } catch (NumberFormatException e2) {
            error("Cannot load leftover minimal age system property", e2);
            return LEFTOVER_MIN_AGE_DEFAULT;
        }
    }

    static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] tmp = new byte[8192];
        while (true) {
            int len = in.read(tmp);
            if (len > 0) {
                out.write(tmp, 0, len);
            } else {
                return;
            }
        }
    }

    private static void debug(String message) {
        LOGGER.debug(message);
    }

    private static void error(String message, Throwable t2) {
        LOGGER.error(message, t2);
    }
}
