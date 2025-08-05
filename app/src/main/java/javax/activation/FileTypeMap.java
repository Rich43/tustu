package javax.activation;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

/* loaded from: rt.jar:javax/activation/FileTypeMap.class */
public abstract class FileTypeMap {
    private static FileTypeMap defaultMap = null;
    private static Map<ClassLoader, FileTypeMap> map = new WeakHashMap();

    public abstract String getContentType(File file);

    public abstract String getContentType(String str);

    public static synchronized void setDefaultFileTypeMap(FileTypeMap fileTypeMap) {
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            try {
                security.checkSetFactory();
            } catch (SecurityException ex) {
                if (FileTypeMap.class.getClassLoader() == null || FileTypeMap.class.getClassLoader() != fileTypeMap.getClass().getClassLoader()) {
                    throw ex;
                }
            }
        }
        map.remove(SecuritySupport.getContextClassLoader());
        defaultMap = fileTypeMap;
    }

    public static synchronized FileTypeMap getDefaultFileTypeMap() {
        if (defaultMap != null) {
            return defaultMap;
        }
        ClassLoader tccl = SecuritySupport.getContextClassLoader();
        FileTypeMap def = map.get(tccl);
        if (def == null) {
            def = new MimetypesFileTypeMap();
            map.put(tccl, def);
        }
        return def;
    }
}
