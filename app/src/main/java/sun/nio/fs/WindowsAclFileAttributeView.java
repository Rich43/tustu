package sun.nio.fs;

import java.io.IOException;
import java.nio.file.ProviderMismatchException;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import sun.nio.fs.WindowsSecurity;
import sun.nio.fs.WindowsUserPrincipals;

/* loaded from: rt.jar:sun/nio/fs/WindowsAclFileAttributeView.class */
class WindowsAclFileAttributeView extends AbstractAclFileAttributeView {
    private static final short SIZEOF_SECURITY_DESCRIPTOR = 20;
    private final WindowsPath file;
    private final boolean followLinks;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !WindowsAclFileAttributeView.class.desiredAssertionStatus();
    }

    WindowsAclFileAttributeView(WindowsPath windowsPath, boolean z2) {
        this.file = windowsPath;
        this.followLinks = z2;
    }

    private void checkAccess(WindowsPath windowsPath, boolean z2, boolean z3) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            if (z2) {
                securityManager.checkRead(windowsPath.getPathForPermissionCheck());
            }
            if (z3) {
                securityManager.checkWrite(windowsPath.getPathForPermissionCheck());
            }
            securityManager.checkPermission(new RuntimePermission("accessUserInformation"));
        }
    }

    static NativeBuffer getFileSecurity(String str, int i2) throws IOException {
        int iGetFileSecurity = 0;
        try {
            iGetFileSecurity = WindowsNativeDispatcher.GetFileSecurity(str, i2, 0L, 0);
        } catch (WindowsException e2) {
            e2.rethrowAsIOException(str);
        }
        if (!$assertionsDisabled && iGetFileSecurity <= 0) {
            throw new AssertionError();
        }
        NativeBuffer nativeBuffer = NativeBuffers.getNativeBuffer(iGetFileSecurity);
        while (true) {
            try {
                int iGetFileSecurity2 = WindowsNativeDispatcher.GetFileSecurity(str, i2, nativeBuffer.address(), iGetFileSecurity);
                if (iGetFileSecurity2 <= iGetFileSecurity) {
                    return nativeBuffer;
                }
                nativeBuffer.release();
                nativeBuffer = NativeBuffers.getNativeBuffer(iGetFileSecurity2);
                iGetFileSecurity = iGetFileSecurity2;
            } catch (WindowsException e3) {
                nativeBuffer.release();
                e3.rethrowAsIOException(str);
                return null;
            }
        }
    }

    @Override // java.nio.file.attribute.FileOwnerAttributeView
    public UserPrincipal getOwner() throws IOException {
        checkAccess(this.file, true, false);
        NativeBuffer fileSecurity = getFileSecurity(WindowsLinkSupport.getFinalPath(this.file, this.followLinks), 1);
        try {
            try {
                long jGetSecurityDescriptorOwner = WindowsNativeDispatcher.GetSecurityDescriptorOwner(fileSecurity.address());
                if (jGetSecurityDescriptorOwner == 0) {
                    throw new IOException("no owner");
                }
                UserPrincipal userPrincipalFromSid = WindowsUserPrincipals.fromSid(jGetSecurityDescriptorOwner);
                fileSecurity.release();
                return userPrincipalFromSid;
            } catch (WindowsException e2) {
                e2.rethrowAsIOException(this.file);
                fileSecurity.release();
                return null;
            }
        } catch (Throwable th) {
            fileSecurity.release();
            throw th;
        }
    }

    @Override // java.nio.file.attribute.AclFileAttributeView
    public List<AclEntry> getAcl() throws IOException {
        checkAccess(this.file, true, false);
        NativeBuffer fileSecurity = getFileSecurity(WindowsLinkSupport.getFinalPath(this.file, this.followLinks), 4);
        try {
            List<AclEntry> acl = WindowsSecurityDescriptor.getAcl(fileSecurity.address());
            fileSecurity.release();
            return acl;
        } catch (Throwable th) {
            fileSecurity.release();
            throw th;
        }
    }

    /* JADX WARN: Failed to calculate best type for var: r11v1 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException
     */
    /* JADX WARN: Not initialized variable reg: 11, insn: 0x00d1: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r11 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) A[TRY_LEAVE], block:B:29:0x00d1 */
    @Override // java.nio.file.attribute.FileOwnerAttributeView
    public void setOwner(UserPrincipal userPrincipal) throws IOException {
        NativeBuffer nativeBuffer;
        if (userPrincipal == null) {
            throw new NullPointerException("'owner' is null");
        }
        if (!(userPrincipal instanceof WindowsUserPrincipals.User)) {
            throw new ProviderMismatchException();
        }
        WindowsUserPrincipals.User user = (WindowsUserPrincipals.User) userPrincipal;
        checkAccess(this.file, false, true);
        String finalPath = WindowsLinkSupport.getFinalPath(this.file, this.followLinks);
        try {
            long jConvertStringSidToSid = WindowsNativeDispatcher.ConvertStringSidToSid(user.sidString());
            try {
                try {
                    NativeBuffer nativeBuffer2 = NativeBuffers.getNativeBuffer(20);
                    try {
                        WindowsNativeDispatcher.InitializeSecurityDescriptor(nativeBuffer2.address());
                        WindowsNativeDispatcher.SetSecurityDescriptorOwner(nativeBuffer2.address(), jConvertStringSidToSid);
                        WindowsSecurity.Privilege privilegeEnablePrivilege = WindowsSecurity.enablePrivilege("SeRestorePrivilege");
                        try {
                            WindowsNativeDispatcher.SetFileSecurity(finalPath, 1, nativeBuffer2.address());
                            privilegeEnablePrivilege.drop();
                            nativeBuffer2.release();
                        } catch (Throwable th) {
                            privilegeEnablePrivilege.drop();
                            throw th;
                        }
                    } catch (WindowsException e2) {
                        e2.rethrowAsIOException(this.file);
                        nativeBuffer2.release();
                    }
                } catch (Throwable th2) {
                    nativeBuffer.release();
                    throw th2;
                }
            } finally {
                WindowsNativeDispatcher.LocalFree(jConvertStringSidToSid);
            }
        } catch (WindowsException e3) {
            throw new IOException("Failed to get SID for " + user.getName() + ": " + e3.errorString());
        }
    }

    @Override // java.nio.file.attribute.AclFileAttributeView
    public void setAcl(List<AclEntry> list) throws IOException {
        checkAccess(this.file, false, true);
        String finalPath = WindowsLinkSupport.getFinalPath(this.file, this.followLinks);
        WindowsSecurityDescriptor windowsSecurityDescriptorCreate = WindowsSecurityDescriptor.create(list);
        try {
            try {
                WindowsNativeDispatcher.SetFileSecurity(finalPath, 4, windowsSecurityDescriptorCreate.address());
                windowsSecurityDescriptorCreate.release();
            } catch (WindowsException e2) {
                e2.rethrowAsIOException(this.file);
                windowsSecurityDescriptorCreate.release();
            }
        } catch (Throwable th) {
            windowsSecurityDescriptorCreate.release();
            throw th;
        }
    }
}
