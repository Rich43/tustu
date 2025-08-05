package javax.net.ssl;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/* loaded from: rt.jar:javax/net/ssl/KeyStoreBuilderParameters.class */
public class KeyStoreBuilderParameters implements ManagerFactoryParameters {
    private final List<KeyStore.Builder> parameters;

    public KeyStoreBuilderParameters(KeyStore.Builder builder) {
        this.parameters = Collections.singletonList(Objects.requireNonNull(builder));
    }

    public KeyStoreBuilderParameters(List<KeyStore.Builder> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.parameters = Collections.unmodifiableList(new ArrayList(list));
    }

    public List<KeyStore.Builder> getParameters() {
        return this.parameters;
    }
}
