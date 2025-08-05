package javax.tools;

import java.io.File;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: rt.jar:javax/tools/ToolProvider.class */
public class ToolProvider {
    private static final String propertyName = "sun.tools.ToolProvider";
    private static final String loggerName = "javax.tools";
    private static final String defaultJavaCompilerName = "com.sun.tools.javac.api.JavacTool";
    private static final String defaultDocumentationToolName = "com.sun.tools.javadoc.api.JavadocTool";
    private static ToolProvider instance;
    private Map<String, Reference<Class<?>>> toolClasses = new HashMap();
    private Reference<ClassLoader> refToolClassLoader = null;
    private static final String[] defaultToolsLocation = {"lib", "tools.jar"};

    static <T> T trace(Level level, Object obj) {
        try {
            if (System.getProperty(propertyName) != null) {
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                String str = "???";
                String name = ToolProvider.class.getName();
                if (stackTrace.length > 2) {
                    StackTraceElement stackTraceElement = stackTrace[2];
                    str = String.format((Locale) null, "%s(%s:%s)", stackTraceElement.getMethodName(), stackTraceElement.getFileName(), Integer.valueOf(stackTraceElement.getLineNumber()));
                    name = stackTraceElement.getClassName();
                }
                Logger logger = Logger.getLogger(loggerName);
                if (obj instanceof Throwable) {
                    logger.logp(level, name, str, obj.getClass().getName(), (Throwable) obj);
                } else {
                    logger.logp(level, name, str, String.valueOf(obj));
                }
            }
            return null;
        } catch (SecurityException e2) {
            System.err.format((Locale) null, "%s: %s; %s%n", ToolProvider.class.getName(), obj, e2.getLocalizedMessage());
            return null;
        }
    }

    public static JavaCompiler getSystemJavaCompiler() {
        return (JavaCompiler) instance().getSystemTool(JavaCompiler.class, defaultJavaCompilerName);
    }

    public static DocumentationTool getSystemDocumentationTool() {
        return (DocumentationTool) instance().getSystemTool(DocumentationTool.class, defaultDocumentationToolName);
    }

    public static ClassLoader getSystemToolClassLoader() {
        try {
            return instance().getSystemToolClass(JavaCompiler.class, defaultJavaCompilerName).getClassLoader();
        } catch (Throwable th) {
            return (ClassLoader) trace(Level.WARNING, th);
        }
    }

    private static synchronized ToolProvider instance() {
        if (instance == null) {
            instance = new ToolProvider();
        }
        return instance;
    }

    private ToolProvider() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <T> T getSystemTool(Class<T> cls, String str) {
        try {
            return (T) getSystemToolClass(cls, str).asSubclass(cls).newInstance();
        } catch (Throwable th) {
            trace(Level.WARNING, th);
            return null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <T> Class<? extends T> getSystemToolClass(Class<T> cls, String str) {
        Reference<Class<?>> reference = this.toolClasses.get(str);
        Class<?> clsFindSystemToolClass = reference == null ? null : reference.get();
        if (clsFindSystemToolClass == null) {
            try {
                clsFindSystemToolClass = findSystemToolClass(str);
                this.toolClasses.put(str, new WeakReference(clsFindSystemToolClass));
            } catch (Throwable th) {
                return (Class) trace(Level.WARNING, th);
            }
        }
        return (Class<? extends T>) clsFindSystemToolClass.asSubclass(cls);
    }

    private Class<?> findSystemToolClass(String str) throws MalformedURLException, ClassNotFoundException {
        try {
            return Class.forName(str, false, null);
        } catch (ClassNotFoundException e2) {
            trace(Level.FINE, e2);
            ClassLoader classLoaderNewInstance = this.refToolClassLoader == null ? null : this.refToolClassLoader.get();
            if (classLoaderNewInstance == null) {
                File file = new File(System.getProperty("java.home"));
                if (file.getName().equalsIgnoreCase("jre")) {
                    file = file.getParentFile();
                }
                for (String str2 : defaultToolsLocation) {
                    file = new File(file, str2);
                }
                if (!file.exists()) {
                    throw e2;
                }
                URL[] urlArr = {file.toURI().toURL()};
                trace(Level.FINE, urlArr[0].toString());
                classLoaderNewInstance = URLClassLoader.newInstance(urlArr);
                this.refToolClassLoader = new WeakReference(classLoaderNewInstance);
            }
            return Class.forName(str, false, classLoaderNewInstance);
        }
    }
}
