package jdk.jfr;

@Enabled(true)
@Registered(true)
@StackTrace(true)
/* loaded from: jfr.jar:jdk/jfr/Event.class */
public abstract class Event {
    protected Event() {
    }

    public final void begin() {
    }

    public final void end() {
    }

    public final void commit() {
    }

    public final boolean isEnabled() {
        return false;
    }

    public final boolean shouldCommit() {
        return false;
    }

    public final void set(int i2, Object obj) {
    }
}
