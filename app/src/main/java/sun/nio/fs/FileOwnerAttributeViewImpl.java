package sun.nio.fs;

import java.io.IOException;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.util.HashMap;
import java.util.Map;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:sun/nio/fs/FileOwnerAttributeViewImpl.class */
final class FileOwnerAttributeViewImpl implements FileOwnerAttributeView, DynamicFileAttributeView {
    private static final String OWNER_NAME = "owner";
    private final FileAttributeView view;
    private final boolean isPosixView = true;

    FileOwnerAttributeViewImpl(PosixFileAttributeView posixFileAttributeView) {
        this.view = posixFileAttributeView;
    }

    FileOwnerAttributeViewImpl(AclFileAttributeView aclFileAttributeView) {
        this.view = aclFileAttributeView;
    }

    @Override // java.nio.file.attribute.FileOwnerAttributeView, java.nio.file.attribute.AttributeView
    public String name() {
        return OWNER_NAME;
    }

    @Override // sun.nio.fs.DynamicFileAttributeView
    public void setAttribute(String str, Object obj) throws IOException {
        if (str.equals(OWNER_NAME)) {
            setOwner((UserPrincipal) obj);
            return;
        }
        throw new IllegalArgumentException(PdfOps.SINGLE_QUOTE_TOKEN + name() + CallSiteDescriptor.TOKEN_DELIMITER + str + "' not recognized");
    }

    @Override // sun.nio.fs.DynamicFileAttributeView
    public Map<String, Object> readAttributes(String[] strArr) throws IOException {
        HashMap map = new HashMap();
        for (String str : strArr) {
            if (str.equals("*") || str.equals(OWNER_NAME)) {
                map.put(OWNER_NAME, getOwner());
            } else {
                throw new IllegalArgumentException(PdfOps.SINGLE_QUOTE_TOKEN + name() + CallSiteDescriptor.TOKEN_DELIMITER + str + "' not recognized");
            }
        }
        return map;
    }

    @Override // java.nio.file.attribute.FileOwnerAttributeView
    public UserPrincipal getOwner() throws IOException {
        if (this.isPosixView) {
            return ((PosixFileAttributeView) this.view).readAttributes().owner();
        }
        return ((AclFileAttributeView) this.view).getOwner();
    }

    @Override // java.nio.file.attribute.FileOwnerAttributeView
    public void setOwner(UserPrincipal userPrincipal) throws IOException {
        if (this.isPosixView) {
            ((PosixFileAttributeView) this.view).setOwner(userPrincipal);
        } else {
            ((AclFileAttributeView) this.view).setOwner(userPrincipal);
        }
    }
}
