package java.security;

import javax.security.auth.Subject;

/* loaded from: rt.jar:java/security/Principal.class */
public interface Principal {
    boolean equals(Object obj);

    String toString();

    int hashCode();

    String getName();

    default boolean implies(Subject subject) {
        if (subject == null) {
            return false;
        }
        return subject.getPrincipals().contains(this);
    }
}
