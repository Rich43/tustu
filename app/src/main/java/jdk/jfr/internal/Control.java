package jdk.jfr.internal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/* loaded from: jfr.jar:jdk/jfr/internal/Control.class */
public abstract class Control {
    private final AccessControlContext context;
    private static final int CACHE_SIZE = 5;
    private final Set<?>[] cachedUnions;
    private final String[] cachedValues;
    private String defaultValue;
    private String lastValue;

    public abstract String combine(Set<String> set);

    public abstract void setValue(String str);

    public abstract String getValue();

    public Control(AccessControlContext accessControlContext) {
        this.cachedUnions = new HashSet[5];
        this.cachedValues = new String[5];
        Objects.requireNonNull(accessControlContext);
        this.context = accessControlContext;
    }

    public Control(String str) {
        this.cachedUnions = new HashSet[5];
        this.cachedValues = new String[5];
        this.defaultValue = str;
        this.context = null;
    }

    final void apply(Set<String> set) {
        setValueSafe(findCombineSafe(set));
    }

    final void setDefault() {
        if (this.defaultValue == null) {
            this.defaultValue = getValueSafe();
        }
        apply(this.defaultValue);
    }

    final String getValueSafe() {
        if (this.context == null) {
            return getValue();
        }
        return (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: jdk.jfr.internal.Control.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public String run() {
                try {
                    return Control.this.getValue();
                } catch (Throwable th) {
                    Logger.log(LogTag.JFR_SETTING, LogLevel.WARN, "Exception occured when trying to get value for " + ((Object) getClass()));
                    return Control.this.defaultValue != null ? Control.this.defaultValue : "";
                }
            }
        }, this.context);
    }

    private void apply(String str) {
        if (this.lastValue != null && Objects.equals(str, this.lastValue)) {
            return;
        }
        setValueSafe(str);
    }

    final void setValueSafe(final String str) {
        if (this.context == null) {
            try {
                setValue(str);
            } catch (Throwable th) {
                Logger.log(LogTag.JFR_SETTING, LogLevel.WARN, "Exception occured when setting value \"" + str + "\" for " + ((Object) getClass()));
            }
        } else {
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: jdk.jfr.internal.Control.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public Void run() {
                    try {
                        Control.this.setValue(str);
                        return null;
                    } catch (Throwable th2) {
                        Logger.log(LogTag.JFR_SETTING, LogLevel.WARN, "Exception occured when setting value \"" + str + "\" for " + ((Object) getClass()));
                        return null;
                    }
                }
            }, this.context);
        }
        this.lastValue = str;
    }

    private String combineSafe(final Set<String> set) {
        if (this.context == null) {
            return combine(set);
        }
        return (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: jdk.jfr.internal.Control.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public String run() {
                try {
                    Control.this.combine(Collections.unmodifiableSet(set));
                    return null;
                } catch (Throwable th) {
                    Logger.log(LogTag.JFR_SETTING, LogLevel.WARN, "Exception occured when combining " + ((Object) set) + " for " + ((Object) getClass()));
                    return null;
                }
            }
        }, this.context);
    }

    private final String findCombineSafe(Set<String> set) {
        if (set.size() == 1) {
            return set.iterator().next();
        }
        for (int i2 = 0; i2 < 5; i2++) {
            if (Objects.equals(this.cachedUnions[i2], set)) {
                return this.cachedValues[i2];
            }
        }
        String strCombineSafe = combineSafe(set);
        for (int i3 = 0; i3 < 4; i3++) {
            this.cachedUnions[i3 + 1] = this.cachedUnions[i3];
            this.cachedValues[i3 + 1] = this.cachedValues[i3];
        }
        this.cachedValues[0] = strCombineSafe;
        this.cachedUnions[0] = set;
        return strCombineSafe;
    }

    final String getDefaultValue() {
        return this.defaultValue;
    }

    final String getLastValue() {
        return this.lastValue;
    }

    public final Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    private final void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        throw new IOException("Object cannot be serialized");
    }

    private final void readObject(ObjectInputStream objectInputStream) throws IOException {
        throw new IOException("Class cannot be deserialized");
    }
}
