package sun.misc;

import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.net.URL;
import java.security.AccessController;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.PropertyPermission;
import sun.security.util.SecurityConstants;
import sun.util.locale.LanguageTag;

/* compiled from: Launcher.java */
/* loaded from: rt.jar:sun/misc/PathPermissions.class */
class PathPermissions extends PermissionCollection {
    private static final long serialVersionUID = 8133287259134945693L;
    private File[] path;
    private Permissions perms = null;
    URL codeBase = null;

    PathPermissions(File[] fileArr) {
        this.path = fileArr;
    }

    URL getCodeBase() {
        return this.codeBase;
    }

    @Override // java.security.PermissionCollection
    public void add(Permission permission) {
        throw new SecurityException("attempt to add a permission");
    }

    private synchronized void init() {
        if (this.perms != null) {
            return;
        }
        this.perms = new Permissions();
        this.perms.add(SecurityConstants.CREATE_CLASSLOADER_PERMISSION);
        this.perms.add(new PropertyPermission("java.*", "read"));
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.misc.PathPermissions.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                String absolutePath;
                for (int i2 = 0; i2 < PathPermissions.this.path.length; i2++) {
                    File file = PathPermissions.this.path[i2];
                    try {
                        absolutePath = file.getCanonicalPath();
                    } catch (IOException e2) {
                        absolutePath = file.getAbsolutePath();
                    }
                    if (i2 == 0) {
                        PathPermissions.this.codeBase = Launcher.getFileURL(new File(absolutePath));
                    }
                    if (file.isDirectory()) {
                        if (absolutePath.endsWith(File.separator)) {
                            PathPermissions.this.perms.add(new FilePermission(absolutePath + LanguageTag.SEP, "read"));
                        } else {
                            PathPermissions.this.perms.add(new FilePermission(absolutePath + File.separator + LanguageTag.SEP, "read"));
                        }
                    } else {
                        int iLastIndexOf = absolutePath.lastIndexOf(File.separatorChar);
                        if (iLastIndexOf != -1) {
                            PathPermissions.this.perms.add(new FilePermission(absolutePath.substring(0, iLastIndexOf + 1) + LanguageTag.SEP, "read"));
                        }
                    }
                }
                return null;
            }
        });
    }

    @Override // java.security.PermissionCollection
    public boolean implies(Permission permission) {
        if (this.perms == null) {
            init();
        }
        return this.perms.implies(permission);
    }

    @Override // java.security.PermissionCollection
    public Enumeration<Permission> elements() {
        Enumeration<Permission> enumerationElements;
        if (this.perms == null) {
            init();
        }
        synchronized (this.perms) {
            enumerationElements = this.perms.elements();
        }
        return enumerationElements;
    }

    @Override // java.security.PermissionCollection
    public String toString() {
        if (this.perms == null) {
            init();
        }
        return this.perms.toString();
    }
}
