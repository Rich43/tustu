package java.awt.dnd;

import java.util.EventListener;

/* loaded from: rt.jar:java/awt/dnd/DragSourceListener.class */
public interface DragSourceListener extends EventListener {
    void dragEnter(DragSourceDragEvent dragSourceDragEvent);

    void dragOver(DragSourceDragEvent dragSourceDragEvent);

    void dropActionChanged(DragSourceDragEvent dragSourceDragEvent);

    void dragExit(DragSourceEvent dragSourceEvent);

    void dragDropEnd(DragSourceDropEvent dragSourceDropEvent);
}
