package sun.nio.fs;

import java.io.IOException;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.UserPrincipal;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import javafx.fxml.FXMLLoader;
import sun.nio.fs.WindowsNativeDispatcher;

/* loaded from: rt.jar:sun/nio/fs/WindowsUserPrincipals.class */
class WindowsUserPrincipals {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !WindowsUserPrincipals.class.desiredAssertionStatus();
    }

    private WindowsUserPrincipals() {
    }

    /* loaded from: rt.jar:sun/nio/fs/WindowsUserPrincipals$User.class */
    static class User implements UserPrincipal {
        private final String sidString;
        private final int sidType;
        private final String accountName;

        User(String str, int i2, String str2) {
            this.sidString = str;
            this.sidType = i2;
            this.accountName = str2;
        }

        String sidString() {
            return this.sidString;
        }

        @Override // java.security.Principal
        public String getName() {
            return this.accountName;
        }

        @Override // java.security.Principal
        public String toString() {
            String str;
            switch (this.sidType) {
                case 1:
                    str = "User";
                    break;
                case 2:
                    str = "Group";
                    break;
                case 3:
                    str = "Domain";
                    break;
                case 4:
                    str = "Alias";
                    break;
                case 5:
                    str = "Well-known group";
                    break;
                case 6:
                    str = "Deleted";
                    break;
                case 7:
                    str = "Invalid";
                    break;
                case 8:
                default:
                    str = "Unknown";
                    break;
                case 9:
                    str = "Computer";
                    break;
            }
            return this.accountName + " (" + str + ")";
        }

        @Override // java.security.Principal
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof User)) {
                return false;
            }
            return this.sidString.equals(((User) obj).sidString);
        }

        @Override // java.security.Principal
        public int hashCode() {
            return this.sidString.hashCode();
        }
    }

    /* loaded from: rt.jar:sun/nio/fs/WindowsUserPrincipals$Group.class */
    static class Group extends User implements GroupPrincipal {
        Group(String str, int i2, String str2) {
            super(str, i2, str2);
        }
    }

    static UserPrincipal fromSid(long j2) throws IOException {
        String str;
        try {
            String strConvertSidToStringSid = WindowsNativeDispatcher.ConvertSidToStringSid(j2);
            if (strConvertSidToStringSid == null) {
                throw new AssertionError();
            }
            WindowsNativeDispatcher.Account accountLookupAccountSid = null;
            try {
                accountLookupAccountSid = WindowsNativeDispatcher.LookupAccountSid(j2);
                str = accountLookupAccountSid.domain() + FXMLLoader.ESCAPE_PREFIX + accountLookupAccountSid.name();
            } catch (WindowsException e2) {
                str = strConvertSidToStringSid;
            }
            int iUse = accountLookupAccountSid == null ? 8 : accountLookupAccountSid.use();
            if (iUse == 2 || iUse == 5 || iUse == 4) {
                return new Group(strConvertSidToStringSid, iUse, str);
            }
            return new User(strConvertSidToStringSid, iUse, str);
        } catch (WindowsException e3) {
            throw new IOException("Unable to convert SID to String: " + e3.errorString());
        }
    }

    static UserPrincipal lookup(String str) throws IOException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("lookupUserInformation"));
        }
        try {
            int iLookupAccountName = WindowsNativeDispatcher.LookupAccountName(str, 0L, 0);
            if (!$assertionsDisabled && iLookupAccountName <= 0) {
                throw new AssertionError();
            }
            NativeBuffer nativeBuffer = NativeBuffers.getNativeBuffer(iLookupAccountName);
            try {
                try {
                    if (WindowsNativeDispatcher.LookupAccountName(str, nativeBuffer.address(), iLookupAccountName) != iLookupAccountName) {
                        throw new AssertionError((Object) "SID change during lookup");
                    }
                    UserPrincipal userPrincipalFromSid = fromSid(nativeBuffer.address());
                    nativeBuffer.release();
                    return userPrincipalFromSid;
                } catch (WindowsException e2) {
                    throw new IOException(str + ": " + e2.errorString());
                }
            } catch (Throwable th) {
                nativeBuffer.release();
                throw th;
            }
        } catch (WindowsException e3) {
            if (e3.lastError() == 1332) {
                throw new UserPrincipalNotFoundException(str);
            }
            throw new IOException(str + ": " + e3.errorString());
        }
    }
}
