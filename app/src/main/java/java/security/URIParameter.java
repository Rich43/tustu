package java.security;

import java.net.URI;
import java.security.Policy;
import javax.security.auth.login.Configuration;

/* loaded from: rt.jar:java/security/URIParameter.class */
public class URIParameter implements Policy.Parameters, Configuration.Parameters {
    private URI uri;

    public URIParameter(URI uri) {
        if (uri == null) {
            throw new NullPointerException("invalid null URI");
        }
        this.uri = uri;
    }

    public URI getURI() {
        return this.uri;
    }
}
