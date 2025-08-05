package java.security;

import java.nio.ByteBuffer;
import java.util.HashMap;
import sun.security.util.Debug;

/* loaded from: rt.jar:java/security/SecureClassLoader.class */
public class SecureClassLoader extends ClassLoader {
    private final boolean initialized;
    private final HashMap<CodeSource, ProtectionDomain> pdcache;
    private static final Debug debug = Debug.getInstance("scl");

    static {
        ClassLoader.registerAsParallelCapable();
    }

    protected SecureClassLoader(ClassLoader classLoader) {
        super(classLoader);
        this.pdcache = new HashMap<>(11);
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkCreateClassLoader();
        }
        this.initialized = true;
    }

    protected SecureClassLoader() {
        this.pdcache = new HashMap<>(11);
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkCreateClassLoader();
        }
        this.initialized = true;
    }

    protected final Class<?> defineClass(String str, byte[] bArr, int i2, int i3, CodeSource codeSource) {
        return defineClass(str, bArr, i2, i3, getProtectionDomain(codeSource));
    }

    protected final Class<?> defineClass(String str, ByteBuffer byteBuffer, CodeSource codeSource) {
        return defineClass(str, byteBuffer, getProtectionDomain(codeSource));
    }

    protected PermissionCollection getPermissions(CodeSource codeSource) {
        check();
        return new Permissions();
    }

    private ProtectionDomain getProtectionDomain(CodeSource codeSource) {
        ProtectionDomain protectionDomain;
        if (codeSource == null) {
            return null;
        }
        synchronized (this.pdcache) {
            protectionDomain = this.pdcache.get(codeSource);
            if (protectionDomain == null) {
                protectionDomain = new ProtectionDomain(codeSource, getPermissions(codeSource), this, null);
                this.pdcache.put(codeSource, protectionDomain);
                if (debug != null) {
                    debug.println(" getPermissions " + ((Object) protectionDomain));
                    debug.println("");
                }
            }
        }
        return protectionDomain;
    }

    private void check() {
        if (!this.initialized) {
            throw new SecurityException("ClassLoader object not initialized");
        }
    }
}
