package java.lang;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FilePermission;
import java.net.InetAddress;
import java.net.SocketPermission;
import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.Permission;
import java.security.PrivilegedAction;
import java.security.Security;
import java.security.SecurityPermission;
import java.util.PropertyPermission;
import java.util.StringTokenizer;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.reflect.CallerSensitive;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:java/lang/SecurityManager.class */
public class SecurityManager {

    @Deprecated
    protected boolean inCheck;
    private boolean initialized;
    private static String[] packageAccess;
    private static String[] packageDefinition;
    private static ThreadGroup rootGroup = getRootGroup();
    private static boolean packageAccessValid = false;
    private static final Object packageAccessLock = new Object();
    private static boolean packageDefinitionValid = false;
    private static final Object packageDefinitionLock = new Object();

    protected native Class[] getClassContext();

    private native ClassLoader currentClassLoader0();

    @Deprecated
    protected native int classDepth(String str);

    private native int classLoaderDepth0();

    private native Class<?> currentLoadedClass0();

    private boolean hasAllPermission() {
        try {
            checkPermission(SecurityConstants.ALL_PERMISSION);
            return true;
        } catch (SecurityException e2) {
            return false;
        }
    }

    @Deprecated
    public boolean getInCheck() {
        return this.inCheck;
    }

    public SecurityManager() {
        this.initialized = false;
        synchronized (SecurityManager.class) {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkPermission(new RuntimePermission("createSecurityManager"));
            }
            this.initialized = true;
        }
    }

    @Deprecated
    protected ClassLoader currentClassLoader() {
        ClassLoader classLoaderCurrentClassLoader0 = currentClassLoader0();
        if (classLoaderCurrentClassLoader0 != null && hasAllPermission()) {
            classLoaderCurrentClassLoader0 = null;
        }
        return classLoaderCurrentClassLoader0;
    }

    @Deprecated
    protected Class<?> currentLoadedClass() {
        Class<?> clsCurrentLoadedClass0 = currentLoadedClass0();
        if (clsCurrentLoadedClass0 != null && hasAllPermission()) {
            clsCurrentLoadedClass0 = null;
        }
        return clsCurrentLoadedClass0;
    }

    @Deprecated
    protected int classLoaderDepth() {
        int iClassLoaderDepth0 = classLoaderDepth0();
        if (iClassLoaderDepth0 != -1) {
            if (hasAllPermission()) {
                iClassLoaderDepth0 = -1;
            } else {
                iClassLoaderDepth0--;
            }
        }
        return iClassLoaderDepth0;
    }

    @Deprecated
    protected boolean inClass(String str) {
        return classDepth(str) >= 0;
    }

    @Deprecated
    protected boolean inClassLoader() {
        return currentClassLoader() != null;
    }

    public Object getSecurityContext() {
        return AccessController.getContext();
    }

    public void checkPermission(Permission permission) {
        AccessController.checkPermission(permission);
    }

    public void checkPermission(Permission permission, Object obj) throws AccessControlException {
        if (obj instanceof AccessControlContext) {
            ((AccessControlContext) obj).checkPermission(permission);
            return;
        }
        throw new SecurityException();
    }

    public void checkCreateClassLoader() {
        checkPermission(SecurityConstants.CREATE_CLASSLOADER_PERMISSION);
    }

    private static ThreadGroup getRootGroup() {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        while (true) {
            ThreadGroup threadGroup2 = threadGroup;
            if (threadGroup2.getParent() != null) {
                threadGroup = threadGroup2.getParent();
            } else {
                return threadGroup2;
            }
        }
    }

    public void checkAccess(Thread thread) {
        if (thread == null) {
            throw new NullPointerException("thread can't be null");
        }
        if (thread.getThreadGroup() == rootGroup) {
            checkPermission(SecurityConstants.MODIFY_THREAD_PERMISSION);
        }
    }

    public void checkAccess(ThreadGroup threadGroup) {
        if (threadGroup == null) {
            throw new NullPointerException("thread group can't be null");
        }
        if (threadGroup == rootGroup) {
            checkPermission(SecurityConstants.MODIFY_THREADGROUP_PERMISSION);
        }
    }

    public void checkExit(int i2) {
        checkPermission(new RuntimePermission("exitVM." + i2));
    }

    public void checkExec(String str) {
        if (new File(str).isAbsolute()) {
            checkPermission(new FilePermission(str, SecurityConstants.FILE_EXECUTE_ACTION));
        } else {
            checkPermission(new FilePermission("<<ALL FILES>>", SecurityConstants.FILE_EXECUTE_ACTION));
        }
    }

    public void checkLink(String str) {
        if (str == null) {
            throw new NullPointerException("library can't be null");
        }
        checkPermission(new RuntimePermission("loadLibrary." + str));
    }

    public void checkRead(FileDescriptor fileDescriptor) {
        if (fileDescriptor == null) {
            throw new NullPointerException("file descriptor can't be null");
        }
        checkPermission(new RuntimePermission("readFileDescriptor"));
    }

    public void checkRead(String str) {
        checkPermission(new FilePermission(str, "read"));
    }

    public void checkRead(String str, Object obj) throws AccessControlException {
        checkPermission(new FilePermission(str, "read"), obj);
    }

    public void checkWrite(FileDescriptor fileDescriptor) {
        if (fileDescriptor == null) {
            throw new NullPointerException("file descriptor can't be null");
        }
        checkPermission(new RuntimePermission("writeFileDescriptor"));
    }

    public void checkWrite(String str) {
        checkPermission(new FilePermission(str, "write"));
    }

    public void checkDelete(String str) {
        checkPermission(new FilePermission(str, SecurityConstants.FILE_DELETE_ACTION));
    }

    public void checkConnect(String str, int i2) {
        if (str == null) {
            throw new NullPointerException("host can't be null");
        }
        if (!str.startsWith("[") && str.indexOf(58) != -1) {
            str = "[" + str + "]";
        }
        if (i2 == -1) {
            checkPermission(new SocketPermission(str, SecurityConstants.SOCKET_RESOLVE_ACTION));
        } else {
            checkPermission(new SocketPermission(str + CallSiteDescriptor.TOKEN_DELIMITER + i2, SecurityConstants.SOCKET_CONNECT_ACTION));
        }
    }

    public void checkConnect(String str, int i2, Object obj) throws AccessControlException {
        if (str == null) {
            throw new NullPointerException("host can't be null");
        }
        if (!str.startsWith("[") && str.indexOf(58) != -1) {
            str = "[" + str + "]";
        }
        if (i2 == -1) {
            checkPermission(new SocketPermission(str, SecurityConstants.SOCKET_RESOLVE_ACTION), obj);
        } else {
            checkPermission(new SocketPermission(str + CallSiteDescriptor.TOKEN_DELIMITER + i2, SecurityConstants.SOCKET_CONNECT_ACTION), obj);
        }
    }

    public void checkListen(int i2) {
        checkPermission(new SocketPermission("localhost:" + i2, SecurityConstants.SOCKET_LISTEN_ACTION));
    }

    public void checkAccept(String str, int i2) {
        if (str == null) {
            throw new NullPointerException("host can't be null");
        }
        if (!str.startsWith("[") && str.indexOf(58) != -1) {
            str = "[" + str + "]";
        }
        checkPermission(new SocketPermission(str + CallSiteDescriptor.TOKEN_DELIMITER + i2, SecurityConstants.SOCKET_ACCEPT_ACTION));
    }

    public void checkMulticast(InetAddress inetAddress) {
        String hostAddress = inetAddress.getHostAddress();
        if (!hostAddress.startsWith("[") && hostAddress.indexOf(58) != -1) {
            hostAddress = "[" + hostAddress + "]";
        }
        checkPermission(new SocketPermission(hostAddress, SecurityConstants.SOCKET_CONNECT_ACCEPT_ACTION));
    }

    @Deprecated
    public void checkMulticast(InetAddress inetAddress, byte b2) {
        String hostAddress = inetAddress.getHostAddress();
        if (!hostAddress.startsWith("[") && hostAddress.indexOf(58) != -1) {
            hostAddress = "[" + hostAddress + "]";
        }
        checkPermission(new SocketPermission(hostAddress, SecurityConstants.SOCKET_CONNECT_ACCEPT_ACTION));
    }

    public void checkPropertiesAccess() {
        checkPermission(new PropertyPermission("*", SecurityConstants.PROPERTY_RW_ACTION));
    }

    public void checkPropertyAccess(String str) {
        checkPermission(new PropertyPermission(str, "read"));
    }

    @Deprecated
    public boolean checkTopLevelWindow(Object obj) {
        if (obj == null) {
            throw new NullPointerException("window can't be null");
        }
        Permission permission = SecurityConstants.AWT.TOPLEVEL_WINDOW_PERMISSION;
        if (permission == null) {
            permission = SecurityConstants.ALL_PERMISSION;
        }
        try {
            checkPermission(permission);
            return true;
        } catch (SecurityException e2) {
            return false;
        }
    }

    public void checkPrintJobAccess() {
        checkPermission(new RuntimePermission("queuePrintJob"));
    }

    @Deprecated
    public void checkSystemClipboardAccess() {
        Permission permission = SecurityConstants.AWT.ACCESS_CLIPBOARD_PERMISSION;
        if (permission == null) {
            permission = SecurityConstants.ALL_PERMISSION;
        }
        checkPermission(permission);
    }

    @Deprecated
    public void checkAwtEventQueueAccess() {
        Permission permission = SecurityConstants.AWT.CHECK_AWT_EVENTQUEUE_PERMISSION;
        if (permission == null) {
            permission = SecurityConstants.ALL_PERMISSION;
        }
        checkPermission(permission);
    }

    private static String[] getPackages(String str) {
        StringTokenizer stringTokenizer;
        int iCountTokens;
        String[] strArr = null;
        if (str != null && !str.equals("") && (iCountTokens = (stringTokenizer = new StringTokenizer(str, ",")).countTokens()) > 0) {
            strArr = new String[iCountTokens];
            int i2 = 0;
            while (stringTokenizer.hasMoreElements()) {
                int i3 = i2;
                i2++;
                strArr[i3] = stringTokenizer.nextToken().trim();
            }
        }
        if (strArr == null) {
            strArr = new String[0];
        }
        return strArr;
    }

    public void checkPackageAccess(String str) {
        String[] strArr;
        if (str == null) {
            throw new NullPointerException("package name can't be null");
        }
        synchronized (packageAccessLock) {
            if (!packageAccessValid) {
                packageAccess = getPackages((String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: java.lang.SecurityManager.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    public String run() {
                        return Security.getProperty("package.access");
                    }
                }));
                packageAccessValid = true;
            }
            strArr = packageAccess;
        }
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (str.startsWith(strArr[i2]) || strArr[i2].equals(str + ".")) {
                checkPermission(new RuntimePermission("accessClassInPackage." + str));
                return;
            }
        }
    }

    public void checkPackageDefinition(String str) {
        String[] strArr;
        if (str == null) {
            throw new NullPointerException("package name can't be null");
        }
        synchronized (packageDefinitionLock) {
            if (!packageDefinitionValid) {
                packageDefinition = getPackages((String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: java.lang.SecurityManager.2
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    public String run() {
                        return Security.getProperty("package.definition");
                    }
                }));
                packageDefinitionValid = true;
            }
            strArr = packageDefinition;
        }
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (str.startsWith(strArr[i2]) || strArr[i2].equals(str + ".")) {
                checkPermission(new RuntimePermission("defineClassInPackage." + str));
                return;
            }
        }
    }

    public void checkSetFactory() {
        checkPermission(new RuntimePermission("setFactory"));
    }

    @CallerSensitive
    @Deprecated
    public void checkMemberAccess(Class<?> cls, int i2) {
        if (cls == null) {
            throw new NullPointerException("class can't be null");
        }
        if (i2 != 0) {
            Class[] classContext = getClassContext();
            if (classContext.length < 4 || classContext[3].getClassLoader() != cls.getClassLoader()) {
                checkPermission(SecurityConstants.CHECK_MEMBER_ACCESS_PERMISSION);
            }
        }
    }

    public void checkSecurityAccess(String str) {
        checkPermission(new SecurityPermission(str));
    }

    public ThreadGroup getThreadGroup() {
        return Thread.currentThread().getThreadGroup();
    }
}
