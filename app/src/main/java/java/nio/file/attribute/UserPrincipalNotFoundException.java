package java.nio.file.attribute;

import java.io.IOException;

/* loaded from: rt.jar:java/nio/file/attribute/UserPrincipalNotFoundException.class */
public class UserPrincipalNotFoundException extends IOException {
    static final long serialVersionUID = -5369283889045833024L;
    private final String name;

    public UserPrincipalNotFoundException(String str) {
        this.name = str;
    }

    public String getName() {
        return this.name;
    }
}
