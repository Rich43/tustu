package javax.security.auth;

/* loaded from: rt.jar:javax/security/auth/Refreshable.class */
public interface Refreshable {
    boolean isCurrent();

    void refresh() throws RefreshFailedException;
}
