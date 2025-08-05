package sun.nio.fs;

import java.io.IOException;
import java.nio.file.ProviderMismatchException;
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.AclEntryFlag;
import java.nio.file.attribute.AclEntryPermission;
import java.nio.file.attribute.AclEntryType;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.icepdf.core.util.PdfOps;
import sun.misc.Unsafe;
import sun.nio.fs.WindowsUserPrincipals;

/* loaded from: rt.jar:sun/nio/fs/WindowsSecurityDescriptor.class */
class WindowsSecurityDescriptor {
    private static final short SIZEOF_ACL = 8;
    private static final short SIZEOF_ACCESS_ALLOWED_ACE = 12;
    private static final short SIZEOF_ACCESS_DENIED_ACE = 12;
    private static final short SIZEOF_SECURITY_DESCRIPTOR = 20;
    private static final short OFFSETOF_TYPE = 0;
    private static final short OFFSETOF_FLAGS = 1;
    private static final short OFFSETOF_ACCESS_MASK = 4;
    private static final short OFFSETOF_SID = 8;
    private final List<Long> sidList;
    private final NativeBuffer aclBuffer;
    private final NativeBuffer sdBuffer;
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final WindowsSecurityDescriptor NULL_DESCRIPTOR = new WindowsSecurityDescriptor();

    private WindowsSecurityDescriptor() {
        this.sidList = null;
        this.aclBuffer = null;
        this.sdBuffer = null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private WindowsSecurityDescriptor(List<AclEntry> list) throws IOException {
        ArrayList arrayList = new ArrayList(list);
        this.sidList = new ArrayList(arrayList.size());
        try {
            try {
                int iGetLengthSid = 8;
                Iterator<E> it = arrayList.iterator();
                while (it.hasNext()) {
                    UserPrincipal userPrincipalPrincipal = ((AclEntry) it.next()).principal();
                    if (!(userPrincipalPrincipal instanceof WindowsUserPrincipals.User)) {
                        throw new ProviderMismatchException();
                    }
                    try {
                        long jConvertStringSidToSid = WindowsNativeDispatcher.ConvertStringSidToSid(((WindowsUserPrincipals.User) userPrincipalPrincipal).sidString());
                        this.sidList.add(Long.valueOf(jConvertStringSidToSid));
                        iGetLengthSid += WindowsNativeDispatcher.GetLengthSid(jConvertStringSidToSid) + Math.max(12, 12);
                    } catch (WindowsException e2) {
                        throw new IOException("Failed to get SID for " + userPrincipalPrincipal.getName() + ": " + e2.errorString());
                    }
                }
                this.aclBuffer = NativeBuffers.getNativeBuffer(iGetLengthSid);
                this.sdBuffer = NativeBuffers.getNativeBuffer(20);
                WindowsNativeDispatcher.InitializeAcl(this.aclBuffer.address(), iGetLengthSid);
                for (int i2 = 0; i2 < arrayList.size(); i2++) {
                    try {
                        encode((AclEntry) arrayList.get(i2), this.sidList.get(i2).longValue(), this.aclBuffer.address());
                    } catch (WindowsException e3) {
                        throw new IOException("Failed to encode ACE: " + e3.errorString());
                    }
                }
                WindowsNativeDispatcher.InitializeSecurityDescriptor(this.sdBuffer.address());
                WindowsNativeDispatcher.SetSecurityDescriptorDacl(this.sdBuffer.address(), this.aclBuffer.address());
                if (1 == 0) {
                    release();
                }
            } catch (WindowsException e4) {
                throw new IOException(e4.getMessage());
            }
        } catch (Throwable th) {
            if (0 == 0) {
                release();
            }
            throw th;
        }
    }

    void release() {
        if (this.sdBuffer != null) {
            this.sdBuffer.release();
        }
        if (this.aclBuffer != null) {
            this.aclBuffer.release();
        }
        if (this.sidList != null) {
            Iterator<Long> it = this.sidList.iterator();
            while (it.hasNext()) {
                WindowsNativeDispatcher.LocalFree(it.next().longValue());
            }
        }
    }

    long address() {
        if (this.sdBuffer == null) {
            return 0L;
        }
        return this.sdBuffer.address();
    }

    private static AclEntry decode(long j2) throws IOException {
        AclEntryType aclEntryType;
        byte b2 = unsafe.getByte(j2 + 0);
        if (b2 != 0 && b2 != 1) {
            return null;
        }
        if (b2 == 0) {
            aclEntryType = AclEntryType.ALLOW;
        } else {
            aclEntryType = AclEntryType.DENY;
        }
        byte b3 = unsafe.getByte(j2 + 1);
        EnumSet enumSetNoneOf = EnumSet.noneOf(AclEntryFlag.class);
        if ((b3 & 1) != 0) {
            enumSetNoneOf.add(AclEntryFlag.FILE_INHERIT);
        }
        if ((b3 & 2) != 0) {
            enumSetNoneOf.add(AclEntryFlag.DIRECTORY_INHERIT);
        }
        if ((b3 & 4) != 0) {
            enumSetNoneOf.add(AclEntryFlag.NO_PROPAGATE_INHERIT);
        }
        if ((b3 & 8) != 0) {
            enumSetNoneOf.add(AclEntryFlag.INHERIT_ONLY);
        }
        int i2 = unsafe.getInt(j2 + 4);
        EnumSet enumSetNoneOf2 = EnumSet.noneOf(AclEntryPermission.class);
        if ((i2 & 1) > 0) {
            enumSetNoneOf2.add(AclEntryPermission.READ_DATA);
        }
        if ((i2 & 2) > 0) {
            enumSetNoneOf2.add(AclEntryPermission.WRITE_DATA);
        }
        if ((i2 & 4) > 0) {
            enumSetNoneOf2.add(AclEntryPermission.APPEND_DATA);
        }
        if ((i2 & 8) > 0) {
            enumSetNoneOf2.add(AclEntryPermission.READ_NAMED_ATTRS);
        }
        if ((i2 & 16) > 0) {
            enumSetNoneOf2.add(AclEntryPermission.WRITE_NAMED_ATTRS);
        }
        if ((i2 & 32) > 0) {
            enumSetNoneOf2.add(AclEntryPermission.EXECUTE);
        }
        if ((i2 & 64) > 0) {
            enumSetNoneOf2.add(AclEntryPermission.DELETE_CHILD);
        }
        if ((i2 & 128) > 0) {
            enumSetNoneOf2.add(AclEntryPermission.READ_ATTRIBUTES);
        }
        if ((i2 & 256) > 0) {
            enumSetNoneOf2.add(AclEntryPermission.WRITE_ATTRIBUTES);
        }
        if ((i2 & 65536) > 0) {
            enumSetNoneOf2.add(AclEntryPermission.DELETE);
        }
        if ((i2 & 131072) > 0) {
            enumSetNoneOf2.add(AclEntryPermission.READ_ACL);
        }
        if ((i2 & 262144) > 0) {
            enumSetNoneOf2.add(AclEntryPermission.WRITE_ACL);
        }
        if ((i2 & 524288) > 0) {
            enumSetNoneOf2.add(AclEntryPermission.WRITE_OWNER);
        }
        if ((i2 & 1048576) > 0) {
            enumSetNoneOf2.add(AclEntryPermission.SYNCHRONIZE);
        }
        return AclEntry.newBuilder().setType(aclEntryType).setPrincipal(WindowsUserPrincipals.fromSid(j2 + 8)).setFlags(enumSetNoneOf).setPermissions(enumSetNoneOf2).build();
    }

    private static void encode(AclEntry aclEntry, long j2, long j3) throws WindowsException {
        if (aclEntry.type() != AclEntryType.ALLOW && aclEntry.type() != AclEntryType.DENY) {
            return;
        }
        boolean z2 = aclEntry.type() == AclEntryType.ALLOW;
        Set<AclEntryPermission> setPermissions = aclEntry.permissions();
        int i2 = 0;
        if (setPermissions.contains(AclEntryPermission.READ_DATA)) {
            i2 = 0 | 1;
        }
        if (setPermissions.contains(AclEntryPermission.WRITE_DATA)) {
            i2 |= 2;
        }
        if (setPermissions.contains(AclEntryPermission.APPEND_DATA)) {
            i2 |= 4;
        }
        if (setPermissions.contains(AclEntryPermission.READ_NAMED_ATTRS)) {
            i2 |= 8;
        }
        if (setPermissions.contains(AclEntryPermission.WRITE_NAMED_ATTRS)) {
            i2 |= 16;
        }
        if (setPermissions.contains(AclEntryPermission.EXECUTE)) {
            i2 |= 32;
        }
        if (setPermissions.contains(AclEntryPermission.DELETE_CHILD)) {
            i2 |= 64;
        }
        if (setPermissions.contains(AclEntryPermission.READ_ATTRIBUTES)) {
            i2 |= 128;
        }
        if (setPermissions.contains(AclEntryPermission.WRITE_ATTRIBUTES)) {
            i2 |= 256;
        }
        if (setPermissions.contains(AclEntryPermission.DELETE)) {
            i2 |= 65536;
        }
        if (setPermissions.contains(AclEntryPermission.READ_ACL)) {
            i2 |= 131072;
        }
        if (setPermissions.contains(AclEntryPermission.WRITE_ACL)) {
            i2 |= 262144;
        }
        if (setPermissions.contains(AclEntryPermission.WRITE_OWNER)) {
            i2 |= 524288;
        }
        if (setPermissions.contains(AclEntryPermission.SYNCHRONIZE)) {
            i2 |= 1048576;
        }
        Set<AclEntryFlag> setFlags = aclEntry.flags();
        byte b2 = 0;
        if (setFlags.contains(AclEntryFlag.FILE_INHERIT)) {
            b2 = (byte) (0 | 1);
        }
        if (setFlags.contains(AclEntryFlag.DIRECTORY_INHERIT)) {
            b2 = (byte) (b2 | 2);
        }
        if (setFlags.contains(AclEntryFlag.NO_PROPAGATE_INHERIT)) {
            b2 = (byte) (b2 | 4);
        }
        if (setFlags.contains(AclEntryFlag.INHERIT_ONLY)) {
            b2 = (byte) (b2 | 8);
        }
        if (z2) {
            WindowsNativeDispatcher.AddAccessAllowedAceEx(j3, b2, i2, j2);
        } else {
            WindowsNativeDispatcher.AddAccessDeniedAceEx(j3, b2, i2, j2);
        }
    }

    static WindowsSecurityDescriptor create(List<AclEntry> list) throws IOException {
        return new WindowsSecurityDescriptor(list);
    }

    static WindowsSecurityDescriptor fromAttribute(FileAttribute<?>... fileAttributeArr) throws IOException {
        WindowsSecurityDescriptor windowsSecurityDescriptor = NULL_DESCRIPTOR;
        for (FileAttribute<?> fileAttribute : fileAttributeArr) {
            if (windowsSecurityDescriptor != NULL_DESCRIPTOR) {
                windowsSecurityDescriptor.release();
            }
            if (fileAttribute == null) {
                throw new NullPointerException();
            }
            if (fileAttribute.name().equals("acl:acl")) {
                windowsSecurityDescriptor = new WindowsSecurityDescriptor((List) fileAttribute.value());
            } else {
                throw new UnsupportedOperationException(PdfOps.SINGLE_QUOTE_TOKEN + fileAttribute.name() + "' not supported as initial attribute");
            }
        }
        return windowsSecurityDescriptor;
    }

    static List<AclEntry> getAcl(long j2) throws IOException {
        int iAceCount;
        long jGetSecurityDescriptorDacl = WindowsNativeDispatcher.GetSecurityDescriptorDacl(j2);
        if (jGetSecurityDescriptorDacl == 0) {
            iAceCount = 0;
        } else {
            iAceCount = WindowsNativeDispatcher.GetAclInformation(jGetSecurityDescriptorDacl).aceCount();
        }
        ArrayList arrayList = new ArrayList(iAceCount);
        for (int i2 = 0; i2 < iAceCount; i2++) {
            AclEntry aclEntryDecode = decode(WindowsNativeDispatcher.GetAce(jGetSecurityDescriptorDacl, i2));
            if (aclEntryDecode != null) {
                arrayList.add(aclEntryDecode);
            }
        }
        return arrayList;
    }
}
