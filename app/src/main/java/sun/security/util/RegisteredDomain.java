package sun.security.util;

import java.util.Optional;

/* loaded from: rt.jar:sun/security/util/RegisteredDomain.class */
public interface RegisteredDomain {

    /* loaded from: rt.jar:sun/security/util/RegisteredDomain$Type.class */
    public enum Type {
        ICANN,
        PRIVATE
    }

    String name();

    Type type();

    String publicSuffix();

    static Optional<RegisteredDomain> from(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        return Optional.ofNullable(sun.net.RegisteredDomain.registeredDomain(str));
    }
}
