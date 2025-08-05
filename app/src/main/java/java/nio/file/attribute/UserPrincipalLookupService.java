package java.nio.file.attribute;

import java.io.IOException;

/* loaded from: rt.jar:java/nio/file/attribute/UserPrincipalLookupService.class */
public abstract class UserPrincipalLookupService {
    public abstract UserPrincipal lookupPrincipalByName(String str) throws IOException;

    public abstract GroupPrincipal lookupPrincipalByGroupName(String str) throws IOException;

    protected UserPrincipalLookupService() {
    }
}
