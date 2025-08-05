package javax.management.remote;

import javax.security.auth.Subject;

/* loaded from: rt.jar:javax/management/remote/JMXAuthenticator.class */
public interface JMXAuthenticator {
    Subject authenticate(Object obj);
}
