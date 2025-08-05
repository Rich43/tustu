package sun.nio.fs;

import java.io.IOException;
import java.nio.file.attribute.AclFileAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:sun/nio/fs/AbstractAclFileAttributeView.class */
abstract class AbstractAclFileAttributeView implements AclFileAttributeView, DynamicFileAttributeView {
    private static final String OWNER_NAME = "owner";
    private static final String ACL_NAME = "acl";

    AbstractAclFileAttributeView() {
    }

    @Override // java.nio.file.attribute.AclFileAttributeView, java.nio.file.attribute.FileOwnerAttributeView, java.nio.file.attribute.AttributeView
    public final String name() {
        return ACL_NAME;
    }

    @Override // sun.nio.fs.DynamicFileAttributeView
    public final void setAttribute(String str, Object obj) throws IOException {
        if (str.equals(OWNER_NAME)) {
            setOwner((UserPrincipal) obj);
        } else {
            if (str.equals(ACL_NAME)) {
                setAcl((List) obj);
                return;
            }
            throw new IllegalArgumentException(PdfOps.SINGLE_QUOTE_TOKEN + name() + CallSiteDescriptor.TOKEN_DELIMITER + str + "' not recognized");
        }
    }

    @Override // sun.nio.fs.DynamicFileAttributeView
    public final Map<String, Object> readAttributes(String[] strArr) throws IOException {
        boolean z2 = false;
        boolean z3 = false;
        for (String str : strArr) {
            if (str.equals("*")) {
                z3 = true;
                z2 = true;
            } else if (str.equals(ACL_NAME)) {
                z2 = true;
            } else if (str.equals(OWNER_NAME)) {
                z3 = true;
            } else {
                throw new IllegalArgumentException(PdfOps.SINGLE_QUOTE_TOKEN + name() + CallSiteDescriptor.TOKEN_DELIMITER + str + "' not recognized");
            }
        }
        HashMap map = new HashMap(2);
        if (z2) {
            map.put(ACL_NAME, getAcl());
        }
        if (z3) {
            map.put(OWNER_NAME, getOwner());
        }
        return Collections.unmodifiableMap(map);
    }
}
