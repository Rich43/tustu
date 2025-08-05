package sun.security.action;

import java.security.PrivilegedAction;
import java.security.Provider;
import java.util.Map;

/* loaded from: rt.jar:sun/security/action/PutAllAction.class */
public class PutAllAction implements PrivilegedAction<Void> {
    private final Provider provider;
    private final Map<?, ?> map;

    public PutAllAction(Provider provider, Map<?, ?> map) {
        this.provider = provider;
        this.map = map;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.security.PrivilegedAction
    public Void run() {
        this.provider.putAll(this.map);
        return null;
    }
}
