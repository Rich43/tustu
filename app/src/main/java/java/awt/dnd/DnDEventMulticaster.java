package java.awt.dnd;

import java.awt.AWTEventMulticaster;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.EventListener;

/* loaded from: rt.jar:java/awt/dnd/DnDEventMulticaster.class */
class DnDEventMulticaster extends AWTEventMulticaster implements DragSourceListener, DragSourceMotionListener {
    protected DnDEventMulticaster(EventListener eventListener, EventListener eventListener2) {
        super(eventListener, eventListener2);
    }

    @Override // java.awt.dnd.DragSourceListener
    public void dragEnter(DragSourceDragEvent dragSourceDragEvent) {
        ((DragSourceListener) this.f12359a).dragEnter(dragSourceDragEvent);
        ((DragSourceListener) this.f12360b).dragEnter(dragSourceDragEvent);
    }

    @Override // java.awt.dnd.DragSourceListener
    public void dragOver(DragSourceDragEvent dragSourceDragEvent) {
        ((DragSourceListener) this.f12359a).dragOver(dragSourceDragEvent);
        ((DragSourceListener) this.f12360b).dragOver(dragSourceDragEvent);
    }

    @Override // java.awt.dnd.DragSourceListener
    public void dropActionChanged(DragSourceDragEvent dragSourceDragEvent) {
        ((DragSourceListener) this.f12359a).dropActionChanged(dragSourceDragEvent);
        ((DragSourceListener) this.f12360b).dropActionChanged(dragSourceDragEvent);
    }

    @Override // java.awt.dnd.DragSourceListener
    public void dragExit(DragSourceEvent dragSourceEvent) {
        ((DragSourceListener) this.f12359a).dragExit(dragSourceEvent);
        ((DragSourceListener) this.f12360b).dragExit(dragSourceEvent);
    }

    @Override // java.awt.dnd.DragSourceListener
    public void dragDropEnd(DragSourceDropEvent dragSourceDropEvent) {
        ((DragSourceListener) this.f12359a).dragDropEnd(dragSourceDropEvent);
        ((DragSourceListener) this.f12360b).dragDropEnd(dragSourceDropEvent);
    }

    @Override // java.awt.dnd.DragSourceMotionListener
    public void dragMouseMoved(DragSourceDragEvent dragSourceDragEvent) {
        ((DragSourceMotionListener) this.f12359a).dragMouseMoved(dragSourceDragEvent);
        ((DragSourceMotionListener) this.f12360b).dragMouseMoved(dragSourceDragEvent);
    }

    public static DragSourceListener add(DragSourceListener dragSourceListener, DragSourceListener dragSourceListener2) {
        return (DragSourceListener) addInternal(dragSourceListener, dragSourceListener2);
    }

    public static DragSourceMotionListener add(DragSourceMotionListener dragSourceMotionListener, DragSourceMotionListener dragSourceMotionListener2) {
        return (DragSourceMotionListener) addInternal(dragSourceMotionListener, dragSourceMotionListener2);
    }

    public static DragSourceListener remove(DragSourceListener dragSourceListener, DragSourceListener dragSourceListener2) {
        return (DragSourceListener) removeInternal(dragSourceListener, dragSourceListener2);
    }

    public static DragSourceMotionListener remove(DragSourceMotionListener dragSourceMotionListener, DragSourceMotionListener dragSourceMotionListener2) {
        return (DragSourceMotionListener) removeInternal(dragSourceMotionListener, dragSourceMotionListener2);
    }

    protected static EventListener addInternal(EventListener eventListener, EventListener eventListener2) {
        return eventListener == null ? eventListener2 : eventListener2 == null ? eventListener : new DnDEventMulticaster(eventListener, eventListener2);
    }

    @Override // java.awt.AWTEventMulticaster
    protected EventListener remove(EventListener eventListener) {
        if (eventListener == this.f12359a) {
            return this.f12360b;
        }
        if (eventListener == this.f12360b) {
            return this.f12359a;
        }
        EventListener eventListenerRemoveInternal = removeInternal(this.f12359a, eventListener);
        EventListener eventListenerRemoveInternal2 = removeInternal(this.f12360b, eventListener);
        if (eventListenerRemoveInternal == this.f12359a && eventListenerRemoveInternal2 == this.f12360b) {
            return this;
        }
        return addInternal(eventListenerRemoveInternal, eventListenerRemoveInternal2);
    }

    protected static EventListener removeInternal(EventListener eventListener, EventListener eventListener2) {
        if (eventListener == eventListener2 || eventListener == null) {
            return null;
        }
        if (eventListener instanceof DnDEventMulticaster) {
            return ((DnDEventMulticaster) eventListener).remove(eventListener2);
        }
        return eventListener;
    }

    protected static void save(ObjectOutputStream objectOutputStream, String str, EventListener eventListener) throws IOException {
        AWTEventMulticaster.save(objectOutputStream, str, eventListener);
    }
}
