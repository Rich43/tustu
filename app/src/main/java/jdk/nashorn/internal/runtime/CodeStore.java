package jdk.nashorn.internal.runtime;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import jdk.nashorn.internal.codegen.OptimisticTypesPersistence;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.runtime.logging.Logger;
import jdk.nashorn.internal.runtime.options.Options;
import sun.util.locale.LanguageTag;

@Logger(name = "codestore")
/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/CodeStore.class */
public abstract class CodeStore implements Loggable {
    public static final String NASHORN_PROVIDE_CODE_STORE = "nashorn.provideCodeStore";
    private DebugLogger log;

    public abstract StoredScript store(String str, Source source, StoredScript storedScript);

    public abstract StoredScript load(Source source, String str);

    protected CodeStore() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // jdk.nashorn.internal.runtime.logging.Loggable
    public DebugLogger initLogger(Context context) {
        this.log = context.getLogger(getClass());
        return this.log;
    }

    @Override // jdk.nashorn.internal.runtime.logging.Loggable
    public DebugLogger getLogger() {
        return this.log;
    }

    public static CodeStore newCodeStore(Context context) {
        try {
            SecurityManager sm = System.getSecurityManager();
            if (sm != null) {
                sm.checkPermission(new RuntimePermission(NASHORN_PROVIDE_CODE_STORE));
            }
            ServiceLoader<CodeStore> services = ServiceLoader.load(CodeStore.class);
            Iterator<CodeStore> iterator = services.iterator();
            if (iterator.hasNext()) {
                CodeStore store = iterator.next();
                store.initLogger(context).info("using code store provider ", store.getClass().getCanonicalName());
                return store;
            }
        } catch (AccessControlException e2) {
            context.getLogger(CodeStore.class).warning("failed to load code store provider ", e2);
        }
        try {
            CodeStore store2 = new DirectoryCodeStore(context);
            store2.initLogger(context);
            return store2;
        } catch (IOException e3) {
            context.getLogger(CodeStore.class).warning("failed to create cache directory ", e3);
            return null;
        }
    }

    public StoredScript store(String functionKey, Source source, String mainClassName, Map<String, byte[]> classBytes, Map<Integer, FunctionInitializer> initializers, Object[] constants, int compilationId) {
        return store(functionKey, source, storedScriptFor(source, mainClassName, classBytes, initializers, constants, compilationId));
    }

    public StoredScript storedScriptFor(Source source, String mainClassName, Map<String, byte[]> classBytes, Map<Integer, FunctionInitializer> initializers, Object[] constants, int compilationId) {
        for (Object constant : constants) {
            if (!(constant instanceof Serializable)) {
                getLogger().warning("cannot store ", source, " non serializable constant ", constant);
                return null;
            }
        }
        return new StoredScript(compilationId, mainClassName, classBytes, initializers, constants);
    }

    public static String getCacheKey(Object functionId, Type[] paramTypes) {
        StringBuilder b2 = new StringBuilder().append(functionId);
        if (paramTypes != null && paramTypes.length > 0) {
            b2.append('-');
            for (Type t2 : paramTypes) {
                b2.append(Type.getShortSignatureDescriptor(t2));
            }
        }
        return b2.toString();
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/CodeStore$DirectoryCodeStore.class */
    public static class DirectoryCodeStore extends CodeStore {
        private static final int DEFAULT_MIN_SIZE = 1000;
        private final File dir;
        private final boolean readOnly;
        private final int minSize;

        public DirectoryCodeStore(Context context) throws IOException {
            this(context, Options.getStringProperty("nashorn.persistent.code.cache", "nashorn_code_cache"), false, 1000);
        }

        public DirectoryCodeStore(Context context, String path, boolean readOnly, int minSize) throws IOException {
            this.dir = checkDirectory(path, context.getEnv(), readOnly);
            this.readOnly = readOnly;
            this.minSize = minSize;
        }

        private static File checkDirectory(final String path, final ScriptEnvironment env, final boolean readOnly) throws IOException {
            try {
                return (File) AccessController.doPrivileged(new PrivilegedExceptionAction<File>() { // from class: jdk.nashorn.internal.runtime.CodeStore.DirectoryCodeStore.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public File run() throws IOException {
                        File dir = new File(path, DirectoryCodeStore.getVersionDir(env)).getAbsoluteFile();
                        if (readOnly) {
                            if (!dir.exists() || !dir.isDirectory()) {
                                throw new IOException("Not a directory: " + dir.getPath());
                            }
                            if (!dir.canRead()) {
                                throw new IOException("Directory not readable: " + dir.getPath());
                            }
                        } else {
                            if (!dir.exists() && !dir.mkdirs()) {
                                throw new IOException("Could not create directory: " + dir.getPath());
                            }
                            if (!dir.isDirectory()) {
                                throw new IOException("Not a directory: " + dir.getPath());
                            }
                            if (!dir.canRead() || !dir.canWrite()) {
                                throw new IOException("Directory not readable or writable: " + dir.getPath());
                            }
                        }
                        return dir;
                    }
                });
            } catch (PrivilegedActionException e2) {
                throw ((IOException) e2.getException());
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static String getVersionDir(ScriptEnvironment env) throws IOException {
            try {
                String versionDir = OptimisticTypesPersistence.getVersionDirName();
                return env._optimistic_types ? versionDir + "_opt" : versionDir;
            } catch (Exception e2) {
                throw new IOException(e2);
            }
        }

        @Override // jdk.nashorn.internal.runtime.CodeStore
        public StoredScript load(final Source source, final String functionKey) {
            if (belowThreshold(source)) {
                return null;
            }
            final File file = getCacheFile(source, functionKey);
            try {
                return (StoredScript) AccessController.doPrivileged(new PrivilegedExceptionAction<StoredScript>() { // from class: jdk.nashorn.internal.runtime.CodeStore.DirectoryCodeStore.2
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public StoredScript run() throws IOException, ClassNotFoundException {
                        if (!file.exists()) {
                            return null;
                        }
                        ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
                        Throwable th = null;
                        try {
                            try {
                                StoredScript storedScript = (StoredScript) in.readObject();
                                DirectoryCodeStore.this.getLogger().info("loaded ", source, LanguageTag.SEP, functionKey);
                                if (in != null) {
                                    if (0 != 0) {
                                        try {
                                            in.close();
                                        } catch (Throwable th2) {
                                            th.addSuppressed(th2);
                                        }
                                    } else {
                                        in.close();
                                    }
                                }
                                return storedScript;
                            } catch (Throwable th3) {
                                if (in != null) {
                                    if (th != null) {
                                        try {
                                            in.close();
                                        } catch (Throwable th4) {
                                            th.addSuppressed(th4);
                                        }
                                    } else {
                                        in.close();
                                    }
                                }
                                throw th3;
                            }
                        } finally {
                        }
                    }
                });
            } catch (PrivilegedActionException e2) {
                getLogger().warning("failed to load ", source, LanguageTag.SEP, functionKey, ": ", e2.getException());
                return null;
            }
        }

        @Override // jdk.nashorn.internal.runtime.CodeStore
        public StoredScript store(final String functionKey, final Source source, final StoredScript script) {
            if (this.readOnly || script == null || belowThreshold(source)) {
                return null;
            }
            final File file = getCacheFile(source, functionKey);
            try {
                return (StoredScript) AccessController.doPrivileged(new PrivilegedExceptionAction<StoredScript>() { // from class: jdk.nashorn.internal.runtime.CodeStore.DirectoryCodeStore.3
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public StoredScript run() throws IOException {
                        ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
                        Throwable th = null;
                        try {
                            out.writeObject(script);
                            if (out != null) {
                                if (0 != 0) {
                                    try {
                                        out.close();
                                    } catch (Throwable th2) {
                                        th.addSuppressed(th2);
                                    }
                                } else {
                                    out.close();
                                }
                            }
                            DirectoryCodeStore.this.getLogger().info("stored ", source, LanguageTag.SEP, functionKey);
                            return script;
                        } catch (Throwable th3) {
                            if (out != null) {
                                if (0 != 0) {
                                    try {
                                        out.close();
                                    } catch (Throwable th4) {
                                        th.addSuppressed(th4);
                                    }
                                } else {
                                    out.close();
                                }
                            }
                            throw th3;
                        }
                    }
                });
            } catch (PrivilegedActionException e2) {
                getLogger().warning("failed to store ", script, LanguageTag.SEP, functionKey, ": ", e2.getException());
                return null;
            }
        }

        private File getCacheFile(Source source, String functionKey) {
            return new File(this.dir, source.getDigest() + '-' + functionKey);
        }

        private boolean belowThreshold(Source source) {
            if (source.getLength() < this.minSize) {
                getLogger().info("below size threshold ", source);
                return true;
            }
            return false;
        }
    }
}
