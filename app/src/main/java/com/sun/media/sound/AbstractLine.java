package com.sun.media.sound;

import java.util.Map;
import java.util.Vector;
import java.util.WeakHashMap;
import javax.sound.sampled.Control;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;

/* loaded from: rt.jar:com/sun/media/sound/AbstractLine.class */
abstract class AbstractLine implements Line {
    protected final Line.Info info;
    protected Control[] controls;
    AbstractMixer mixer;
    private volatile boolean open;
    private final Vector listeners = new Vector();
    private static final Map<ThreadGroup, EventDispatcher> dispatchers = new WeakHashMap();

    @Override // javax.sound.sampled.Line
    public abstract void open() throws LineUnavailableException;

    @Override // javax.sound.sampled.Line, java.lang.AutoCloseable
    public abstract void close();

    protected AbstractLine(Line.Info info, AbstractMixer abstractMixer, Control[] controlArr) {
        controlArr = controlArr == null ? new Control[0] : controlArr;
        this.info = info;
        this.mixer = abstractMixer;
        this.controls = controlArr;
    }

    @Override // javax.sound.sampled.Line
    public final Line.Info getLineInfo() {
        return this.info;
    }

    @Override // javax.sound.sampled.Line
    public final boolean isOpen() {
        return this.open;
    }

    @Override // javax.sound.sampled.Line
    public final void addLineListener(LineListener lineListener) {
        synchronized (this.listeners) {
            if (!this.listeners.contains(lineListener)) {
                this.listeners.addElement(lineListener);
            }
        }
    }

    @Override // javax.sound.sampled.Line
    public final void removeLineListener(LineListener lineListener) {
        this.listeners.removeElement(lineListener);
    }

    @Override // javax.sound.sampled.Line
    public final Control[] getControls() {
        Control[] controlArr = new Control[this.controls.length];
        for (int i2 = 0; i2 < this.controls.length; i2++) {
            controlArr[i2] = this.controls[i2];
        }
        return controlArr;
    }

    @Override // javax.sound.sampled.Line
    public final boolean isControlSupported(Control.Type type) {
        if (type == null) {
            return false;
        }
        for (int i2 = 0; i2 < this.controls.length; i2++) {
            if (type == this.controls[i2].getType()) {
                return true;
            }
        }
        return false;
    }

    @Override // javax.sound.sampled.Line
    public final Control getControl(Control.Type type) {
        if (type != null) {
            for (int i2 = 0; i2 < this.controls.length; i2++) {
                if (type == this.controls[i2].getType()) {
                    return this.controls[i2];
                }
            }
        }
        throw new IllegalArgumentException("Unsupported control type: " + ((Object) type));
    }

    final void setOpen(boolean z2) {
        boolean z3 = false;
        long longFramePosition = getLongFramePosition();
        synchronized (this) {
            if (this.open != z2) {
                this.open = z2;
                z3 = true;
            }
        }
        if (z3) {
            if (z2) {
                sendEvents(new LineEvent(this, LineEvent.Type.OPEN, longFramePosition));
            } else {
                sendEvents(new LineEvent(this, LineEvent.Type.CLOSE, longFramePosition));
            }
        }
    }

    final void sendEvents(LineEvent lineEvent) {
        getEventDispatcher().sendAudioEvents(lineEvent, this.listeners);
    }

    public final int getFramePosition() {
        return (int) getLongFramePosition();
    }

    public long getLongFramePosition() {
        return -1L;
    }

    final AbstractMixer getMixer() {
        return this.mixer;
    }

    final EventDispatcher getEventDispatcher() {
        EventDispatcher eventDispatcher;
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        synchronized (dispatchers) {
            EventDispatcher eventDispatcher2 = dispatchers.get(threadGroup);
            if (eventDispatcher2 == null) {
                eventDispatcher2 = new EventDispatcher();
                dispatchers.put(threadGroup, eventDispatcher2);
                eventDispatcher2.start();
            }
            eventDispatcher = eventDispatcher2;
        }
        return eventDispatcher;
    }
}
