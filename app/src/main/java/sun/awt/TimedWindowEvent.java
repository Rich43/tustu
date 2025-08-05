package sun.awt;

import java.awt.Window;
import java.awt.event.WindowEvent;

/* loaded from: rt.jar:sun/awt/TimedWindowEvent.class */
public class TimedWindowEvent extends WindowEvent {
    private long time;

    public long getWhen() {
        return this.time;
    }

    public TimedWindowEvent(Window window, int i2, Window window2, long j2) {
        super(window, i2, window2);
        this.time = j2;
    }

    public TimedWindowEvent(Window window, int i2, Window window2, int i3, int i4, long j2) {
        super(window, i2, window2, i3, i4);
        this.time = j2;
    }
}
