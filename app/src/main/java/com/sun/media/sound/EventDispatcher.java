package com.sun.media.sound;

import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.ControllerEventListener;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import sun.java2d.marlin.MarlinConst;

/* loaded from: rt.jar:com/sun/media/sound/EventDispatcher.class */
final class EventDispatcher implements Runnable {
    private static final int AUTO_CLOSE_TIME = 5000;
    private final ArrayList eventQueue = new ArrayList();
    private Thread thread = null;
    private final ArrayList<ClipInfo> autoClosingClips = new ArrayList<>();
    private final ArrayList<LineMonitor> lineMonitors = new ArrayList<>();
    static final int LINE_MONITOR_TIME = 400;

    /* loaded from: rt.jar:com/sun/media/sound/EventDispatcher$LineMonitor.class */
    interface LineMonitor {
        void checkLine();
    }

    EventDispatcher() {
    }

    synchronized void start() {
        if (this.thread == null) {
            this.thread = JSSecurityManager.createThread(this, "Java Sound Event Dispatcher", true, -1, true);
        }
    }

    void processEvent(EventInfo eventInfo) {
        int listenerCount = eventInfo.getListenerCount();
        if (eventInfo.getEvent() instanceof LineEvent) {
            LineEvent lineEvent = (LineEvent) eventInfo.getEvent();
            for (int i2 = 0; i2 < listenerCount; i2++) {
                try {
                    ((LineListener) eventInfo.getListener(i2)).update(lineEvent);
                } catch (Throwable th) {
                }
            }
            return;
        }
        if (eventInfo.getEvent() instanceof MetaMessage) {
            MetaMessage metaMessage = (MetaMessage) eventInfo.getEvent();
            for (int i3 = 0; i3 < listenerCount; i3++) {
                try {
                    ((MetaEventListener) eventInfo.getListener(i3)).meta(metaMessage);
                } catch (Throwable th2) {
                }
            }
            return;
        }
        if (eventInfo.getEvent() instanceof ShortMessage) {
            ShortMessage shortMessage = (ShortMessage) eventInfo.getEvent();
            if ((shortMessage.getStatus() & 240) == 176) {
                for (int i4 = 0; i4 < listenerCount; i4++) {
                    try {
                        ((ControllerEventListener) eventInfo.getListener(i4)).controlChange(shortMessage);
                    } catch (Throwable th3) {
                    }
                }
                return;
            }
            return;
        }
        Printer.err("Unknown event type: " + eventInfo.getEvent());
    }

    void dispatchEvents() {
        EventInfo eventInfo = null;
        synchronized (this) {
            try {
                if (this.eventQueue.size() == 0) {
                    if (this.autoClosingClips.size() > 0 || this.lineMonitors.size() > 0) {
                        int i2 = 5000;
                        if (this.lineMonitors.size() > 0) {
                            i2 = 400;
                        }
                        wait(i2);
                    } else {
                        wait();
                    }
                }
            } catch (InterruptedException e2) {
            }
            if (this.eventQueue.size() > 0) {
                eventInfo = (EventInfo) this.eventQueue.remove(0);
            }
        }
        if (eventInfo != null) {
            processEvent(eventInfo);
            return;
        }
        if (this.autoClosingClips.size() > 0) {
            closeAutoClosingClips();
        }
        if (this.lineMonitors.size() > 0) {
            monitorLines();
        }
    }

    private synchronized void postEvent(EventInfo eventInfo) {
        this.eventQueue.add(eventInfo);
        notifyAll();
    }

    @Override // java.lang.Runnable
    public void run() {
        while (true) {
            try {
                dispatchEvents();
            } catch (Throwable th) {
            }
        }
    }

    void sendAudioEvents(Object obj, List list) {
        if (list == null || list.size() == 0) {
            return;
        }
        start();
        postEvent(new EventInfo(obj, list));
    }

    private void closeAutoClosingClips() {
        synchronized (this.autoClosingClips) {
            long jCurrentTimeMillis = System.currentTimeMillis();
            for (int size = this.autoClosingClips.size() - 1; size >= 0; size--) {
                ClipInfo clipInfo = this.autoClosingClips.get(size);
                if (clipInfo.isExpired(jCurrentTimeMillis)) {
                    AutoClosingClip clip = clipInfo.getClip();
                    if (!clip.isOpen() || !clip.isAutoClosing()) {
                        this.autoClosingClips.remove(size);
                    } else if (!clip.isRunning() && !clip.isActive() && clip.isAutoClosing()) {
                        clip.close();
                    }
                }
            }
        }
    }

    private int getAutoClosingClipIndex(AutoClosingClip autoClosingClip) {
        synchronized (this.autoClosingClips) {
            for (int size = this.autoClosingClips.size() - 1; size >= 0; size--) {
                if (autoClosingClip.equals(this.autoClosingClips.get(size).getClip())) {
                    return size;
                }
            }
            return -1;
        }
    }

    void autoClosingClipOpened(AutoClosingClip autoClosingClip) {
        int autoClosingClipIndex;
        synchronized (this.autoClosingClips) {
            autoClosingClipIndex = getAutoClosingClipIndex(autoClosingClip);
            if (autoClosingClipIndex == -1) {
                this.autoClosingClips.add(new ClipInfo(autoClosingClip));
            }
        }
        if (autoClosingClipIndex == -1) {
            synchronized (this) {
                notifyAll();
            }
        }
    }

    void autoClosingClipClosed(AutoClosingClip autoClosingClip) {
        synchronized (this.autoClosingClips) {
            int autoClosingClipIndex = getAutoClosingClipIndex(autoClosingClip);
            if (autoClosingClipIndex != -1) {
                this.autoClosingClips.remove(autoClosingClipIndex);
            }
        }
    }

    private void monitorLines() {
        synchronized (this.lineMonitors) {
            for (int i2 = 0; i2 < this.lineMonitors.size(); i2++) {
                this.lineMonitors.get(i2).checkLine();
            }
        }
    }

    void addLineMonitor(LineMonitor lineMonitor) {
        synchronized (this.lineMonitors) {
            if (this.lineMonitors.indexOf(lineMonitor) >= 0) {
                return;
            }
            this.lineMonitors.add(lineMonitor);
            synchronized (this) {
                notifyAll();
            }
        }
    }

    void removeLineMonitor(LineMonitor lineMonitor) {
        synchronized (this.lineMonitors) {
            if (this.lineMonitors.indexOf(lineMonitor) < 0) {
                return;
            }
            this.lineMonitors.remove(lineMonitor);
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/EventDispatcher$EventInfo.class */
    private class EventInfo {
        private final Object event;
        private final Object[] listeners;

        EventInfo(Object obj, List list) {
            this.event = obj;
            this.listeners = list.toArray();
        }

        Object getEvent() {
            return this.event;
        }

        int getListenerCount() {
            return this.listeners.length;
        }

        Object getListener(int i2) {
            return this.listeners[i2];
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/EventDispatcher$ClipInfo.class */
    private class ClipInfo {
        private final AutoClosingClip clip;
        private final long expiration = System.currentTimeMillis() + MarlinConst.statDump;

        ClipInfo(AutoClosingClip autoClosingClip) {
            this.clip = autoClosingClip;
        }

        AutoClosingClip getClip() {
            return this.clip;
        }

        boolean isExpired(long j2) {
            return j2 > this.expiration;
        }
    }
}
