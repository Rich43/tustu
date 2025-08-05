package javafx.animation;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javafx.beans.NamedArg;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:javafx/animation/KeyFrame.class */
public final class KeyFrame {
    private static final EventHandler<ActionEvent> DEFAULT_ON_FINISHED;
    private static final String DEFAULT_NAME;
    private final Duration time;
    private final Set<KeyValue> values;
    private final EventHandler<ActionEvent> onFinished;
    private final String name;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !KeyFrame.class.desiredAssertionStatus();
        DEFAULT_ON_FINISHED = null;
        DEFAULT_NAME = null;
    }

    public Duration getTime() {
        return this.time;
    }

    public Set<KeyValue> getValues() {
        return this.values;
    }

    public EventHandler<ActionEvent> getOnFinished() {
        return this.onFinished;
    }

    public String getName() {
        return this.name;
    }

    public KeyFrame(@NamedArg(SchemaSymbols.ATTVAL_TIME) Duration time, @NamedArg("name") String name, @NamedArg("onFinished") EventHandler<ActionEvent> onFinished, @NamedArg("values") Collection<KeyValue> values) {
        Set<KeyValue> setUnmodifiableSet;
        if (time == null) {
            throw new NullPointerException("The time has to be specified");
        }
        if (time.lessThan(Duration.ZERO) || time.equals(Duration.UNKNOWN)) {
            throw new IllegalArgumentException("The time is invalid.");
        }
        this.time = time;
        this.name = name;
        if (values != null) {
            Set<KeyValue> set = new CopyOnWriteArraySet<>(values);
            set.remove(null);
            if (set.size() == 0) {
                setUnmodifiableSet = Collections.emptySet();
            } else if (set.size() == 1) {
                setUnmodifiableSet = Collections.singleton(set.iterator().next());
            } else {
                setUnmodifiableSet = Collections.unmodifiableSet(set);
            }
            this.values = setUnmodifiableSet;
        } else {
            this.values = Collections.emptySet();
        }
        this.onFinished = onFinished;
    }

    public KeyFrame(@NamedArg(SchemaSymbols.ATTVAL_TIME) Duration time, @NamedArg("name") String name, @NamedArg("onFinished") EventHandler<ActionEvent> onFinished, @NamedArg("values") KeyValue... values) {
        Set<KeyValue> setUnmodifiableSet;
        if (time == null) {
            throw new NullPointerException("The time has to be specified");
        }
        if (time.lessThan(Duration.ZERO) || time.equals(Duration.UNKNOWN)) {
            throw new IllegalArgumentException("The time is invalid.");
        }
        this.time = time;
        this.name = name;
        if (values != null) {
            Set<KeyValue> set = new CopyOnWriteArraySet<>();
            for (KeyValue keyValue : values) {
                if (keyValue != null) {
                    set.add(keyValue);
                }
            }
            if (set.size() == 0) {
                setUnmodifiableSet = Collections.emptySet();
            } else if (set.size() == 1) {
                setUnmodifiableSet = Collections.singleton(set.iterator().next());
            } else {
                setUnmodifiableSet = Collections.unmodifiableSet(set);
            }
            this.values = setUnmodifiableSet;
        } else {
            this.values = Collections.emptySet();
        }
        this.onFinished = onFinished;
    }

    public KeyFrame(@NamedArg(SchemaSymbols.ATTVAL_TIME) Duration time, @NamedArg("onFinished") EventHandler<ActionEvent> onFinished, @NamedArg("values") KeyValue... values) {
        this(time, DEFAULT_NAME, onFinished, values);
    }

    public KeyFrame(@NamedArg(SchemaSymbols.ATTVAL_TIME) Duration time, @NamedArg("name") String name, @NamedArg("values") KeyValue... values) {
        this(time, name, DEFAULT_ON_FINISHED, values);
    }

    public KeyFrame(@NamedArg(SchemaSymbols.ATTVAL_TIME) Duration time, @NamedArg("values") KeyValue... values) {
        this(time, DEFAULT_NAME, DEFAULT_ON_FINISHED, values);
    }

    public String toString() {
        return "KeyFrame [time=" + ((Object) this.time) + ", values=" + ((Object) this.values) + ", onFinished=" + ((Object) this.onFinished) + ", name=" + this.name + "]";
    }

    public int hashCode() {
        if (!$assertionsDisabled && (this.time == null || this.values == null)) {
            throw new AssertionError();
        }
        int result = (31 * 1) + this.time.hashCode();
        return (31 * ((31 * ((31 * result) + (this.name == null ? 0 : this.name.hashCode()))) + (this.onFinished == null ? 0 : this.onFinished.hashCode()))) + this.values.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof KeyFrame) {
            KeyFrame kf = (KeyFrame) obj;
            if ($assertionsDisabled || !(this.time == null || this.values == null || kf.time == null || kf.values == null)) {
                return this.time.equals(kf.time) && (this.name != null ? this.name.equals(kf.name) : kf.name == null) && (this.onFinished != null ? this.onFinished.equals(kf.onFinished) : kf.onFinished == null) && this.values.equals(kf.values);
            }
            throw new AssertionError();
        }
        return false;
    }
}
