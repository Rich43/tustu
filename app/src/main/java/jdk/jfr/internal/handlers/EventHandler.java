package jdk.jfr.internal.handlers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import jdk.jfr.EventType;
import jdk.jfr.internal.EventControl;
import jdk.jfr.internal.JVM;
import jdk.jfr.internal.PlatformEventType;
import jdk.jfr.internal.PrivateAccess;
import jdk.jfr.internal.StringPool;

/* loaded from: jfr.jar:jdk/jfr/internal/handlers/EventHandler.class */
public abstract class EventHandler {
    protected final PlatformEventType platformEventType;
    private final EventType eventType;
    private final EventControl eventControl;

    protected EventHandler(boolean z2, EventType eventType, EventControl eventControl) {
        if (System.getSecurityManager() != null && EventHandler.class.getClassLoader() != getClass().getClassLoader()) {
            throw new SecurityException("Illegal subclass");
        }
        this.eventType = eventType;
        this.platformEventType = PrivateAccess.getInstance().getPlatformEventType(eventType);
        this.eventControl = eventControl;
        this.platformEventType.setRegistered(z2);
    }

    protected final StringPool createStringFieldWriter() {
        return new StringPool();
    }

    public final boolean shouldCommit(long j2) {
        return isEnabled() && j2 >= this.platformEventType.getThresholdTicks();
    }

    public final boolean isEnabled() {
        return this.platformEventType.isCommitable();
    }

    public final EventType getEventType() {
        return this.eventType;
    }

    public final PlatformEventType getPlatformEventType() {
        return this.platformEventType;
    }

    public final EventControl getEventControl() {
        return this.eventControl;
    }

    public static long timestamp() {
        return JVM.counterTime();
    }

    public static long duration(long j2) {
        if (j2 == 0) {
            return 0L;
        }
        return timestamp() - j2;
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

    public boolean isRegistered() {
        return this.platformEventType.isRegistered();
    }

    public boolean setRegistered(boolean z2) {
        return this.platformEventType.setRegistered(z2);
    }
}
