package javax.management.loading;

import com.sun.jmx.defaults.JmxProperties;
import com.sun.jmx.defaults.ServiceName;
import com.sun.jmx.remote.util.EnvHelp;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.io.Externalizable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanRegistration;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.ServiceNotFoundException;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:javax/management/loading/MLet.class */
public class MLet extends URLClassLoader implements MLetMBean, MBeanRegistration, Externalizable {
    private static final long serialVersionUID = 3636148327800330130L;
    private MBeanServer server;
    private List<MLetContent> mletList;
    private String libraryDirectory;
    private ObjectName mletObjectName;
    private URL[] myUrls;
    private transient ClassLoaderRepository currentClr;
    private transient boolean delegateToCLR;
    private Map<String, Class<?>> primitiveClasses;

    public MLet() {
        this(new URL[0]);
    }

    public MLet(URL[] urlArr) {
        this(urlArr, true);
    }

    public MLet(URL[] urlArr, ClassLoader classLoader) {
        this(urlArr, classLoader, true);
    }

    public MLet(URL[] urlArr, ClassLoader classLoader, URLStreamHandlerFactory uRLStreamHandlerFactory) {
        this(urlArr, classLoader, uRLStreamHandlerFactory, true);
    }

    public MLet(URL[] urlArr, boolean z2) {
        super(urlArr);
        this.server = null;
        this.mletList = new ArrayList();
        this.mletObjectName = null;
        this.myUrls = null;
        this.primitiveClasses = new HashMap(8);
        this.primitiveClasses.put(Boolean.TYPE.toString(), Boolean.class);
        this.primitiveClasses.put(Character.TYPE.toString(), Character.class);
        this.primitiveClasses.put(Byte.TYPE.toString(), Byte.class);
        this.primitiveClasses.put(Short.TYPE.toString(), Short.class);
        this.primitiveClasses.put(Integer.TYPE.toString(), Integer.class);
        this.primitiveClasses.put(Long.TYPE.toString(), Long.class);
        this.primitiveClasses.put(Float.TYPE.toString(), Float.class);
        this.primitiveClasses.put(Double.TYPE.toString(), Double.class);
        init(z2);
    }

    public MLet(URL[] urlArr, ClassLoader classLoader, boolean z2) {
        super(urlArr, classLoader);
        this.server = null;
        this.mletList = new ArrayList();
        this.mletObjectName = null;
        this.myUrls = null;
        this.primitiveClasses = new HashMap(8);
        this.primitiveClasses.put(Boolean.TYPE.toString(), Boolean.class);
        this.primitiveClasses.put(Character.TYPE.toString(), Character.class);
        this.primitiveClasses.put(Byte.TYPE.toString(), Byte.class);
        this.primitiveClasses.put(Short.TYPE.toString(), Short.class);
        this.primitiveClasses.put(Integer.TYPE.toString(), Integer.class);
        this.primitiveClasses.put(Long.TYPE.toString(), Long.class);
        this.primitiveClasses.put(Float.TYPE.toString(), Float.class);
        this.primitiveClasses.put(Double.TYPE.toString(), Double.class);
        init(z2);
    }

    public MLet(URL[] urlArr, ClassLoader classLoader, URLStreamHandlerFactory uRLStreamHandlerFactory, boolean z2) {
        super(urlArr, classLoader, uRLStreamHandlerFactory);
        this.server = null;
        this.mletList = new ArrayList();
        this.mletObjectName = null;
        this.myUrls = null;
        this.primitiveClasses = new HashMap(8);
        this.primitiveClasses.put(Boolean.TYPE.toString(), Boolean.class);
        this.primitiveClasses.put(Character.TYPE.toString(), Character.class);
        this.primitiveClasses.put(Byte.TYPE.toString(), Byte.class);
        this.primitiveClasses.put(Short.TYPE.toString(), Short.class);
        this.primitiveClasses.put(Integer.TYPE.toString(), Integer.class);
        this.primitiveClasses.put(Long.TYPE.toString(), Long.class);
        this.primitiveClasses.put(Float.TYPE.toString(), Float.class);
        this.primitiveClasses.put(Double.TYPE.toString(), Double.class);
        init(z2);
    }

    private void init(boolean z2) {
        this.delegateToCLR = z2;
        try {
            this.libraryDirectory = System.getProperty(JmxProperties.MLET_LIB_DIR);
            if (this.libraryDirectory == null) {
                this.libraryDirectory = getTmpDir();
            }
        } catch (SecurityException e2) {
        }
    }

    @Override // java.net.URLClassLoader, javax.management.loading.MLetMBean
    public void addURL(URL url) {
        if (!Arrays.asList(getURLs()).contains(url)) {
            super.addURL(url);
        }
    }

    @Override // javax.management.loading.MLetMBean
    public void addURL(String str) throws ServiceNotFoundException {
        try {
            URL url = new URL(str);
            if (!Arrays.asList(getURLs()).contains(url)) {
                super.addURL(url);
            }
        } catch (MalformedURLException e2) {
            if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINEST)) {
                JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "addUrl", "Malformed URL: " + str, (Throwable) e2);
            }
            throw new ServiceNotFoundException("The specified URL is malformed");
        }
    }

    @Override // java.net.URLClassLoader, javax.management.loading.MLetMBean
    public URL[] getURLs() {
        return super.getURLs();
    }

    @Override // javax.management.loading.MLetMBean
    public Set<Object> getMBeansFromURL(URL url) throws ServiceNotFoundException {
        if (url == null) {
            throw new ServiceNotFoundException("The specified URL is null");
        }
        return getMBeansFromURL(url.toString());
    }

    @Override // javax.management.loading.MLetMBean
    public Set<Object> getMBeansFromURL(String str) throws ServiceNotFoundException {
        ObjectInstance objectInstance;
        if (this.server == null) {
            throw new IllegalStateException("This MLet MBean is not registered with an MBeanServer.");
        }
        if (str == null) {
            JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "getMBeansFromURL", "URL is null");
            throw new ServiceNotFoundException("The specified URL is null");
        }
        String strReplace = str.replace(File.separatorChar, '/');
        if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "getMBeansFromURL", "<URL = " + strReplace + ">");
        }
        try {
            this.mletList = new MLetParser().parseURL(strReplace);
            if (this.mletList.size() == 0) {
                String str2 = "File " + strReplace + " not found or MLET tag not defined in file";
                JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "getMBeansFromURL", str2);
                throw new ServiceNotFoundException(str2);
            }
            HashSet hashSet = new HashSet();
            for (MLetContent mLetContent : this.mletList) {
                String code = mLetContent.getCode();
                if (code != null && code.endsWith(".class")) {
                    code = code.substring(0, code.length() - 6);
                }
                String name = mLetContent.getName();
                URL codeBase = mLetContent.getCodeBase();
                String version = mLetContent.getVersion();
                String serializedObject = mLetContent.getSerializedObject();
                String jarFiles = mLetContent.getJarFiles();
                URL documentBase = mLetContent.getDocumentBase();
                if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINER)) {
                    JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "getMBeansFromURL", "\n\tMLET TAG     = " + ((Object) mLetContent.getAttributes()) + "\n\tCODEBASE     = " + ((Object) codeBase) + "\n\tARCHIVE      = " + jarFiles + "\n\tCODE         = " + code + "\n\tOBJECT       = " + serializedObject + "\n\tNAME         = " + name + "\n\tVERSION      = " + version + "\n\tDOCUMENT URL = " + ((Object) documentBase));
                }
                StringTokenizer stringTokenizer = new StringTokenizer(jarFiles, ",", false);
                while (stringTokenizer.hasMoreTokens()) {
                    String strTrim = stringTokenizer.nextToken().trim();
                    if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINER)) {
                        JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "getMBeansFromURL", "Load archive for codebase <" + ((Object) codeBase) + ">, file <" + strTrim + ">");
                    }
                    try {
                        codeBase = check(version, codeBase, strTrim, mLetContent);
                        try {
                            if (!Arrays.asList(getURLs()).contains(new URL(codeBase.toString() + strTrim))) {
                                addURL(((Object) codeBase) + strTrim);
                            }
                        } catch (MalformedURLException e2) {
                        }
                    } catch (Exception e3) {
                        JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "getMBeansFromURL", "Got unexpected exception", (Throwable) e3);
                        hashSet.add(e3);
                    }
                }
                if (code != null && serializedObject != null) {
                    JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "getMBeansFromURL", "CODE and OBJECT parameters cannot be specified at the same time in tag MLET");
                    hashSet.add(new Error("CODE and OBJECT parameters cannot be specified at the same time in tag MLET"));
                } else if (code == null && serializedObject == null) {
                    JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "getMBeansFromURL", "Either CODE or OBJECT parameter must be specified in tag MLET");
                    hashSet.add(new Error("Either CODE or OBJECT parameter must be specified in tag MLET"));
                } else {
                    if (code != null) {
                        try {
                            List<String> parameterTypes = mLetContent.getParameterTypes();
                            List<String> parameterValues = mLetContent.getParameterValues();
                            ArrayList arrayList = new ArrayList();
                            for (int i2 = 0; i2 < parameterTypes.size(); i2++) {
                                arrayList.add(constructParameter(parameterValues.get(i2), parameterTypes.get(i2)));
                            }
                            if (parameterTypes.isEmpty()) {
                                if (name == null) {
                                    objectInstance = this.server.createMBean(code, null, this.mletObjectName);
                                } else {
                                    objectInstance = this.server.createMBean(code, new ObjectName(name), this.mletObjectName);
                                }
                            } else {
                                Object[] array = arrayList.toArray();
                                String[] strArr = new String[parameterTypes.size()];
                                parameterTypes.toArray(strArr);
                                if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINEST)) {
                                    StringBuilder sb = new StringBuilder();
                                    for (int i3 = 0; i3 < strArr.length; i3++) {
                                        sb.append("\n\tSignature     = ").append(strArr[i3]).append("\t\nParams        = ").append(array[i3]);
                                    }
                                    JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "getMBeansFromURL", sb.toString());
                                }
                                if (name == null) {
                                    objectInstance = this.server.createMBean(code, null, this.mletObjectName, array, strArr);
                                } else {
                                    objectInstance = this.server.createMBean(code, new ObjectName(name), this.mletObjectName, array, strArr);
                                }
                            }
                        } catch (IOException e4) {
                            JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "getMBeansFromURL", "IOException", (Throwable) e4);
                            hashSet.add(e4);
                        } catch (Error e5) {
                            JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "getMBeansFromURL", "Error", (Throwable) e5);
                            hashSet.add(e5);
                        } catch (SecurityException e6) {
                            JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "getMBeansFromURL", "SecurityException", (Throwable) e6);
                            hashSet.add(e6);
                        } catch (InstanceAlreadyExistsException e7) {
                            JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "getMBeansFromURL", "InstanceAlreadyExistsException", (Throwable) e7);
                            hashSet.add(e7);
                        } catch (InstanceNotFoundException e8) {
                            JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "getMBeansFromURL", "InstanceNotFoundException", (Throwable) e8);
                            hashSet.add(e8);
                        } catch (MBeanRegistrationException e9) {
                            JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "getMBeansFromURL", "MBeanRegistrationException", (Throwable) e9);
                            hashSet.add(e9);
                        } catch (MBeanException e10) {
                            JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "getMBeansFromURL", "MBeanException", (Throwable) e10);
                            hashSet.add(e10);
                        } catch (NotCompliantMBeanException e11) {
                            JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "getMBeansFromURL", "NotCompliantMBeanException", (Throwable) e11);
                            hashSet.add(e11);
                        } catch (ReflectionException e12) {
                            JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "getMBeansFromURL", "ReflectionException", (Throwable) e12);
                            hashSet.add(e12);
                        } catch (Exception e13) {
                            JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "getMBeansFromURL", "Exception", (Throwable) e13);
                            hashSet.add(e13);
                        }
                    } else {
                        Object objLoadSerializedObject = loadSerializedObject(codeBase, serializedObject);
                        if (name == null) {
                            this.server.registerMBean(objLoadSerializedObject, null);
                        } else {
                            this.server.registerMBean(objLoadSerializedObject, new ObjectName(name));
                        }
                        objectInstance = new ObjectInstance(name, objLoadSerializedObject.getClass().getName());
                    }
                    hashSet.add(objectInstance);
                }
            }
            return hashSet;
        } catch (Exception e14) {
            String str3 = "Problems while parsing URL [" + strReplace + "], got exception [" + e14.toString() + "]";
            JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "getMBeansFromURL", str3);
            throw ((ServiceNotFoundException) EnvHelp.initCause(new ServiceNotFoundException(str3), e14));
        }
    }

    @Override // javax.management.loading.MLetMBean
    public synchronized String getLibraryDirectory() {
        return this.libraryDirectory;
    }

    @Override // javax.management.loading.MLetMBean
    public synchronized void setLibraryDirectory(String str) {
        this.libraryDirectory = str;
    }

    @Override // javax.management.MBeanRegistration
    public ObjectName preRegister(MBeanServer mBeanServer, ObjectName objectName) throws Exception {
        setMBeanServer(mBeanServer);
        if (objectName == null) {
            objectName = new ObjectName(mBeanServer.getDefaultDomain() + CallSiteDescriptor.TOKEN_DELIMITER + ServiceName.MLET);
        }
        this.mletObjectName = objectName;
        return this.mletObjectName;
    }

    @Override // javax.management.MBeanRegistration
    public void postRegister(Boolean bool) {
    }

    @Override // javax.management.MBeanRegistration
    public void preDeregister() throws Exception {
    }

    @Override // javax.management.MBeanRegistration
    public void postDeregister() {
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput objectOutput) throws UnsupportedOperationException, IOException {
        throw new UnsupportedOperationException("MLet.writeExternal");
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput objectInput) throws UnsupportedOperationException, IOException, ClassNotFoundException {
        throw new UnsupportedOperationException("MLet.readExternal");
    }

    public synchronized Class<?> loadClass(String str, ClassLoaderRepository classLoaderRepository) throws ClassNotFoundException {
        ClassLoaderRepository classLoaderRepository2 = this.currentClr;
        try {
            this.currentClr = classLoaderRepository;
            Class<?> clsLoadClass = loadClass(str);
            this.currentClr = classLoaderRepository2;
            return clsLoadClass;
        } catch (Throwable th) {
            this.currentClr = classLoaderRepository2;
            throw th;
        }
    }

    @Override // java.net.URLClassLoader, java.lang.ClassLoader
    protected Class<?> findClass(String str) throws ClassNotFoundException {
        return findClass(str, this.currentClr);
    }

    Class<?> findClass(String str, ClassLoaderRepository classLoaderRepository) throws ClassNotFoundException {
        Class<?> clsLoadClassBefore = null;
        JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "findClass", str);
        try {
            clsLoadClassBefore = super.findClass(str);
            if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINER)) {
                JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "findClass", "Class " + str + " loaded through MLet classloader");
            }
        } catch (ClassNotFoundException e2) {
            if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINEST)) {
                JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "findClass", "Class " + str + " not found locally");
            }
        }
        if (clsLoadClassBefore == null && this.delegateToCLR && classLoaderRepository != null) {
            try {
                if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINEST)) {
                    JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "findClass", "Class " + str + " : looking in CLR");
                }
                clsLoadClassBefore = classLoaderRepository.loadClassBefore(this, str);
                if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINER)) {
                    JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "findClass", "Class " + str + " loaded through the default classloader repository");
                }
            } catch (ClassNotFoundException e3) {
                if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINEST)) {
                    JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "findClass", "Class " + str + " not found in CLR");
                }
            }
        }
        if (clsLoadClassBefore == null) {
            JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "findClass", "Failed to load class " + str);
            throw new ClassNotFoundException(str);
        }
        return clsLoadClassBefore;
    }

    @Override // java.lang.ClassLoader
    protected String findLibrary(String str) {
        String strMapLibraryName = System.mapLibraryName(str);
        if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "findLibrary", "Search " + str + " in all JAR files");
        }
        if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "findLibrary", "loadLibraryAsResource(" + strMapLibraryName + ")");
        }
        String strLoadLibraryAsResource = loadLibraryAsResource(strMapLibraryName);
        if (strLoadLibraryAsResource != null) {
            if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINER)) {
                JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "findLibrary", strMapLibraryName + " loaded, absolute path = " + strLoadLibraryAsResource);
            }
            return strLoadLibraryAsResource;
        }
        String str2 = removeSpace(System.getProperty("os.name")) + File.separator + removeSpace(System.getProperty("os.arch")) + File.separator + removeSpace(System.getProperty("os.version")) + File.separator + "lib" + File.separator + strMapLibraryName;
        if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "findLibrary", "loadLibraryAsResource(" + str2 + ")");
        }
        String strLoadLibraryAsResource2 = loadLibraryAsResource(str2);
        if (strLoadLibraryAsResource2 != null) {
            if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINER)) {
                JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "findLibrary", str2 + " loaded, absolute path = " + strLoadLibraryAsResource2);
            }
            return strLoadLibraryAsResource2;
        }
        if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "findLibrary", str + " not found in any JAR file");
            JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "findLibrary", "Search " + str + " along the path specified as the java.library.path property");
            return null;
        }
        return null;
    }

    private String getTmpDir() {
        String property = System.getProperty("java.io.tmpdir");
        if (property != null) {
            return property;
        }
        File fileCreateTempFile = null;
        try {
            try {
                fileCreateTempFile = File.createTempFile("tmp", "jmx");
                if (fileCreateTempFile == null) {
                    if (fileCreateTempFile != null) {
                        try {
                            if (!fileCreateTempFile.delete()) {
                                JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "getTmpDir", "Failed to delete temp file");
                            }
                        } catch (Exception e2) {
                            JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "getTmpDir", "Failed to delete temporary file", (Throwable) e2);
                        }
                    }
                    return null;
                }
                File parentFile = fileCreateTempFile.getParentFile();
                if (parentFile == null) {
                    if (fileCreateTempFile != null) {
                        try {
                            if (!fileCreateTempFile.delete()) {
                                JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "getTmpDir", "Failed to delete temp file");
                            }
                        } catch (Exception e3) {
                            JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "getTmpDir", "Failed to delete temporary file", (Throwable) e3);
                        }
                    }
                    return null;
                }
                String absolutePath = parentFile.getAbsolutePath();
                if (fileCreateTempFile != null) {
                    try {
                        if (!fileCreateTempFile.delete()) {
                            JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "getTmpDir", "Failed to delete temp file");
                        }
                    } catch (Exception e4) {
                        JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "getTmpDir", "Failed to delete temporary file", (Throwable) e4);
                    }
                }
                return absolutePath;
            } catch (Exception e5) {
                JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "getTmpDir", "Failed to determine system temporary dir");
                if (fileCreateTempFile != null) {
                    try {
                        if (!fileCreateTempFile.delete()) {
                            JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "getTmpDir", "Failed to delete temp file");
                        }
                    } catch (Exception e6) {
                        JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "getTmpDir", "Failed to delete temporary file", (Throwable) e6);
                    }
                }
                return null;
            }
        } catch (Throwable th) {
            if (fileCreateTempFile != null) {
                try {
                    if (!fileCreateTempFile.delete()) {
                        JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "getTmpDir", "Failed to delete temp file");
                    }
                } catch (Exception e7) {
                    JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "getTmpDir", "Failed to delete temporary file", (Throwable) e7);
                }
            }
            throw th;
        }
    }

    /* JADX WARN: Finally extract failed */
    private synchronized String loadLibraryAsResource(String str) {
        try {
            InputStream resourceAsStream = getResourceAsStream(str.replace(File.separatorChar, '/'));
            if (resourceAsStream != null) {
                try {
                    File file = new File(this.libraryDirectory);
                    file.mkdirs();
                    File file2 = Files.createTempFile(file.toPath(), str + ".", null, new FileAttribute[0]).toFile();
                    file2.deleteOnExit();
                    FileOutputStream fileOutputStream = new FileOutputStream(file2);
                    try {
                        byte[] bArr = new byte[4096];
                        while (true) {
                            int i2 = resourceAsStream.read(bArr);
                            if (i2 < 0) {
                                break;
                            }
                            fileOutputStream.write(bArr, 0, i2);
                        }
                        fileOutputStream.close();
                        if (file2.exists()) {
                            String absolutePath = file2.getAbsolutePath();
                            resourceAsStream.close();
                            return absolutePath;
                        }
                        resourceAsStream.close();
                    } catch (Throwable th) {
                        fileOutputStream.close();
                        throw th;
                    }
                } catch (Throwable th2) {
                    resourceAsStream.close();
                    throw th2;
                }
            }
            return null;
        } catch (Exception e2) {
            JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "loadLibraryAsResource", "Failed to load library : " + str, (Throwable) e2);
            return null;
        }
    }

    private static String removeSpace(String str) {
        return str.trim().replace(" ", "");
    }

    protected URL check(String str, URL url, String str2, MLetContent mLetContent) throws Exception {
        return url;
    }

    private Object loadSerializedObject(URL url, String str) throws IOException, ClassNotFoundException {
        if (str != null) {
            str = str.replace(File.separatorChar, '/');
        }
        if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MLET_LOGGER.logp(Level.FINER, MLet.class.getName(), "loadSerializedObject", url.toString() + str);
        }
        InputStream resourceAsStream = getResourceAsStream(str);
        if (resourceAsStream != null) {
            try {
                MLetObjectInputStream mLetObjectInputStream = new MLetObjectInputStream(resourceAsStream, this);
                Object object = mLetObjectInputStream.readObject();
                mLetObjectInputStream.close();
                return object;
            } catch (IOException e2) {
                if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINEST)) {
                    JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "loadSerializedObject", "Exception while deserializing " + str, (Throwable) e2);
                }
                throw e2;
            } catch (ClassNotFoundException e3) {
                if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINEST)) {
                    JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "loadSerializedObject", "Exception while deserializing " + str, (Throwable) e3);
                }
                throw e3;
            }
        }
        if (JmxProperties.MLET_LOGGER.isLoggable(Level.FINEST)) {
            JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "loadSerializedObject", "Error: File " + str + " containing serialized object not found");
        }
        throw new Error("File " + str + " containing serialized object not found");
    }

    private Object constructParameter(String str, String str2) {
        Class<?> cls = this.primitiveClasses.get(str2);
        if (cls != null) {
            try {
                return cls.getConstructor(String.class).newInstance(str);
            } catch (Exception e2) {
                JmxProperties.MLET_LOGGER.logp(Level.FINEST, MLet.class.getName(), "constructParameter", "Got unexpected exception", (Throwable) e2);
            }
        }
        if (str2.compareTo(Constants.BOOLEAN_CLASS) == 0) {
            return Boolean.valueOf(str);
        }
        if (str2.compareTo("java.lang.Byte") == 0) {
            return new Byte(str);
        }
        if (str2.compareTo("java.lang.Short") == 0) {
            return new Short(str);
        }
        if (str2.compareTo("java.lang.Long") == 0) {
            return new Long(str);
        }
        if (str2.compareTo(Constants.INTEGER_CLASS) == 0) {
            return new Integer(str);
        }
        if (str2.compareTo("java.lang.Float") == 0) {
            return new Float(str);
        }
        if (str2.compareTo(Constants.DOUBLE_CLASS) == 0) {
            return new Double(str);
        }
        if (str2.compareTo("java.lang.String") == 0) {
            return str;
        }
        return str;
    }

    private synchronized void setMBeanServer(final MBeanServer mBeanServer) {
        this.server = mBeanServer;
        this.currentClr = (ClassLoaderRepository) AccessController.doPrivileged(new PrivilegedAction<ClassLoaderRepository>() { // from class: javax.management.loading.MLet.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public ClassLoaderRepository run2() {
                return mBeanServer.getClassLoaderRepository();
            }
        });
    }
}
