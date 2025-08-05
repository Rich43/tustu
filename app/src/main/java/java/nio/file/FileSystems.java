package java.nio.file;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.IOException;
import java.net.URI;
import java.nio.file.spi.FileSystemProvider;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import sun.nio.fs.DefaultFileSystemProvider;

/* loaded from: rt.jar:java/nio/file/FileSystems.class */
public final class FileSystems {
    private FileSystems() {
    }

    /* loaded from: rt.jar:java/nio/file/FileSystems$DefaultFileSystemHolder.class */
    private static class DefaultFileSystemHolder {
        static final FileSystem defaultFileSystem = defaultFileSystem();

        private DefaultFileSystemHolder() {
        }

        private static FileSystem defaultFileSystem() {
            return ((FileSystemProvider) AccessController.doPrivileged(new PrivilegedAction<FileSystemProvider>() { // from class: java.nio.file.FileSystems.DefaultFileSystemHolder.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public FileSystemProvider run2() {
                    return DefaultFileSystemHolder.getDefaultProvider();
                }
            })).getFileSystem(URI.create("file:///"));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static FileSystemProvider getDefaultProvider() {
            FileSystemProvider fileSystemProviderCreate = DefaultFileSystemProvider.create();
            String property = System.getProperty("java.nio.file.spi.DefaultFileSystemProvider");
            if (property != null) {
                for (String str : property.split(",")) {
                    try {
                        fileSystemProviderCreate = (FileSystemProvider) Class.forName(str, true, ClassLoader.getSystemClassLoader()).getDeclaredConstructor(FileSystemProvider.class).newInstance(fileSystemProviderCreate);
                        if (!fileSystemProviderCreate.getScheme().equals(DeploymentDescriptorParser.ATTR_FILE)) {
                            throw new Error("Default provider must use scheme 'file'");
                        }
                    } catch (Exception e2) {
                        throw new Error(e2);
                    }
                }
            }
            return fileSystemProviderCreate;
        }
    }

    public static FileSystem getDefault() {
        return DefaultFileSystemHolder.defaultFileSystem;
    }

    public static FileSystem getFileSystem(URI uri) {
        String scheme = uri.getScheme();
        for (FileSystemProvider fileSystemProvider : FileSystemProvider.installedProviders()) {
            if (scheme.equalsIgnoreCase(fileSystemProvider.getScheme())) {
                return fileSystemProvider.getFileSystem(uri);
            }
        }
        throw new ProviderNotFoundException("Provider \"" + scheme + "\" not found");
    }

    public static FileSystem newFileSystem(URI uri, Map<String, ?> map) throws IOException {
        return newFileSystem(uri, map, null);
    }

    public static FileSystem newFileSystem(URI uri, Map<String, ?> map, ClassLoader classLoader) throws IOException {
        String scheme = uri.getScheme();
        for (FileSystemProvider fileSystemProvider : FileSystemProvider.installedProviders()) {
            if (scheme.equalsIgnoreCase(fileSystemProvider.getScheme())) {
                return fileSystemProvider.newFileSystem(uri, map);
            }
        }
        if (classLoader != null) {
            Iterator it = ServiceLoader.load(FileSystemProvider.class, classLoader).iterator();
            while (it.hasNext()) {
                FileSystemProvider fileSystemProvider2 = (FileSystemProvider) it.next();
                if (scheme.equalsIgnoreCase(fileSystemProvider2.getScheme())) {
                    return fileSystemProvider2.newFileSystem(uri, map);
                }
            }
        }
        throw new ProviderNotFoundException("Provider \"" + scheme + "\" not found");
    }

    public static FileSystem newFileSystem(Path path, ClassLoader classLoader) throws IOException {
        if (path == null) {
            throw new NullPointerException();
        }
        Map<String, ?> mapEmptyMap = Collections.emptyMap();
        Iterator<FileSystemProvider> it = FileSystemProvider.installedProviders().iterator();
        while (it.hasNext()) {
            try {
                return it.next().newFileSystem(path, mapEmptyMap);
            } catch (UnsupportedOperationException e2) {
            }
        }
        if (classLoader != null) {
            Iterator it2 = ServiceLoader.load(FileSystemProvider.class, classLoader).iterator();
            while (it2.hasNext()) {
                try {
                    return ((FileSystemProvider) it2.next()).newFileSystem(path, mapEmptyMap);
                } catch (UnsupportedOperationException e3) {
                }
            }
        }
        throw new ProviderNotFoundException("Provider not found");
    }
}
