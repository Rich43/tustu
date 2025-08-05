package javax.security.auth;

/* loaded from: rt.jar:javax/security/auth/Destroyable.class */
public interface Destroyable {
    default void destroy() throws DestroyFailedException {
        throw new DestroyFailedException();
    }

    default boolean isDestroyed() {
        return false;
    }
}
